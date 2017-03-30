package com.fxiaoke.fhc.source

import java.text.SimpleDateFormat
import java.util.Date

import com.fxiaoke.fhc.context.HuijuContext
import com.fxiaoke.fhc.utils.Utils
import org.apache.spark.rdd.RDD

/**
  * Created by jiangxd on 2016/10/24.
  */
object ServerActionSource {

  /**
    * 获取ServerAction当天的数据
    *
    * @param huijuContext     运行环境
    * @param serverActionPath CEP数据源地址
    * @param runDate          运行日期
    * @return (eid,uid,Platform,prov,Date)
    */
  def getServerActionNew(huijuContext: HuijuContext, serverActionPath: String, runDate: java.util.Date): RDD[(String, String, String, String, String)] = {

    val serverActionRDD = huijuContext.sparkContext.textFile(serverActionPath).map(r => {
      r.split("\t")
    }).filter(_.length == 2).map(x => {
      val str = x(1)
      //val t = Splitter.on('\u0001').withKeyValueSeparator('\u0002').split(str)
      val t = Utils.splitter(str)
      var prov = t.get("ProductVersion")
      if (prov == null || prov.equals("null")) {
        prov = "0"
      }
      //兼容time
      val sf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      var date: Date = runDate
      val time = t.get("Time")
      try {
        val longTime = java.lang.Long.parseLong(time)
        date = new Date(longTime)
      } catch {
        case _ => {
          try {
            date = sf.parse(time)
          }
          catch {
            case _ => {
              println(str)
            }
          }
        }
      }

      (t.get("EnterpriseID"), t.get("UserID"), t.get("Platform"), prov, sf.format(date))
    }).map(r => {
      var eid = r._1 //EnterpriseID
      var uid = r._2 //UserID

      try {
        Integer.parseInt(eid)
      } catch {
        case _ => eid = "0"
      }

      try {
        Integer.parseInt(uid)
      } catch {
        case _ => uid = "0"
      }
      //(eid,uid,Platform,prov,Date)
      (eid, uid, r._3, r._4, r._5)
    }).filter(r => {
      !("0".equals(r._1) || "0".equals(r._2) || "null".equals(r._1) || "null".equals(r._2))
    })
    serverActionRDD

  }

  /**
    * 对ServerAction去重
    *
    * @param serverAction
    * @return ((eid,uid),(pf,prov))
    */
   def getServerActionDistinctRDD(serverAction: RDD[(String, String, String, String, String)]): RDD[((Int, Int), (Int, Int))] = {
    val sa = serverAction //(eid,uid,pf,prov,lut)
      .map(r => ((r._1, r._2), (r._3, r._4, r._5))) //((eid,uid),(pf,prov,lut))
      .reduceByKey((x, y) => {
      val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      var pf = ""
      var prov = ""
      var lut = ""
      try {
        if (x == null) {
          pf = y._1
          prov = y._2
          lut = y._3
        }
        if (y == null) {
          pf = x._1
          prov = x._2
          lut = x._3
        }
        if (x != null && y != null) {
          val lut_x = sdf.parse(x._3).getTime
          val lut_y = sdf.parse(y._3).getTime
          if (lut_x >= lut_y) {
            pf = x._1
            prov = x._2
            lut = x._3
          } else {
            pf = y._1
            prov = y._2
            lut = y._3
          }
        }
      } catch {
        case ex: Exception =>
      }
      (pf, prov, lut)
    })
      .map(r => {
        var pf = -1
        var prov = -1
        var eid = -1
        var uid = -1
        try {
          pf = r._2._1.toInt
        } catch {
          case ex: Exception =>
        }
        try {
          prov = r._2._2.toInt
        } catch {
          case ex: Exception =>
        }
        try {
          eid = r._1._1.toInt
        } catch {
          case ex: Exception =>
        }
        try {
          uid = r._1._2.toInt
        } catch {
          case ex: Exception =>
        }
        ((eid, uid), (pf, prov))
      })
    sa
  }


}
