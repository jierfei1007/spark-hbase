package com.fxiaoke.fhc.test;

import com.fxiaoke.fhc.bean.Orders
import junit.framework.TestCase

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by jief on 2017/3/31.
 */
class TestStatic extends TestCase{

 def testorders(): Unit ={
    val orders:ListBuffer[Orders]= new ListBuffer()
    val orderIdSet = mutable.HashSet[(Int,Int)]()
    orders.filter(order => {
      order != null
    }).foreach(order => {
      orderIdSet.add((order.getOrderId,order.getProductId))
   })
 }

  def testOption(): Unit ={

  }
}
