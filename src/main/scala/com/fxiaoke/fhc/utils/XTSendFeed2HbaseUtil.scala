package com.fxiaoke.fhc.utils

import java.text.DecimalFormat
import java.util
import java.util.Calendar

import org.apache.spark.sql.Row
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by jief on 2017/6/22.
  */
object XTSendFeed2HbaseUtil {
  val decimalFormat = new DecimalFormat("00")
  /**
    * 插入Feed
    *
    * @param hbaseBatchSize 批量size
    * @param propConfig     配置
    * @return
    */
  def insertFeedTypes(hbaseBatchSize: Int = 2000, propConfig: java.util.Map[String, String]) = (iterator: Iterator[Row]) => {
    val hbaseConnAndTable = HbaseCommonUtils.getHbaseTable(propConfig, "SENDFEED_TYPE", Array("types"))
    val family_bytes = Bytes.toBytes("types")
    val p_feed_type = "p_feed_type".getBytes
    val p_feed_plan_type = "p_feed_plan_type".getBytes
    val p_feed_approve_type = "p_feed_approve_type".getBytes
    val p_approve_flow_type = "p_approve_flow_type".getBytes
    val p_xt_sendfeed_numeric_a="p_xt_sendfeed_numeric_a".getBytes
    val p_xt_sendfeed_numeric_b="p_xt_sendfeed_numeric_b".getBytes
    val p_xt_sendfeed_numeric_c="p_xt_sendfeed_numeric_c".getBytes
    val sendfeed_time = "sendfeed_time".getBytes
    val putList = new util.ArrayList[Put]()
    var size = 0
    try {
      for (row <- iterator) {
        var eid:Int=0
        var p_feed_id:Long=0
        if(!row.isNullAt(0)){
          eid=row.getInt(0)
        }
        if(!row.isNullAt(1)){
          p_feed_id=row.getLong(1)
        }
        val row_key =eid + "-" + p_feed_id
        val p = new Put(row_key.getBytes)
        if(!row.isNullAt(2)){
          p.addColumn(family_bytes, p_feed_type, Bytes.toBytes(row.getInt(2)))
        }
        if(!row.isNullAt(3)){
          p.addColumn(family_bytes, p_feed_plan_type, Bytes.toBytes(row.getInt(3)))
        }
        if(!row.isNullAt(4)){
          p.addColumn(family_bytes, p_feed_approve_type, Bytes.toBytes(row.getInt(4)))
        }
        if(!row.isNullAt(5)){
          p.addColumn(family_bytes, p_approve_flow_type, Bytes.toBytes(row.getInt(5)))
        }
        if(!row.isNullAt(6)){
          p.addColumn(family_bytes, p_xt_sendfeed_numeric_a, Bytes.toBytes(row.getInt(6)))
        }
        if(!row.isNullAt(7)){
          p.addColumn(family_bytes, p_xt_sendfeed_numeric_b, Bytes.toBytes(row.getInt(7)))
        }
        if(!row.isNullAt(8)){
          p.addColumn(family_bytes, p_xt_sendfeed_numeric_c, Bytes.toBytes(row.getInt(8)))
        }
        if(!row.isNullAt(9)){
          val sft=row.getTimestamp(9)
          val calendar=Calendar.getInstance()
          calendar.setTimeInMillis(sft.getTime)
          val year = calendar.get(Calendar.YEAR)
          //设置月份;月份需要+1
          val month = decimalFormat.format(calendar.get(Calendar.MONTH) + 1)
          //设置日
          val day = decimalFormat.format(calendar.get(Calendar.DATE))
          p.addColumn(family_bytes,sendfeed_time,Bytes.toBytes(year+""+month+""+day))
        }
        putList.add(p)
        size = size + 1
        //提交
        if (size > hbaseBatchSize) {
          hbaseConnAndTable._2.put(putList)
          putList.clear()
          size = 0
        }
      }
      if (size > 0) {
        hbaseConnAndTable._2.put(putList)
      }
    } catch {
      case e:Throwable => {
        println("insert refund into hbase table:'ENTERPRISE_REFUNDS' error" + e.getMessage)
        throw new RuntimeException("insert refund into hbase table:'ENTERPRISE_REFUNDS' error" + e.getMessage)
      }
    } finally {
      HbaseCommonUtils.closeHbaseTable(hbaseConnAndTable._1, hbaseConnAndTable._2)
    }
  }
}
