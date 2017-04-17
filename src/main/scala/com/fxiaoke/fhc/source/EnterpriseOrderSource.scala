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
                     case when B.create_time is null and A.order_create_time is not null then cast(A.order_create_time as string)
                          when B.create_time is null and A.order_create_time is null then '1970-01-01 00:00:00'
                          else cast(B.create_time as string) end as sub_order_create_time,
                     case when B.product_end_time is null then '1970-01-01 00:00:00' else cast(B.product_end_time as string) end as product_end_time
                     from dw_bds_b.b_pub_hjodr_order A,dw_bds_b.b_pub_hjodr_sub_order B where A.id=B.order_id and A.is_delete=0 and B.is_delete=0 """
    println("exec sql:"+sql)
    val dataFrame:DataFrame=hiveContext.sql(sql)
    val ordersRDD:RDD[Orders]=dataFrame.map(row=>{
      val eid=row.getAs[Int]("eid")
      val order_id=row.getAs[Int]("order_id")
      val sub_order_id=row.getAs[Int]("sub_order_id")
      val total_amount=row.getAs[java.math.BigDecimal]("total_amount")  //整个订单的金额
      val order_create_time=row.getAs[String]("order_create_time")
      val product_id = row.getAs[Int]("product_id")
      val purchase_amount=row.getAs[java.math.BigDecimal]("purchase_amount") //购买金额真实的产品金额
      val sub_order_create_time=row.getAs[String]("sub_order_create_time")
      val product_end_time=row.getAs[String]("product_end_time")
      new Orders(eid,order_id,sub_order_id,order_create_time,total_amount.doubleValue(),product_id,purchase_amount.doubleValue(),sub_order_create_time,product_end_time)
    })
    ordersRDD
  }

  /**
    * 获取所有购买订单的企业id
    * @param hiveContext
    * @return
    */
  def createEidRDD(hiveContext: HiveContext,runDate:String):RDD[Int]={
    val sql:String ="select "+
      " distinct A.eid as eid "+
      " from dw_bds_b.b_pub_hjodr_order A where  A.is_delete=0 and A.order_create_time <= '"+runDate+" 23:59:59.0'"
    println("exec:"+sql)
    val df=hiveContext.sql(sql)
    val rdd=df.map(row=>{
        row.getAs[Int]("eid")
    })
    rdd
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
            A.refund_amount as refund_amount,
            A.order_id as order_id,
            case when A.create_time is null then '1970-01-01 00:00:00' else cast(A.create_time as string) end as refund_create_time,
            B.product_id as product_id,
            B.refund_amount as 	sub_refund_amount
            from (
                select C.* from dw_bds_b.b_pub_hjodr_refund C,
                (select distinct order_id,`key`,`value` from dw_bds_b.b_pub_hjodr_order_extend where `key`='approve_status' and `value`='6')D
                where C.is_delete=0 and C.order_id=D.order_id) A ,dw_bds_b.b_pub_hjodr_sub_refund B
            where A.id=B.refund_id and B.is_delete=0 """
    println("exec sql:"+sql)
    val dataFrame:DataFrame=hiveContext.sql(sql)
    val refundRDD:RDD[Refunds]=dataFrame.map(row=>{
      val eid=row.getAs[Int]("eid")
      val refund_id=row.getAs[Int]("refund_id")
      val sub_refund_id=row.getAs[Int]("sub_refund_id")
      val refund_amount=row.getAs[java.math.BigDecimal]("refund_amount") //申请退款总金额（真实的退款金额）
      val order_id=row.getAs[Int]("order_id")
      val refund_create_time=row.getAs[String]("refund_create_time")
      val product_id = row.getAs[Int]("product_id")
      val sub_refund_amount = row.getAs[java.math.BigDecimal]("sub_refund_amount") //申请产品退款金额
      new Refunds(eid,refund_id,sub_refund_id,refund_amount.doubleValue(),order_id,refund_create_time,product_id,sub_refund_amount.doubleValue())
    })
    refundRDD
  }
}
