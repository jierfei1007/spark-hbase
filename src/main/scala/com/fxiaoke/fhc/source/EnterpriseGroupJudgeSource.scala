package com.fxiaoke.fhc.source

import java.text.SimpleDateFormat
import java.util

import com.fxiaoke.fhc.context.HuijuContext
import com.fxiaoke.fhc.log.PrintLog
import org.apache.spark.rdd.RDD

import scala.io.Source

/**
  * Created by jiangxd on 2016/10/13.
  */
object EnterpriseGroupJudgeSource {

  /**
    * 获取代理商企业Map
    *
    * @return map[ea,1]
    */
  def getAgentEnterpriseMap(): util.HashMap[String, Int] = {
    val agentEaMap = new util.HashMap[String, Int]()
    val file = Source.fromURL(getClass.getResource("/agentEnterpriseAccount.txt")).getLines()

    for (line <- file) {
      agentEaMap.put(line.trim, 1)
    }
    agentEaMap
  }


  /**
    * 获取黑名单企业的RDD
    *
    * @param huiJuContext             运行环境
    * @param enterpriseBlackTableName 企业黑名单
    * @return RDD[eid,1]
    */

  def getEnterpriseBlackRDD(huiJuContext: HuijuContext, enterpriseBlackTableName: String): RDD[(Int, Int)] = {
    try {
      val sf = new SimpleDateFormat("yyyyMMdd")
      val getEnterpriseBlackSql = " select  distinct enterprise_id  from  " + enterpriseBlackTableName
      PrintLog.log(" getEnterpriseBlackSql : " + getEnterpriseBlackSql)
      val enterpriseBlackDF = huiJuContext.hiveContext.sql(getEnterpriseBlackSql)
      val enterpriseBlackRDD = enterpriseBlackDF.map(row => {
        val eid = row.getInt(0)
        (eid, 1)
      })
      enterpriseBlackRDD
    } catch {
      case e: Exception => PrintLog.log(" getEnterpriseBlackRDD is error : " + e)
        huiJuContext.sparkContext.parallelize(List(-1)).map(r => (r, 1))
    }
  }
}
