package com.fxiaoke.fhc.source

import java.text.SimpleDateFormat
import java.util.Date

import com.fxiaoke.fhc.bean.EnterpriseStaticBean
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
  def getYdayEnterpriseStaticRDD(huiJuContext: HuijuContext, enterpriseTableName: String, runDate: Date): RDD[EnterpriseStaticBean] = {
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
        var enterpriseStaticBean = new EnterpriseStaticBean
        enterpriseStaticBean.setSkEnterpriseID(row.getString(0))
        enterpriseStaticBean.setEnterpriseID(row.getInt(1))
        enterpriseStaticBean.setEnterpriseName(row.getString(2))
        enterpriseStaticBean.setEnterpriseShortName(row.getString(3))
        enterpriseStaticBean.setEnterpriseAccount(row.getString(4))
        enterpriseStaticBean.setVendorID(row.getInt(5))
        enterpriseStaticBean.setVendorName(row.getString(6))
        enterpriseStaticBean.setCreatorID(row.getInt(7))
        enterpriseStaticBean.setKeyContactName(row.getString(8))
        enterpriseStaticBean.setKeyContactPhone(row.getString(9))
        enterpriseStaticBean.setKeyContactEmail(row.getString(10))
        enterpriseStaticBean.setContactName(row.getString(11))
        enterpriseStaticBean.setContactPhone(row.getString(12))
        enterpriseStaticBean.setContactEmail(row.getString(13))
        enterpriseStaticBean.setContactIM(row.getString(14))
        enterpriseStaticBean.setEnterpriseAddress(row.getString(15))
        enterpriseStaticBean.setEnterpriseRemark(row.getString(16))
        enterpriseStaticBean.setAccountTotalAmount(row.getInt(17))
        enterpriseStaticBean.setRunStatus(row.getInt(18))
        enterpriseStaticBean.setRunStatusDesc(row.getString(19))
        enterpriseStaticBean.setIndustry1(row.getInt(20))
        enterpriseStaticBean.setIndustry1Value(row.getString(21))
        enterpriseStaticBean.setIndustry2(row.getInt(22))
        enterpriseStaticBean.setIndustry2Value(row.getString(23))
        enterpriseStaticBean.setIndustry3(row.getInt(24))
        enterpriseStaticBean.setIndustry3Value(row.getString(25))
        enterpriseStaticBean.setEnterpriseProvince(row.getInt(26))
        enterpriseStaticBean.setEnterpriseProvinceDesc(row.getString(27))
        enterpriseStaticBean.setCity(row.getString(28))
        enterpriseStaticBean.setEnterpriseSource(row.getInt(29))
        enterpriseStaticBean.setEnterpriseSourceDesc(row.getString(30))
        enterpriseStaticBean.setRegisteTime(row.getString(31))
        enterpriseStaticBean.setAppStartTime(row.getString(32))
        enterpriseStaticBean.setEnterpriseGroup(row.getInt(33))
        enterpriseStaticBean.setEnterpriseGroupDesc(row.getString(34))
        enterpriseStaticBean.setCompanyScale(row.getInt(35))
        enterpriseStaticBean.setCompanyScaleDesc(row.getString(36))
        enterpriseStaticBean.setEnterpriseType(row.getInt(37))
        enterpriseStaticBean.setEnterpriseTypeDesc(row.getString(38))
        enterpriseStaticBean.setSkVersion(row.getInt(39))
        enterpriseStaticBean.setSkBeginDate(row.getString(40))
        enterpriseStaticBean.setSkEndDate(row.getString(41))
        enterpriseStaticBean.setEnterpriseScale(row.getString(42))
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
          var enterpriseStaticBean = new EnterpriseStaticBean
          enterpriseStaticBean.setEnterpriseID(r)
          enterpriseStaticBean
        })
    }
  }
}
