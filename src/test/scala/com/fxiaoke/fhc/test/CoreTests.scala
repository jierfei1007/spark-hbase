package com.fxiaoke.fhc.test

import com.fxiaoke.fhc.bean.Orders
import junit.framework.TestCase
import org.apache.hadoop.hbase.filter.BinaryComparator
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by jief on 2017/3/23.
  */
class CoreTests extends TestCase{

  def test1(): Unit ={
    println("sshsjs")
  }

  def test2(): Unit ={
    println(new BinaryComparator(Bytes.toBytes("2017-03-27 13:53:21")).compareTo(Bytes.toBytes("2018-03-26 13:53:22")))
//    Bytes.toBytes("2017-03-26 13:53:23")
  }

  def testBytesComparator(): Unit ={
    println(new BinaryComparator(Bytes.toBytes(1.2d)).compareTo(Bytes.toBytes(1.2d)))
  }

  def testCollection(): Unit ={
    val list=ListBuffer[String]()
      list+="erfei"
      list+="hhhh"
    list.foreach(println(_))
  }

  def testOption(): Unit ={
    def a(name:String):Option[String]={
      if(name.equalsIgnoreCase("jief")){
        Some("nihao")
      }else{
        None
      }
    }
    val result=a("jief")
    println(result.isDefined)
  }

  def testList(): Unit ={
    val list=List[String]()
    list.::("jief").foreach(println(_))
    list.::("hhhh").foreach(println(_))
    list.foreach(println(_))
  }

  def testMap(): Unit ={
    val orderIdMap = mutable.HashMap[(Int,Int),String]()
    orderIdMap.put((1,1),"1")
    orderIdMap.put((2,2),"2")
    orderIdMap.put((3,3),"3")
    orderIdMap.put((4,4),"4")
    orderIdMap.put((5,5),"5")
    orderIdMap.put((5,5),"5")

    class A(k:Int,v:Int,name:String){
      def getk(): Int ={
        k
      }
      def getv():Int={
        v
      }
      def getName():String={
        name
      }
    }
    val objList=ListBuffer[A]()
    objList.+=(new A(4,4,"4"))
    objList.+=(new A(5,5,"5"))
    objList.+=(new A(6,6,"6"))

    objList.filter(a=>{
      a!=null && orderIdMap.contains((a.getk,a.getv))
    }).foreach(a=>{
        if(orderIdMap.get((a.getk,a.getv)).get.equals(a.getName)){
          orderIdMap.remove((a.getk,a.getv))
        }
    })
    orderIdMap.foreach(kv=>{
      println(kv._1+":"+kv._2)
    })
    import scala.util.control.Breaks._
  }

  def testClass(): Unit ={

    class B(a:Int){
//      var aa=a
      def geta():Int={
        a
      }
    }
    val b= new B(1)
    println(b.geta())
  }

  def teststring(): Unit ={
    val a="2017-03-28 12:12:12"
    val b="2017-03-28 11:12:13"
    println(b > a)
  }

  def testArray(): Unit ={
    val array=Array(1,2,3,4,5,6,4)
    println(array(2))
  }

  def testNull(): Unit ={
    val a=null
    println(a.asInstanceOf[AnyRef])
  }

  def testList2(): Unit ={
    val a=List(1,2,3,4)
    val b=a .::(5)
    a.foreach(println(_))
    b.foreach(println(_))

  }

  def testdouble(): Unit ={
    val b=3500.00d
    println((b-3499.99)>0)

    println((3499.99+0.01)==b)
  }

  def testBytes(): Unit ={
//    Bytes
//    println(Bytes.toDouble(Bytes.toBytesBinary("@\\xE0\\x91\\x18\\xA3\\xD7\\x0A=")))
    println(Bytes.toDouble(Bytes.toBytesBinary("@r\\xC0\\x00\\x00\\x00\\x00\\x00")))
  }

  def testBytestoInt(): Unit ={
//    println(Bytes.toInt(Bytes.toBytesBinary("\\x00\\x00\\x00\\x10")))
    var a=1
    a+=1
    println(a)
  }
  def testWhile(): Unit ={
    val list= ListBuffer()
    val option=new Some(list)
//    println(option.isEmpty)
    val l=option.get
    println(l.size)

  }

  def testSet(): Unit ={
    val set=Set("a","b","c")
    println(set.contains("b"))
    println(set.contains("d"))
  }

  def testThrowException(): Unit ={
    try{
      println("run try catch!")
      throw new RuntimeException("error")
    }catch{
      case e:Exception=>{
        throw new RuntimeException("throw error"+e.getMessage)
      }
    }finally {
        println("finally")
    }
  }
}
