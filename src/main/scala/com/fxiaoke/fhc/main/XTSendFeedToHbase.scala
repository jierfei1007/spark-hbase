package com.fxiaoke.fhc.main

import com.fxiaoke.fhc.source.xtsendfeed.XTSendFeedSource
import com.fxiaoke.fhc.utils.{Config, XTSendFeed2HbaseUtil}
import org.apache.commons.lang.StringUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by jief on 2017/6/22.
  */
object XTSendFeedToHbase {

  def main(args: Array[String]): Unit = {
    val waringMsg = "spark-submit XXX.jar  " +
      " runEnvironment(SDE,foneshare,preview) \n" +
      " runMode(local,yarn-cluster) \n" +
      " configName \n"+
      " 20170601\n"+
      " 20170610"
    if (args.length != 5) {
      throw new RuntimeException("args numbers error! " + waringMsg)
    }
    //获取运行参数
    val runEnvironment = args(0)
    val runMode = args(1)
    val configName = args(2)
    val fromDate = args(3)
    val toDate = args(4)
    require(StringUtils.isNotEmpty(runEnvironment), "runEnvironment is empty!")
    require(StringUtils.isNotEmpty(runMode), "runMode is empty!")
    require(StringUtils.isNotEmpty(configName), "configName is empty!")
    require(StringUtils.isNotEmpty(fromDate), "fromDate is empty!")
    require(StringUtils.isNotEmpty(toDate), "toDate is empty!")
    require(toDate>=fromDate,"toDate must bigger then fromDate")
    val propConfig = Config.getConfig(configName, runEnvironment)
    val hbaseBatchSize = propConfig.get("hbase.batch").toInt
    val sparkConf = new SparkConf().setAppName("sendFeed入hbase").setMaster(runMode)
    val sparkContext = new SparkContext(sparkConf)
    val hiveContext: HiveContext = new HiveContext(sparkContext)
    val df=XTSendFeedSource.createDayDF(hiveContext,fromDate,toDate)
    val insertFeedTypes = XTSendFeed2HbaseUtil.insertFeedTypes(hbaseBatchSize, propConfig)
    df.coalesce(15).foreachPartition(insertFeedTypes)
    sparkContext.stop()
  }
}
