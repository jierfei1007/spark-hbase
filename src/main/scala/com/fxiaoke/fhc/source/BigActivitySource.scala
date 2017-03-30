package com.fxiaoke.fhc.source

import java.text.SimpleDateFormat

import com.fxiaoke.fhc.context.HuijuContext
import com.fxiaoke.fhc.log.PrintLog
import org.apache.spark.sql.DataFrame

/**
  * Created by jiangxd on 2016/10/19.
  */
object BigActivitySource {

  val sf3 = new SimpleDateFormat("yyyyMMdd")

  def getBigActivityBaseInfoDF(huiJuContext: HuijuContext,
                               bigActivityTableName: String,
                               bomDateStr: String,
                               runDate: java.util.Date,
                               betweenDays: Int): DataFrame = {
    /**
      * 登录率+企业是否达标+功能使用项
      */
    val getBaseInfoSQL = "select enterprise_id ," +
      " sum(case when enterprise_activity is null then 0 else enterprise_activity  end) as enterprise_activity ," +
      " sum(case when online_user_num is null then 0 else online_user_num end) / " + betweenDays + " as online_user_num, " +
      " sum((case when online_user_num  is null then 0 else online_user_num end)/(case when enterpriseaccountnumber  is null then 0 else enterpriseaccountnumber end) ) / " + betweenDays + " as login_rate, " +
      " sum(case when feed_share is null then 0 else feed_share end) as feed_share , " +
      " sum(case when feed_log is null then 0 else feed_log end) as feed_log , " +
      " sum(case when feed_crm_server is null then 0 else feed_crm_server end) as feed_crm_server , " +
      " sum(case when feed_crm_sale is null then 0 else feed_crm_sale end) as feed_crm_sale , " +
      " sum(case when feed_order is null then 0 else feed_order end) as feed_order , " +
      " sum(case when feed_waiqin is null then 0 else feed_waiqin end) as feed_waiqin , " +
      " sum(case when feed_approve is null then 0 else feed_approve end) as feed_approve , " +
      " sum(case when feed_schedule is null then 0 else feed_schedule end) as feed_schedule , " +
      " sum(case when feed_notice is null then 0 else feed_notice end) as feed_notice , " +
      " sum(case when feed_task is null then 0 else feed_task end) as feed_task , " +
      " sum(case when crm_new_customer is null then 0 else crm_new_customer end) as crm_new_customer , " +
      " sum(case when crm_new_contacts is null then 0 else crm_new_contacts end) as crm_new_contacts , " +
      " sum(case when crm_new_business is null then 0 else crm_new_business end) as crm_new_business , " +
      " sum(case when crm_new_sale_order is null then 0 else crm_new_sale_order end) as crm_new_sale_order , " +
      " sum(case when crm_new_payment is null then 0 else crm_new_payment end) as crm_new_payment , " +
      " sum(case when crm_new_refund is null then 0 else crm_new_refund end) as crm_new_refund , " +
      " sum(case when crm_new_return_order is null then 0 else crm_new_return_order end) as crm_new_return_order , " +
      " sum(case when crm_new_clue is null then 0 else crm_new_clue end) as crm_new_clue , " +
      " sum(case when crm_new_clue_pool is null then 0 else crm_new_clue_pool end) as crm_new_clue_pool , " +
      " sum(case when crm_new_high_seas is null then 0 else crm_new_high_seas end) as crm_new_high_seas , " +
      " sum(case when crm_new_visit is null then 0 else crm_new_visit end) as crm_new_visit , " +
      " sum(case when crm_new_check is null then 0 else crm_new_check end) as crm_new_check , " +
      " sum(case when crm_new_role is null then 0 else crm_new_role end) as crm_new_role , " +
      " sum(case when crm_new_form is null then 0 else crm_new_form end) as crm_new_form , " +
      " sum(case when crm_new_contract is null then 0 else crm_new_contract end) as crm_new_contract , " +
      " sum(case when crm_new_market_activitie is null then 0 else crm_new_market_activitie end) as crm_new_market_activitie , " +
      " sum(case when crm_new_competitor is null then 0 else crm_new_competitor end) as crm_new_competitor , " +
      " sum(case when crm_new_product is null then 0 else crm_new_product end) as crm_new_product , " +
      " sum(case when crm_new_scan_card_count is null then 0 else crm_new_scan_card_count end) as crm_new_scan_card_count , " +
      " sum(case when server_new_server_num is null then 0 else server_new_server_num end) as server_new_server_num , " +
      " sum(case when server_push_message is null then 0 else server_push_message end) as server_push_message , " +
      " sum(case when assistant_kaoqin_success is null then 0 else assistant_kaoqin_success end) as assistant_kaoqin_success , " +
      " sum(case when assistant_new_project is null then 0 else assistant_new_project end) as assistant_new_project , " +
      " sum(case when assistant_new_battlefield is null then 0 else assistant_new_battlefield end) as assistant_new_battlefield , " +
      " sum(case when assistant_new_meetting is null then 0 else assistant_new_meetting end) as assistant_new_meetting , " +
      " sum(case when assistant_new_pk is null then 0 else assistant_new_pk end) as assistant_new_pk , " +
      " sum(case when assistant_total_payroll is null then 0 else assistant_total_payroll end) as assistant_total_payroll , " +
      " sum(case when assistant_course_number is null then 0 else assistant_course_number end) as assistant_course_number  " +
      " from " + bigActivityTableName + " where day>='" + bomDateStr + "' and day<='" + sf3.format(runDate) + "' " +
      " group by enterprise_id"

    PrintLog.log(" getBaseInfoSQL : " + getBaseInfoSQL)

    val baseInfoDF = huiJuContext.hiveContext.sql(getBaseInfoSQL)
    baseInfoDF
  }

  def getBigActivityEnterpriseTypeDF(huiJuContext: HuijuContext,
                                     bigActivityTableName: String,
                                     bomDateStr: String,
                                     runDate: java.util.Date): DataFrame = {

    val getEnterpriseTypeSQL = " select b.enterprise_id,a.enterprisetype  from  " + bigActivityTableName + " a " +
      " left join (select max(day) as day , enterprise_id from " + bigActivityTableName + " where day>='" + bomDateStr + "'  and day<='" + sf3.format(runDate) + "' group by enterprise_id) b  " +
      " on a.day = b.day and a.enterprise_id=b.enterprise_id  where b.enterprise_id is not null"

    PrintLog.log("getEnterpriseTypeSQL: " + getEnterpriseTypeSQL)
    val enterpriseTypeDF = huiJuContext.hiveContext.sql(getEnterpriseTypeSQL)
    enterpriseTypeDF
  }


}
