package com.fxiaoke.fhc.source

import java.sql.DriverManager

import org.apache.spark.SparkContext
import org.apache.spark.rdd.{JdbcRDD, RDD}

/**
  * Created by jiangxd on 2016/10/10.
  */
object IbssEnterpriseSource {

  /**
    * 从运营平台IBSS库里面获取到企业的一些基本信息
    *
    * @param config       配置中心配置Map
    * @param sparkContext Spark运行环境
    * @return 所需要的IBSS企业基本信息
    */
  def getIbssEnterpriseRDD(config: java.util.Map[String, String], sparkContext: SparkContext): RDD[(Int, String, String, String, String, String, String)] = {

    val mysqlURL = config.get("mysqlURL")
    val mysqlUserName = config.get("mysqlUserName")
    val mysqlPassword = config.get("mysqlPassword")

    val enterpriseContactSql = " SELECT EnterpriseID,KeyContactName,KeyContactPhone,KeyContactEmail,ContactName,ContactPhone,ContactEmail from Enterprise  WHERE 2 >= ? AND 2 <= ? "
    val ibssEnterpriseRDD = new JdbcRDD(sparkContext, () => {
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      DriverManager.getConnection(mysqlURL, mysqlUserName, mysqlPassword)
    }, enterpriseContactSql, 0, 4, 1, row => {
      val enterprise_id = row.getInt("EnterpriseID")
      val enterprise_key_contact_name = row.getString("KeyContactName")
      val enterprise_key_contact_phone = row.getString("KeyContactPhone")
      val enterprise_key_contact_email = row.getString("KeyContactEmail")
      val enterprise_contact_name = row.getString("ContactName")
      val enterprise_contact_phone = row.getString("ContactPhone")
      val enterprise_contact_email = row.getString("ContactEmail")
      (enterprise_id, enterprise_key_contact_name, enterprise_key_contact_phone, enterprise_key_contact_email, enterprise_contact_name, enterprise_contact_phone, enterprise_contact_email)
    })

    ibssEnterpriseRDD
  }

}
