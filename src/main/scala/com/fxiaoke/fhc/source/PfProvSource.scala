package com.fxiaoke.fhc.source

import java.sql.{DriverManager, ResultSet}
import java.util.Map

import com.fxiaoke.fhc.context.HuijuContext
import com.fxiaoke.fhc.log.PrintLog
import com.fxiaoke.fhc.utils.Utils
import org.apache.spark.rdd.{JdbcRDD, RDD}

/**
  * Created by jiangxd on 2016/10/24.
  */
object PfProvSource {


  /**
    * 从Sqlserver获取Pfprov已经存在的版本号
    *
    * @return ((pf, prov), id)
    */
  def getPfProvSqlServerRDD(huijuContext: HuijuContext,
                            propConfig: Map[String, String]): RDD[((Int, Int), Int)] = {

    val url = propConfig.get("sqlserver.url")
    val database = propConfig.get("sqlserver.database")
    val username = propConfig.get("sqlserver.username")
    val pwd = propConfig.get("sqlserver.password")

    PrintLog.log(" url: " + url)
    PrintLog.log(" database: " + database)
    PrintLog.log(" username: " + username)
    PrintLog.log(" pwd: " + pwd)

    val pfprovOldRdd = new JdbcRDD(
      huijuContext.sparkContext,
      () => {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance()
        DriverManager.getConnection(url + ";DatabaseName=" + database, username, pwd)
      },
      "SELECT * FROM Prov WHERE 2 >= ? AND 2 <= ? and  (value is null or value ='')",
      0, 4, 10,
      r => {
        val id = r.getInt("id")
        val pf = r.getInt("pf")
        val prov = r.getInt("prov")
        val version = r.getString("value")
        ((pf, prov), id)
      }
    )
    pfprovOldRdd
  }

  /**
    * 将Prov JDBCRDD
    *
    * @param huijuContext 运行环境
    * @param propConfig   配置文件
    * @return (id,prov,value,pf,pfprov) RDD[(Int, Int,String, Int,String)]
    */
  def getPfProvInfoRDD(huijuContext: HuijuContext,
                       propConfig: Map[String, String]): RDD[(Int, Int, String, Int, String)] = {

    val url = propConfig.get("sqlserver.url")
    val database = propConfig.get("sqlserver.database")
    val username = propConfig.get("sqlserver.username")
    val pwd = propConfig.get("sqlserver.password")

    PrintLog.log(" url: " + url)
    PrintLog.log(" database: " + database)
    PrintLog.log(" username: " + username)
    PrintLog.log(" pwd: " + pwd)

    val pfprovRDD = new JdbcRDD(
      huijuContext.sparkContext,
      () => {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance()
        DriverManager.getConnection(url + ";DatabaseName=" + database, username, pwd)
      },
      "SELECT * FROM Prov WHERE 2 >= ? AND 2 <= ? ",
      0, 4, 10,
      r => {
        val id = r.getInt("id")
        val pf = r.getInt("pf")
        val prov = r.getInt("prov")
        val value = r.getString("value")
        val pfprov = r.getString("pfprov")
        (id, prov, value, pf, pfprov)
      }
    )
    pfprovRDD
  }

  /**
    * 统计monitorRequest的((pf, prov), vs)
    *
    * @param huijuContext                 运行环境
    * @param monitorRequestDailyInPutPath monitorRequest输入
    * @return ((pf, prov), vs)
    */
  def getMonitorPfprovRDD(huijuContext: HuijuContext,
                          monitorRequestDailyInPutPath: String): RDD[((Int, Int), String)] = {

    val pfprovRDD = huijuContext.sparkContext.textFile(monitorRequestDailyInPutPath).map(r => {
      r.split("\t")
    }).map(x => {
      var t: java.util.Map[String, String] = null
      if (x != null && x.size == 2) {
        val str = x(1)
        try {
          // t = Splitter.on('\u0001').withKeyValueSeparator('\u0002').split(str)
          t = Utils.splitter(str)
        } catch {
          case _ => {
          }
        }
      }
      //MAP(String,String)
      t
    }).filter(_ != null)
      .map(r => {
        try {
          var pf = 0
          var prov = 0
          var version = ""
          val osv = r.get("M4").toString.toLowerCase() //操作系统 （Android/iOS）
          val vs = r.get("M6").toString;
          //纷享逍客应用版本号（5.1.0/5.2.0）

          try {
            prov = Integer.parseInt(r.get("M7")) //纷享销客应用内部版本号
          } catch {
            case _ =>
          }
          val dpf = r.get("M8").toString.toLowerCase() //设备型号

          //判断操作系统类型
          //ios
          if (osv.contains("iphone") || osv.contains("ipad") || osv.contains("itouch") || osv.contains("ios")) {
            pf = 1203
          }
          //android
          if (osv.contains("android") || dpf.contains("meizu") || dpf.contains("xiaomi")
            || dpf.contains("huawei") || dpf.contains("oppo") || dpf.contains("vivo")
            || dpf.contains("doov") || dpf.contains("samsung")) {
            if (pf != 1203) {
              pf = 1303
            }
          }
          (pf, prov, vs)
        } catch {
          case _ => (0, 0, "")
        }
        //(pf, prov, vs)
      }).filter(r => {
      if (r._1 != 0 && r._2 != 0) {
        true
      } else {
        false
      }
    }).map(r => (r, 0)) //((pf, prov, vs),0)
      .reduceByKey(_ + _) //((pf, prov, vs),0)
      //((pf, prov), vs)
      .map(r => ((r._1._1, r._1._2), r._1._3)).map(r => {
      var v = r._2.trim
      val prov = r._1._2.toString
      if (v.length >= 5) {
        v = v.trim.substring(0, 5)
      } else if (v.length == 3) {
        if (prov.length == 9) {
          v = v + "." + prov.charAt(5)
        } else if (prov.length == 6 && prov.charAt(0) == '5') {
          v = v + "." + prov.charAt(2)
        } else {
          v = v + ".0"
        }
      }
      ((r._1._1, r._1._2, v), 0)
    }).reduceByKey(_ + _).map(r => ((r._1._1, r._1._2), r._1._3)) //((pf, prov), vs)

    pfprovRDD
  }


  /**
    * 获取Prov表所有数据
    *
    * @return (prov,value,pf,pfprov)  RDD[(String, String, String, String)]
    */
  def getProvRdd(propConfig: java.util.Map[String, String], huijuContext: HuijuContext): RDD[(String, String, String, String)] = {


    val url = propConfig.get("sqlserver.url")
    val database = propConfig.get("sqlserver.database")
    val username = propConfig.get("sqlserver.username")
    val password = propConfig.get("sqlserver.password")

    //数据库连接函数
    //TODO 连接获取方式
    def connFun() = {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance()
      DriverManager.getConnection(url + ";DatabaseName=" + database, username, password)
    }

    val provSQL = "SELECT * FROM Prov WHERE 2 >= ? AND 2 <= ? "
    def provRsFun(r: ResultSet): (String, String, String, String) = {
      val prov = r.getInt("prov")
      val value = r.getString("value")
      val pf = r.getInt("pf")
      val pfprov = r.getString("pfprov")
      (prov.toString, value, pf.toString, pfprov) //(prov,value,pf,pfprov)
    }

    new JdbcRDD(huijuContext.sparkContext, connFun, provSQL, 0, 4, 1, provRsFun).filter(r => {
      // 过滤prov
      r._2 != null && !"".equals(r._2) && !"null".equals(r._2)
    }).distinct()
  }


}



