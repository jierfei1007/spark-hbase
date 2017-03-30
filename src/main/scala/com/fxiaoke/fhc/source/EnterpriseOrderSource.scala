package com.fxiaoke.fhc.source

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext
import com.fxiaoke.fhc.bean.Orders
import com.fxiaoke.fhc.bean.Refunds
import java.util.Date

/**
  * Created by jief on 2017/3/22.
  */
object EnterpriseOrderSource {
  /**
    * 获取订单
    * @param hiveContext
    * @return
    */
  def createOrderRDD(hiveContext: HiveContext):RDD[Orders]={
    val sql:String="""select
                     A.eid as eid,
                     A.id as order_id,
                     B.id as sub_order_id,
                     A.total_amount as total_amount,
                     case when A.order_create_time is null then '1970-01-01 00:00:00' else cast(A.order_create_time as string) end as order_create_time,
                     B.product_id as product_id,
                     B.purchase_amount as purchase_amount,
                     case when B.create_time is null then '1970-01-01 00:00:00' else cast(B.create_time as string) end as sub_order_create_time,
                     case when B.product_end_time is null then '1970-01-01 00:00:00' else cast(B.product_end_time as string) end as product_end_time
                     from dw_bds_b.b_pub_hjodr_order A,dw_bds_b.b_pub_hjodr_sub_order B where A.id=B.order_id and A.is_delete=0 and B.is_delete=0 """
    println("exec sql:"+sql)
    val dataFrame:DataFrame=hiveContext.sql(sql)
    val ordersRDD:RDD[Orders]=dataFrame.map(row=>{
      val eid=row.getAs[Int]("eid")
      val order_id=row.getAs[Int]("order_id")
      val sub_order_id=row.getAs[Int]("sub_order_id")
      val total_amount=row.getAs[Double]("total_amount")
      val order_create_time=row.getAs[String]("order_create_time")
      val product_id = row.getAs[Int]("product_id")
      val purchase_amount=row.getAs[Double]("purchase_amount")
      val sub_order_create_time=row.getAs[String]("sub_order_create_time")
      val product_end_time=row.getAs[String]("product_end_time")
      new Orders(eid,order_id,sub_order_id,order_create_time,total_amount,product_id,purchase_amount,sub_order_create_time,product_end_time)
    })
    ordersRDD
  }

  /**
    * 获取退款单
    * @param hiveContext
    * @return
    */
  def createRefundRDD(hiveContext: HiveContext):RDD[Refunds]={
    val sql:String="""select
                     A.eid as eid,
                     A.id as refund_id,
                     B.id as sub_refund_id,
                     A.refundable_amount as refundable_amount,
                     A.order_id as order_id,
                     case when A.create_time is null then '1970-01-01 00:00:00' else cast(A.create_time as string) end as refund_create_time,
                     B.product_id as product_id,
                     B.refundable_total_amount as refundable_total_amount
                     from dw_bds_b.b_pub_hjodr_refund A ,dw_bds_b.b_pub_hjodr_sub_refund B where A.id=B.refund_id and A.is_delete=0 and B.is_delete=0 """
    println("exec sql:"+sql)
    val dataFrame:DataFrame=hiveContext.sql(sql)
    val refundRDD:RDD[Refunds]=dataFrame.map(row=>{
      val eid=row.getAs[Int]("eid")
      val refund_id=row.getAs[Int]("refund_id")
      val sub_refund_id=row.getAs[Int]("sub_refund_id")
      val refundable_amount=row.getAs[Double]("refundable_amount")
      val order_id=row.getAs[Int]("order_id")
      val refund_create_time=row.getAs[String]("refund_create_time")
      val product_id = row.getAs[Int]("product_id")
      val refundable_total_amount = row.getAs[Double]("refundable_total_amount")
      new Refunds(eid,refund_id,sub_refund_id,refundable_amount,order_id,refund_create_time,product_id,refundable_total_amount)
    })
    refundRDD
  }
}
