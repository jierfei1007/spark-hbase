package com.fxiaoke.fhc.source

import java.text.SimpleDateFormat
import java.util.Date

import com.fxiaoke.fhc.bean.{EnterpriseInfoStatic, EnterpriseStaticBean}
import com.fxiaoke.fhc.context.HuijuContext
import com.fxiaoke.fhc.log.PrintLog
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext
/**
  * Created by jiangxd on 2016/10/11.
  */
object EnterpriseStaticSource {

  /**
    * 获取前一天的企业静态表的数据
    *
    * @param huiJuContext        运行环境
    * @param enterpriseTableName 企业静态表表名
    * @param runDate             运行日期
    * @return RDD【企业静态表】
    */
  def getYdayEnterpriseStaticRDD(huiJuContext: HuijuContext, enterpriseTableName: String, runDate: Date): RDD[EnterpriseInfoStatic] = {
    try {
      val sf = new SimpleDateFormat("yyyyMMdd")
      // TODO: Add enterpriseScale
      val getEnterpriseStaticSql = "select sk_enterprise_id,enterprise_id,enterprise_name,enterprise_short_name,enterprise_account,vendor_id,vendor_name," +
        " creator_id,key_contact_name,key_contact_phone,key_contact_email,contact_name,contact_phone,contact_email,contact_im,enterprise_address," +
        " enterprise_remark,account_total_amount,run_status,run_status_desc,Industry1,Industry1_desc,Industry2,Industry2_desc,Industry3," +
        " Industry3_desc,enterprise_province,enterprise_province_desc,enterprise_city,enterprise_source,enterprise_source_desc,registe_time,app_start_time," +
        " enterprise_group,enterprise_group_desc,company_scale,company_scale_desc,enterprise_type,enterprise_type_desc,sk_version,sk_begin_date,sk_end_date,enterprise_scale " +
        " from " + enterpriseTableName + " where dt=" + sf.format(runDate)
      PrintLog.log("  getEnterpriseStaticSql : " + getEnterpriseStaticSql)
      val enterpriseStaticDF = huiJuContext.hiveContext.sql(getEnterpriseStaticSql)
      val enterpriseStaticRDD = enterpriseStaticDF.map(row => {
        val enterpriseStaticBean = new EnterpriseInfoStatic
        enterpriseStaticBean.setSk_enterprise_id(row.getString(0))
        enterpriseStaticBean.setEnterprise_id(row.getInt(1))
        enterpriseStaticBean.setEnterprise_name(row.getString(2))
        enterpriseStaticBean.setEnterprise_short_name(row.getString(3))
        enterpriseStaticBean.setEnterprise_account(row.getString(4))
        enterpriseStaticBean.setVendor_id(row.getInt(5))
        enterpriseStaticBean.setVendor_name(row.getString(6))
        enterpriseStaticBean.setCreator_id(row.getInt(7))
        enterpriseStaticBean.setKey_contact_name(row.getString(8))
        enterpriseStaticBean.setKey_contact_phone(row.getString(9))
        enterpriseStaticBean.setKey_contact_email(row.getString(10))
        enterpriseStaticBean.setContact_name(row.getString(11))
        enterpriseStaticBean.setContact_phone(row.getString(12))
        enterpriseStaticBean.setContact_email(row.getString(13))
        enterpriseStaticBean.setContact_im(row.getString(14))
        enterpriseStaticBean.setEnterprise_address(row.getString(15))
        enterpriseStaticBean.setEnterprise_remark(row.getString(16))
        enterpriseStaticBean.setAccount_total_amount(row.getInt(17))
        enterpriseStaticBean.setRun_status(row.getInt(18))
        enterpriseStaticBean.setRun_status_desc(row.getString(19))
        enterpriseStaticBean.setIndustry1(row.getInt(20))
        enterpriseStaticBean.setIndustry1_desc(row.getString(21))
        enterpriseStaticBean.setIndustry2(row.getInt(22))
        enterpriseStaticBean.setIndustry2_desc(row.getString(23))
        enterpriseStaticBean.setIndustry3(row.getInt(24))
        enterpriseStaticBean.setIndustry3_desc(row.getString(25))
        enterpriseStaticBean.setEnterprise_province(row.getInt(26))
        enterpriseStaticBean.setEnterprise_province_desc(row.getString(27))
        enterpriseStaticBean.setEnterprise_city(row.getString(28))
        enterpriseStaticBean.setEnterprise_source(row.getInt(29))
        enterpriseStaticBean.setEnterprise_source_desc(row.getString(30))
        enterpriseStaticBean.setRegiste_time(row.getString(31))
        enterpriseStaticBean.setApp_start_time(row.getString(32))
        enterpriseStaticBean.setEnterprise_group(row.getInt(33))
        enterpriseStaticBean.setEnterprise_group_desc(row.getString(34))
        enterpriseStaticBean.setCompany_scale(row.getInt(35))
        enterpriseStaticBean.setCompany_scale_desc(row.getString(36))
        enterpriseStaticBean.setEnterprise_type(row.getInt(37))
        enterpriseStaticBean.setEnterprise_type_desc(row.getString(38))
        enterpriseStaticBean.setSk_version(row.getInt(39))
        enterpriseStaticBean.setSk_begin_date(row.getString(40))
        enterpriseStaticBean.setSk_end_date(row.getString(41))
        enterpriseStaticBean.setEnterprise_scale(row.getString(42))
        //返回enterpriseStaticBean
        enterpriseStaticBean
      }).filter(enterpriseStaticBean => enterpriseStaticBean != null)
      enterpriseStaticRDD
    } catch {
      /**
        * 如果昨日没有企业静态表数据 , 那么就是初始状态
        */
      case e: Exception => PrintLog.log(" getYdayEnterpriseRDD is error : " + e)
        huiJuContext.sparkContext.parallelize(List(-1)).map(r => {
          val enterpriseStaticBean = new EnterpriseInfoStatic
          enterpriseStaticBean.setEnterprise_id(r)
          enterpriseStaticBean
        })
    }
  }
}
