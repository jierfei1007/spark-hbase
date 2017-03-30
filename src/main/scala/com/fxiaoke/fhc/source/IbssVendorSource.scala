package com.fxiaoke.fhc.source

import java.sql.DriverManager

import org.apache.spark.SparkContext
import org.apache.spark.rdd.{JdbcRDD, RDD}

/**
  * Created by jiangxd on 2016/10/10.
  */
object IbssVendorSource {

  /**
    * 获取代理商ID和代理商名称的RDD
    *
    * @param config       配置文件MAP
    * @param sparkContext spark运行环境
    * @return RDD[(vendorID, vendorName)]
    */
  def getIbssVendorRDD(config: java.util.Map[String, String], sparkContext: SparkContext): RDD[(Int, String)] = {

    val mysqlURL = config.get("mysqlURL")
    val mysqlUserName = config.get("mysqlUserName")
    val mysqlPassword = config.get("mysqlPassword")

    val enterpriseContactSql = " SELECT id,name from org_department  WHERE 2 >= ? AND 2 <= ? "
    val ibssOrgDeptRDD = new JdbcRDD(sparkContext, () => {
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      DriverManager.getConnection(mysqlURL, mysqlUserName, mysqlPassword)
    }, enterpriseContactSql, 0, 4, 1, row => {
      val vendorID = row.getInt("id")
      val vendorName = row.getString("name")
      (vendorID, vendorName)
    })
    ibssOrgDeptRDD
  }

}
