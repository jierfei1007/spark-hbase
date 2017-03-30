package com.fxiaoke.fhc.source

import java.sql.DriverManager

import org.apache.spark.SparkContext
import org.apache.spark.rdd.{JdbcRDD, RDD}

/**
  * Created by jief on 2017/2/27.
  */
object IbssEnterpriseTypeSource {

  /**
    *add by jief 20170227 从ibss库中获取企业类型
    * @param config
    * @param sparkContext
    * @return
    */
  def getIbssEnterpriseTypeRDD(config:java.util.Map[String,String],sparkContext: SparkContext):RDD[(Int,Int)]={

    val mysqlURL = config.get("mysqlURL")
    val mysqlUserName = config.get("mysqlUserName")
    val mysqlPassword = config.get("mysqlPassword")

    val enterpriseContactSql = " select enterprise_id,enterprise_type from enterprise_stream  WHERE 2 >= ? AND 2 <= ? "
    val ibssEnterpriseTypeRDD = new JdbcRDD(sparkContext, () => {
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      DriverManager.getConnection(mysqlURL, mysqlUserName, mysqlPassword)
    }, enterpriseContactSql, 0, 4, 1, row => {
      val eid = row.getInt("enterprise_id")
      val etype = row.getInt("enterprise_type")
      (eid, etype)
    })
    ibssEnterpriseTypeRDD
  }
}
