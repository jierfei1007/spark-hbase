package com.fxiaoke.fhc.utils

import java.util

import org.apache.commons.lang.StringUtils
import org.apache.hadoop.hbase.client.Put
import org.joda.time.DateTime
import com.fxiaoke.fhc.bean.Orders
import com.fxiaoke.fhc.bean.Refunds
import com.fxiaoke.fhc.source.EnterpriseOrderSource
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 将订单数据导入到hbase以便
  * 计算历史企业付费类型
  * Created by jief on 2017/3/21.
  */
object ImportOrder2Hbase {

  val date_format: String = "yyyy-MM-dd HH:mm:ss"

  def main(args: Array[String]): Unit = {
    val waringMsg = "spark-submit XXX.jar  " +
      " runEnvironment(SDE,foneshare,preview) \n" +
      " runMode(local,yarn-cluster) \n" +
      " configName \n"
    if (args.length != 3) {
      throw new RuntimeException("args numbers error! " + waringMsg)
    }
    //获取运行参数
    val runEnvironment = args(0)
    val runMode = args(1)
    val configName = args(2)

    require(StringUtils.isNotEmpty(runEnvironment), "runEnvironment is empty!")
    require(StringUtils.isNotEmpty(runMode), "runMode is empty!")
    require(StringUtils.isNotEmpty(configName), "configName is empty!")
    val propConfig = Config.getConfig(configName, runEnvironment)
    val hbaseBatchSize = propConfig.get("hbase.batch").toInt
    val sparkConf = new SparkConf().setAppName("订单信息入hbase").setMaster(runMode)
    val sparkContext = new SparkContext(sparkConf)
    val hiveContext: HiveContext = new HiveContext(sparkContext)
//    val orderRDD = EnterpriseOrderSource.createOrderRDD(hiveContext)
//    val inserOrderFun = insertOrders(hbaseBatchSize, propConfig)
//    orderRDD.coalesce(3).foreachPartition(inserOrderFun)
    val refundRDD = EnterpriseOrderSource.createRefundRDD(hiveContext)
    val insertRefundFun = insertRefunds(hbaseBatchSize, propConfig)
    refundRDD.coalesce(1).foreachPartition(insertRefundFun)
    sparkContext.stop()
  }

  /**
    * 插入订单
    *
    * @param hbaseBatchSize 批处理size
    * @param propConfig     配置
    * @return
    */
  def insertOrders(hbaseBatchSize: Int = 2000, propConfig: java.util.Map[String, String]) = (iterator: Iterator[Orders]) => {
    val hbaseConnAndTable = HbaseCommonUtils.getHbaseTable(propConfig, "ENTERPRISE_ORDERS", Array("order"))
    val family_bytes = "order".getBytes
    val orderCreateTime_bytes = "orderCreateTime".getBytes
    val totalAmount_bytes = "totalAmount".getBytes
    val productId_bytes = "productId".getBytes
    val purchaseAmount_bytes = "purchaseAmount".getBytes
    val subOrderCreateTime_bytes = "subOrderCreateTime".getBytes
    val productEndTime_bytes = "productEndTime".getBytes
    val putList = new util.ArrayList[Put]()
    var size = 0
    try {
      for (row <- iterator) {
        val row_key = row.getEid + "-" + row.getOrderId + "-" + row.getSubOrderId
        val p = new Put(row_key.getBytes)
        p.addColumn(family_bytes, orderCreateTime_bytes, Bytes.toBytes(row.getOrderCreateTime()))
        p.addColumn(family_bytes, totalAmount_bytes, Bytes.toBytes(row.getTotalAmount))
        p.addColumn(family_bytes, productId_bytes, Bytes.toBytes(row.getProductId))
        p.addColumn(family_bytes, purchaseAmount_bytes, Bytes.toBytes(row.getPurchaseAmount))
        p.addColumn(family_bytes, subOrderCreateTime_bytes, Bytes.toBytes(row.getSubOrderCreateTime))
        p.addColumn(family_bytes, productEndTime_bytes, Bytes.toBytes(row.getProductEndTime))
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
      case e => {
        println("insert orders into hbase table:'ENTERPRISE_ORDERS' error" + e.getMessage)
      }
    } finally {
      HbaseCommonUtils.closeHbaseTable(hbaseConnAndTable._1, hbaseConnAndTable._2)
    }
  }

  /**
    * 插入退款单
    *
    * @param hbaseBatchSize 批量size
    * @param propConfig     配置
    * @return
    */
  def insertRefunds(hbaseBatchSize: Int = 2000, propConfig: java.util.Map[String, String]) = (iterator: Iterator[Refunds]) => {
    val hbaseConnAndTable = HbaseCommonUtils.getHbaseTable(propConfig, "ENTERPRISE_REFUNDS", Array("refund"))
    val family_bytes = "refund".getBytes
    val refundableAmount = "refundableAmount".getBytes
    val orderId = "orderId".getBytes
    val refundCreateTime = "refundCreateTime".getBytes
    val productId = "productId".getBytes
    val subRefundableTotalAmount = "subRefundableTotalAmount".getBytes
    val putList = new util.ArrayList[Put]()
    var size = 0
    try {
      for (row <- iterator) {
        val row_key = row.getEid + "-" + row.getRefundId + "-" + row.getSubRefundId
        val p = new Put(row_key.getBytes)
        p.addColumn(family_bytes, refundableAmount, Bytes.toBytes(row.getRefundableAmount))
        p.addColumn(family_bytes, orderId, Bytes.toBytes(row.getOrderId))
        p.addColumn(family_bytes, refundCreateTime, Bytes.toBytes(row.getRefundCreateTime))
        p.addColumn(family_bytes, productId, Bytes.toBytes(row.getProductId))
        p.addColumn(family_bytes, subRefundableTotalAmount, Bytes.toBytes(row.getSubRefundableTotalAmount))
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
      case e => {
        println("insert refund into hbase table:'ENTERPRISE_REFUNDS' error" + e.getMessage)
      }
    } finally {
      HbaseCommonUtils.closeHbaseTable(hbaseConnAndTable._1, hbaseConnAndTable._2)
    }
  }
}
