package com.fxiaoke.fhc.main

import java.text.SimpleDateFormat
import java.util.Calendar

import com.fxiaoke.fhc.bean.EnterpriseStaticBean
import com.fxiaoke.fhc.context.HuijuContext
import com.fxiaoke.fhc.job.EnterpriseInfoJob
import com.fxiaoke.fhc.log.PrintLog
import com.fxiaoke.fhc.source._
import com.fxiaoke.fhc.utils.{Config, HdfsHelper}
import org.apache.commons.lang.StringUtils
import org.apache.spark.rdd.RDD

/**
  * Created by jief on 2017/3/21.
  *  采用新的企业付费类型算法，
  *  后台存储采用parquet
  */
object RefreshEnterpriseStaticMain {

  val sf = new SimpleDateFormat("yyyy-MM-dd")
  val sf2 = new SimpleDateFormat("yyyy/MM/dd")
  val sf3 = new SimpleDateFormat("yyyyMMdd")


  def main(args: Array[String]): Unit = {
    //判断传入参数个数
    val waringMsg = "spark-submit XXX.jar  \n" +
      " runEnvironment(SDE,foneshare,preview) \n" +
      " runMode(local,yarn-cluster) \n" +
      " configName \n" +
      " runDate(2016-05-17) \n"
    if(args.length!=4){
      throw new RuntimeException("args numbers error! "+waringMsg)
    }
    //获取运行参数
    val runEnvironment = args(0)
    val runMode = args(1)
    val configName = args(2)
    val runDateStr = args(3)
    require(StringUtils.isNotEmpty(runEnvironment),"runEnvironment is empty!")
    require(StringUtils.isNotEmpty(runMode),"runMode is empty!")
    require(StringUtils.isNotEmpty(configName),"configName is empty!")
    require(StringUtils.isNotEmpty(runDateStr),"runDateStr is empty!")

    var runDate: java.util.Date = null
    try {
      runDate = sf.parse(runDateStr)
    } catch {
      case e: Exception => throw new RuntimeException("parse date :" +runDateStr+ "error!the right format is 1999-01-01")
    }

    //获取运行日期前一天的日期
    val calendar = Calendar.getInstance()
    calendar.setTime(runDate)
    calendar.add(Calendar.DATE, -1)
    val ydayDate = calendar.getTime

    // 从配置中心获取参数
    val propConfig = Config.getConfig(configName, runEnvironment)
    /**
      * 如果是刷历史数据，由于员工表备份从20160713开始有，所以20151012-20160713都使用20160713的数据进行跑数，之前就没有企业历史数据。
      * /facishare-data/fscloud/import_data/EnterpriseBak/2016/07/13/
      */
    var enterpriseSourcePath =  propConfig.get("enterpriseDailySourcePath")+"/"+sf2.format(runDate)
    if (runDate.before(sf.parse("2016-07-14"))){
      enterpriseSourcePath="/facishare-data/fscloud/import_data/EnterpriseBak/2016/07/13/"
    }
    val enterpriseTypeTableName = propConfig.get("enterpriseTypeTabeleName")
    val enterpriseStaticDailyTableName = propConfig.get("enterpriseStaticDailyTableName")
    val enterpriseStaticTableName = propConfig.get("enterpriseStaticTableName")
    val enterpriseStaticDailyOutPutPath = propConfig.get("enterpriseStaticDailyOutPutPath") + "/" + sf2.format(runDate)
    val enterpriseStaticOutPutPath = propConfig.get("enterpriseStaticOutPutPath")
    val enterpriseBlackTableName = propConfig.get("enterpriseBlackTableName")
    PrintLog.log(" enterpriseSourcePath " + enterpriseSourcePath)
    PrintLog.log(" enterpriseTypeTableName " + enterpriseTypeTableName)
    PrintLog.log(" enterpriseStaticTableName " + enterpriseStaticDailyTableName)
    PrintLog.log(" enterpriseStaticDailyOutPutPath " + enterpriseStaticDailyOutPutPath)
    PrintLog.log(" enterpriseStaticOutPutPath " + enterpriseStaticOutPutPath)
    PrintLog.log(" enterpriseBlackTableName " + enterpriseBlackTableName)

    /**
      * spark运行环境
      */
    val huijuContext = new HuijuContext(runEnvironment, "新企业维度表-静态表：dim_pub_enterprise_info_static ", runMode)
    val sparkContext = huijuContext.sparkContext
    /**
      * 从企业创建HDFS日志文件获取源企业信息
      */
    val enterpriseSourceRDD = SourceEnterpriseSource.getSourceEnterpriseRDD(sparkContext, enterpriseSourcePath)
    //获取历史所有下单企业
    val enterpriseIdRDD=EnterpriseOrderSource.createEidRDD(huijuContext.hiveContext,sf.format(runDate))
    val broadcastEidList=sparkContext.broadcast(enterpriseIdRDD.collect())
    //    /**
    //      * 从hive表获取企业类型DF
    //      */
    //    val enterpriseTypeRDD = EnterpriseTypeSource.getEnterpriseTypeRDD(huijuContext, enterpriseTypeTableName, runDate)
    /**
      * * 从IBSS库里获取企业类型DF
      */
//    val enterpriseTypeRDD=IbssEnterpriseTypeSource.getIbssEnterpriseTypeRDD(propConfig, sparkContext)

    /**
      * 从IBSS库里获取企业基本信息(现有的负责人信息和管理员信息是加密后的数据，如果之后的企业信息需要改成明文，则需要使用这个RDD来关联负责人信息和管理员信息)
      */
    //    val ibssEnterpriseRDD = IbssEnterpriseSource.getIbssEnterpriseRDD(propConfig, sparkContext)

    /**
      * 从IBSS库里获取代理商信息
      */
    val ibssVendorMap = IbssVendorSource.getIbssVendorRDD(propConfig, sparkContext).collect().toMap

    /**
      * 获取运算日期前一天的老的EnterpriseStatic信息
      */
    val ydayEnterpriseStaticRDD = EnterpriseStaticSource.getYdayEnterpriseStaticRDD(huijuContext, enterpriseStaticDailyTableName, ydayDate)

    /**
      * 获取行业类别
      */
    val industryMap = IndustrySource.getIndustryMap()

    /**
      * 城市city映射表，数据量小的RDD转换为Map，加速计算
      */
    val districtMap = DistrictSource.getDistrictRDD(propConfig, sparkContext).collect().toMap

    /**
      * 联营/代理商企业账号。数据量小的转换为Map
      */
    val agentEaMap = EnterpriseGroupJudgeSource.getAgentEnterpriseMap()

    /**
      * 获取企业黑名单EID(测试账号)，黑名单企业
      */
    val enterpriseBlackRDD = EnterpriseGroupJudgeSource.getEnterpriseBlackRDD(huijuContext, enterpriseBlackTableName)

    /**
      * 主逻辑计算
      */
    val enterpriseRDD:RDD[EnterpriseStaticBean]=EnterpriseInfoJob.getEnterpriseBaseInfo(enterpriseSourceRDD,broadcastEidList,enterpriseBlackRDD, ydayEnterpriseStaticRDD, ibssVendorMap,
      sf3.format(runDate), sf3.format(ydayDate), enterpriseStaticDailyOutPutPath, enterpriseStaticOutPutPath,
      industryMap, districtMap, agentEaMap,propConfig,sf.format(runDate))
    import huijuContext.hiveContext.implicits._
    val enterpriseDataFrame=huijuContext.sqlContext.createDataFrame(enterpriseRDD,classOf[EnterpriseStaticBean])
    HdfsHelper.saveAsParquet(enterpriseDataFrame,"/facishare-data/fscloud/dw_dim/dim_pub_enterprise_info_static_parquet",3)
//    val loadEnterpriseStaticDailyDailySql = "alter table " + enterpriseStaticDailyTableName + " add if not exists " +
//      "partition(dt='" + sf3.format(runDate) + "') " +
//      "location '" + enterpriseStaticDailyOutPutPath + "'"
//    println("loadEnterpriseStaticDailyDailySql : " + loadEnterpriseStaticDailyDailySql)
//    huijuContext.hiveContext.sql(loadEnterpriseStaticDailyDailySql)
    huijuContext.stop()
  }
}