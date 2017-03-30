package com.fxiaoke.fhc.source

import java.text.SimpleDateFormat
import java.util.Date

import com.fxiaoke.fhc.context.HuijuContext
import com.fxiaoke.fhc.log.PrintLog
import org.apache.spark.rdd.RDD

/**
  * Created by jiangxd on 2016/10/10.
  */
object EnterpriseTypeSource {

  /**
    * 从hive表里获取企业类型
    *
    * @param huiJuContext            汇聚spark环境
    * @param enterpriseTypeTableName 企业类型hive表名
    * @param runDate                 计算日期
    * @return 计算日期当天的企业类型DF    (eid, enterpriseType)  RDD[(Int, Int)]
    */
  def getEnterpriseTypeRDD(huiJuContext: HuijuContext, enterpriseTypeTableName: String, runDate: Date): RDD[(Int, Int)] = {
    try {
      val sf = new SimpleDateFormat("yyyyMMdd")
      val getEnterpriseTypeSql = "select distinct enterpriseID as eid ,enterpriseType from " + enterpriseTypeTableName + " where day=" + sf.format(runDate)
      PrintLog.log(" getEnterpriseTypeSql : " + getEnterpriseTypeSql)
      val enterpriseTypeDF = huiJuContext.hiveContext.sql(getEnterpriseTypeSql)
      val enterpriseTypeRDD = enterpriseTypeDF.map(row => {
        val eid = row.getInt(0)
        val enterpriseType = row.getInt(1)
        (eid, enterpriseType)
      })
      enterpriseTypeRDD
    } catch {
      case e: Exception => PrintLog.log(" getEnterpriseType is error : " + e); null
    }
  }

//  def getEnterpriseTypeDF(huiJuContext: HuijuContext, tableName: String, defaultDate: Date): DataFrame = {
//    try {
//      val sf = new SimpleDateFormat("yyyyMMdd")
//      val sql = "select enterpriseID as eid ,enterpriseType from " + tableName + " where day=" + sf.format(defaultDate)
//      val enterpriseTypeDF = huiJuContext.hiveContext.sql(sql)
//      enterpriseTypeDF
//    } catch {
//      case _ => null
//    }
//  }
//
//  private def getEnterpriseTypeRDDFromDF(enterpriseType: DataFrame): RDD[(Int, Int)] = {
//    val enterpriseTypeRdd = enterpriseType.map(row => {
//      val eid = Integer.parseInt(row.get(0).toString)
//      val enterpriseType = Integer.parseInt(row.get(1).toString)
//      (eid, enterpriseType)
//    })
//    enterpriseTypeRdd
//  }

}
