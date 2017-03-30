package com.fxiaoke.fhc.source

import java.util

import com.fxiaoke.fhc.bean.IndustryBean

import scala.io.Source

/**
  * Created by jiangxd on 2016/10/12.
  */
object IndustrySource {

  /**
    * 获取行业类别树
    *
    * @return Map
    */
  def getIndustryMap(): util.HashMap[Int, IndustryBean] = {
    val industryMap = new util.HashMap[Int, IndustryBean]()
    val file = Source.fromURL(getClass.getResource("/EnterpriseIndustry.txt")).getLines()

    for (line <- file) {
      val cols = line.split("\t")
      val industryBean = new IndustryBean
      industryBean.setIndustryID(cols(1).toInt)
      industryBean.setParentIndustryID(cols(2).toInt)
      industryBean.setIndustryName(cols(0))
      industryBean.setIndustryType(cols(3).toInt)
      industryMap.put(cols(1).toInt, industryBean)
    }
    industryMap
  }
}
