package com.fxiaoke.fhc.context

import java.util.Date
import com.fxiaoke.fhc.utils.Config
import org.apache.spark.sql.{Dataset, SQLContext}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}
/**
  * env 环境线上是foneshare,线下SDE,FTE
  * Created by bill on 16/7/18.
  */
class HuijuContext(env: String, appName: String, master: String) {
  println("默认构造器开始!")
  /**
    * 任务起始时间
    */
  private val appStartTime = new Date()
  /**
    * 配置中心位置
    */
  private val configName = "huiju-spark-util-flush"
  /**
    * 获取配置中心代码,key-value形式代码
    */
  private val appConf = Config.getConfig(configName, env)
  /**
    * 获取当前模式
    */
  val debugflag = getValue("debug.flag").toBoolean
  /**
    *
    */
  private val sparkConf = new SparkConf().setAppName(appName).setMaster(master)
  val sparkContext = new SparkContext(sparkConf)

  /**
    * SparkSQLContext
    */
  lazy val sqlContext: SQLContext = new SQLContext(sparkContext)

  /**
    * HiveSQlContext
    */
  lazy val hiveContext: HiveContext = new HiveContext(sparkContext)
  /**
    * @param env
    * @param appName
    */
  def this(env: String, appName: String) {
    this(env, appName, "yarn-cluster")
    log("辅助构造器 this(env: String, appName: String)")
  }
  /**
    *
    * @param appName
    */
  def this(appName: String) {
    this("foneshare", appName, "yarn-cluster")
    log("辅助构造器 this(appName: String)")
  }
  /**
    * 所有参数都默认
    */
  def this() {
    this("foneshare", "汇聚任务" + new Date(), "yarn-cluster")
    log("辅助构造器 this()")
  }
  /**
    * 获取配置
    * @param key
    * @return
    */
  def getValue(key: String): String = {
    appConf.get(key)
  }
  /**
    * 日志打印
    */
  def log(msg: String): Unit = {
    if (debugflag) {
      println(msg)
    }
  }
  /**
    * stop SparkContext
    */
  def stop(): Unit = {
    sparkContext.stop()
  }

}