package com.fxiaoke.fhc.source

import com.alibaba.fastjson.JSON
import com.fxiaoke.fhc.bean.{EnterpriseBean, Properties}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * Created by jiangxd on 2016/10/10.
  */
object SourceEnterpriseSource {

  /**
    * 从企业创建日志文件获取企业信息RDD
    *
    * @param sparkContext         SparkContext
    * @param enterpriseSourcePath SourceEntreprise HDFS路径
    * @return RDD[EnterpriseBean]
    */
  def getSourceEnterpriseRDD(sparkContext: SparkContext, enterpriseSourcePath: String): RDD[EnterpriseBean] = {
    val enterpriseRdd = sparkContext.textFile(enterpriseSourcePath).map(r => {
      var enterprise: EnterpriseBean = null
      try {
        enterprise = JSON.parseObject(r, classOf[EnterpriseBean])
        enterprise.setKeycontactPhone("null") //JSON文件中没有这一列，所以手动设置为null字符串
        //companyScale处理 未知的公司规模修改为 1
        if (enterprise.getCompanyScale == 0) {
          enterprise.setCompanyScale(1)
        }
        //身份编号
        val province = Properties.PROVINCE_LIST
        //匹配不到的省份赋值为-1  未知
        if (!province.contains(enterprise.getProvince)) {
          enterprise.setProvince(-1)
        }
      } catch {
        case e: Exception => e
      }
      enterprise
    }).filter(_ != null)
    enterpriseRdd
  }
}
