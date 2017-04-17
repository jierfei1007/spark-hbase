package com.fxiaoke.fhc.source

import java.sql.DriverManager

import com.fxiaoke.fhc.main.RefreshEnterpriseStaticMain
import org.apache.spark.SparkContext
import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.sql.hive.HiveContext

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

  /**
    * 获取代理商，由于代理商的结构已经变化，所以2017-04-11之前的历史数据处理都用
    * org_department 20170111分区数据
    * @param hiveContext
    * @param runDate
    * @return
    */
  def getOldIbssVendorRDD(hiveContext: HiveContext, runDate: java.util.Date): RDD[(Int, String)] = {
    var default_runDate = RefreshEnterpriseStaticMain.sf3.format(runDate)
    if (runDate.before(RefreshEnterpriseStaticMain.sf.parse("2017-04-11"))) {
      default_runDate = "20170411"
    }
    val sql = s"select id,name from core_data.org_department where dt = '$default_runDate'"
    val depDF = hiveContext.sql(sql)
    val depRDD: RDD[(Int, String)] = depDF.map(row => {
      val vendorID = row.getAs[Int]("id")
      val vendorName = row.getAs[String]("name")
      (vendorID, vendorName)
    })
    depRDD
  }
}
