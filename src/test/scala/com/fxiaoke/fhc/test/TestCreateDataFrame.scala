package com.fxiaoke.fhc.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by jief on 2017/3/31.
  */
object TestCreateDataFrame {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SimpleApp").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._
    val foobarRdd:RDD[People] = sc.parallelize(Array(new People("jief1",31),new People("jief2",32),new People("jief3",33),new People("jief4",34),new People("jief5",35)))
    val df=sqlContext.createDataFrame(foobarRdd,classOf[People])
//    foobarRdd.toDF

    sqlContext.read.load("/facishare-data/fscloud/dw_dim/dim_pub_enterprise_info_static_parquet")
    foobarRdd.collect()
    df.registerTempTable("people")
    df.show()
    df.schema.fields
    df.select($"name",$"age").show()

    sc.stop()
  }
}
