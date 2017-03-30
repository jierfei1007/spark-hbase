package com.fxiaoke.fhc.source

import java.sql.DriverManager

import org.apache.spark.SparkContext
import org.apache.spark.rdd.{JdbcRDD, RDD}

/**
  * Created by jiangxd on 2016/10/13.
  */
object DistrictSource {


  def getDistrictRDD(config: java.util.Map[String, String], sparkContext: SparkContext): RDD[(String, String)] = {

    val mysqlURL = config.get("mysqlURL")
    val mysqlUserName = config.get("mysqlUserName")
    val mysqlPassword = config.get("mysqlPassword")

    val districtSql = " SELECT value,name from g_district  WHERE 2 >= ? AND 2 <= ? "
    val districtRDD = new JdbcRDD(sparkContext, () => {
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      DriverManager.getConnection(mysqlURL, mysqlUserName, mysqlPassword)
    }, districtSql, 0, 4, 1, row => {
      val id = row.getString("value").trim
      val vendorName = row.getString("name")
      (id, vendorName)
    })
    districtRDD
  }

}
