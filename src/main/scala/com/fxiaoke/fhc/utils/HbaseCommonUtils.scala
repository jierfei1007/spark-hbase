package com.fxiaoke.fhc.utils

import java.util

import com.fxiaoke.fhc.bean.{Orders, Refunds}
import org.apache.commons.lang.StringUtils
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Result, Table}
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.filter._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.Scan
import scala.collection.mutable.ListBuffer

/**
  * Created by jief on 2017/3/22.
  */
object HbaseCommonUtils {
  /**
    * 获取hbase表连接
    * @return
    */
  def getHbaseTable(propConfig: java.util.Map[String, String],HTableName:String,HFamilys:Array[String]):(Connection, Table) = {
    require(StringUtils.isNotEmpty(HTableName),"hbase table name is empty!")
    require(HFamilys!=null && HFamilys.length>0,"hbase table family can not empty!")
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", propConfig.get("hbase.zk.clientPort"))
    conf.set("hbase.zookeeper.quorum", propConfig.get("hbase.zk.quorum"))
    //Connection 的创建是个重量级的工作，线程安全，是操作hbase的入口
    val conn = ConnectionFactory.createConnection(conf)
    //从Connection获得 Admin
    val admin = conn.getAdmin
    //本例将操作的表名
    val tableName = TableName.valueOf(HTableName)
    if (!admin.tableExists(tableName)){
      val tableDescriptor= new HTableDescriptor(tableName)
      HFamilys.filter(f=>StringUtils.isNotEmpty(f)).foreach(family=>{
        tableDescriptor.addFamily(new HColumnDescriptor(family.getBytes))
      })
      admin.createTable(tableDescriptor)
    }
    val table = conn.getTable(tableName)
    (conn, table)
  }

  /**
    * 获取hbase connection
    * @param propConfig
    * @return
    */
  def getHbaseConnection(propConfig: java.util.Map[String, String]):Connection={
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", propConfig.get("hbase.zk.clientPort"))
    conf.set("hbase.zookeeper.quorum", propConfig.get("hbase.zk.quorum"))
    //Connection 的创建是个重量级的工作，线程安全，是操作hbase的入口
    val conn = ConnectionFactory.createConnection(conf)
    conn
  }
  /**
    * 关闭hbase链接
    * @param conn
    * @param table
    */
  def closeHbaseTable(conn: Connection, table: Table) = {

    if (table != null) {
      table.close()
    }
    if (conn != null) {
      conn.close()
    }
  }

  /**
    *
    * @param table table对象
    * @param eid 企业id
    * @param createDate 订单创建时间
    * @return
    */
  def scanEnterpriseOrdersByCreateDate(table:Table,eid:Int,createDate:String):Option[ListBuffer[Orders]]={
    require(eid!=null,"eid is not null")
    require(StringUtils.isNotEmpty(createDate),"order create date is not empty!")
    val orders = ListBuffer[Orders]()
    val filters = new util.ArrayList[org.apache.hadoop.hbase.filter.Filter]()
    try {
      val scan = new org.apache.hadoop.hbase.client.Scan()
      //row key filter
      val rowFilter: Filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(eid + "-.*"))
      filters.add(rowFilter)
      //single column filter
      val singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes("order"), Bytes.toBytes("subOrderCreateTime"), CompareOp.LESS_OR_EQUAL, Bytes.toBytes(createDate))
      filters.add(singleColumnValueFilter)
      val filterList = new FilterList(filters)
      scan.setFilter(filterList)
      val resultScanner = table.getScanner(scan)
      val family_bytes = "order".getBytes
      val orderCreateTime_bytes = "orderCreateTime".getBytes
      val totalAmount_bytes = "totalAmount".getBytes
      val productId_bytes = "productId".getBytes
      val purchaseAmount_bytes = "purchaseAmount".getBytes
      val subOrderCreateTime_bytes = "subOrderCreateTime".getBytes
      val productEndTime_bytes = "productEndTime".getBytes
      val resultIterator = resultScanner.iterator()
      while (resultIterator.hasNext) {
        val result: Result = resultIterator.next()
        val order = new Orders()
        val cell1 = result.getColumnLatestCell(family_bytes, orderCreateTime_bytes)
        val cell2 = result.getColumnLatestCell(family_bytes, totalAmount_bytes)
        val cell3 = result.getColumnLatestCell(family_bytes, productId_bytes)
        val cell4 = result.getColumnLatestCell(family_bytes, purchaseAmount_bytes)
        val cell5 = result.getColumnLatestCell(family_bytes, subOrderCreateTime_bytes)
        val cell6 = result.getColumnLatestCell(family_bytes, productEndTime_bytes)
        order.setOrderCreateTime(Bytes.toString(cell1.getValueArray))
        order.setTotalAmount(Bytes.toDouble(cell2.getValueArray))
        order.setProductId(Bytes.toInt(cell3.getValueArray))
        order.setPurchaseAmount(Bytes.toDouble(cell4.getValueArray))
        order.setSubOrderCreateTime(Bytes.toString(cell5.getValueArray))
        order.setProductEndTime(Bytes.toString(cell6.getValueArray))
        orders += order
      }
      Some[ListBuffer[Orders]](orders)
    }catch{
      case e =>{
        println("get orders by enterprise id: "+eid+" error "+e.getMessage)
        None
      }
    }
  }

  /**
    *
    * @param eid
    * @param table
    * @return
    */
  def scanEnterpriseOrderRefund(eid:Int,table:Table):Option[ListBuffer[Refunds]]={
    require(eid!=null,"eid is null")
    val refundsList = ListBuffer[Refunds]()
    try{
      val scan = new Scan()
      val rowFilter: Filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(eid + "-.*"))
      scan.setFilter(rowFilter)
      val resultScanner = table.getScanner(scan)
      val family_bytes = "refund".getBytes
      val refundableAmount = "refundableAmount".getBytes
      val orderId = "orderId".getBytes
      val refundCreateTime = "refundCreateTime".getBytes
      val productId = "productId".getBytes
      val subRefundableTotalAmount = "subRefundableTotalAmount".getBytes
      val resultIterator = resultScanner.iterator()
      while (resultIterator.hasNext) {
        val result: Result = resultIterator.next()
        val refunds=new Refunds()
        val cell1 = result.getColumnLatestCell(family_bytes, refundableAmount)
        val cell2 = result.getColumnLatestCell(family_bytes, orderId)
        val cell3 = result.getColumnLatestCell(family_bytes, refundCreateTime)
        val cell4 = result.getColumnLatestCell(family_bytes, productId)
        val cell5 = result.getColumnLatestCell(family_bytes, subRefundableTotalAmount)
        refunds.setRefundableAmount(Bytes.toDouble(cell1.getValueArray))
        refunds.setOrderId(Bytes.toInt(cell2.getValueArray))
        refunds.setRefundCreateTime(Bytes.toString(cell3.getValueArray))
        refunds.setProductId(Bytes.toInt(cell4.getValueArray))
        refunds.setSubRefundableTotalAmount(Bytes.toDouble(cell5.getValueArray))
        refundsList+=refunds
      }
      Some(refundsList)
    }catch {
      case e =>{
        println("scan enterprise:"+eid+ " refunds error! "+e.getMessage)
        None
      }
    }
  }

}
