package com.fxiaoke.fhc.source.xtsendfeed

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by jief on 2017/6/22.
  */
object XTSendFeedSource {

  def createDayDF(hiveContext: HiveContext,runDate:String):DataFrame={

    val sql=
      s"""
        |select
        |eid,
        |p_feed_id,
        |p_feed_type,
        |p_feed_plan_type,
        |p_feed_approve_type,
        |p_approve_flow_type,
        |p_xt_sendfeed_numeric_a,
        |p_xt_sendfeed_numeric_b,
        |p_xt_sendfeed_numeric_c from dw_bds_b.b_xt_sendfeed_detail where dt='$runDate'
      """.stripMargin
    hiveContext.sql(sql)

  }

  def createDayDF(hiveContext: HiveContext,fromDate:String,toDate:String):DataFrame={

    val sql=
      s"""
         |select
         |eid,
         |p_feed_id,
         |p_feed_type,
         |p_feed_plan_type,
         |p_feed_approve_type,
         |p_approve_flow_type,
         |p_xt_sendfeed_numeric_a,
         |p_xt_sendfeed_numeric_b,
         |p_xt_sendfeed_numeric_c,
         |sendfeed_time from dw_bds_b.b_xt_sendfeed_detail where dt between '$fromDate' and '$toDate'
      """.stripMargin
    hiveContext.sql(sql)

  }
}
