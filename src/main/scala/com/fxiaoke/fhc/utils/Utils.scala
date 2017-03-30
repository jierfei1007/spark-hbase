package com.fxiaoke.fhc.utils

import java.text.SimpleDateFormat
import java.util.Calendar

import com.google.common.base.Strings

/**
  * Created by jiangxd on 2016/10/11.
  */
object Utils {

  /**
    * String 转 Int
    *
    * @param str       string
    * @param deaultint 默认转换失败的时候默认值
    * @return Int
    */
  def string2int(str: String, deaultint: Int): Int = {
    var resultInt = deaultint
    try {
      resultInt = str.toInt
    } catch {
      case e: Exception => e
    }
    resultInt
  }

  def stringJudge(str: String, defaultStr: String): String = {
    return if (Strings.isNullOrEmpty(str)) defaultStr else str
  }

  def getBom(runDate: java.util.Date): String = {
    val sf3 = new SimpleDateFormat("yyyyMMdd")
    val calendar = Calendar.getInstance()
    calendar.setTime(runDate)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val bomDate = sf3.format(calendar.getTime)
    bomDate
  }

  def daysBetween(smdate: String, bdate: String): Int = {
    val sdf = new SimpleDateFormat("yyyyMMdd")
    val cal = Calendar.getInstance()
    cal.setTime(sdf.parse(smdate))
    val smallDateTime = cal.getTimeInMillis()
    cal.setTime(sdf.parse(bdate))
    val bigDateTime = cal.getTimeInMillis()
    val between_days = (bigDateTime - smallDateTime) / (1000 * 3600 * 24)

    return Integer.parseInt(String.valueOf(between_days))
  }

  /**
    * MonitorRequsest 转化为Map
    *
    * @param str 字符串
    * @return Map
    */
  def splitter(str: String): java.util.Map[String, String] = {
    val imap = new java.util.HashMap[String, String]()
    val data = str.split("\u0001")
    if (data != null) {
      for (i <- 0 to data.length - 1) {
        val temp = data(i).split("\u0002")
        if (temp != null && temp.length == 2) {
          imap.put(temp(0), temp(1))
        }
      }
    }
    imap
  }

}
