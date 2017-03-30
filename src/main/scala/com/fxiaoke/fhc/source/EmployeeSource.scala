package com.fxiaoke.fhc.source

import java.util.Calendar
import com.fxiaoke.fhc.bean.Employee
import com.alibaba.fastjson.JSON
import com.fxiaoke.fhc.context.HuijuContext
import org.apache.spark.rdd.RDD

/**
  * Created by jiangxd on 2016/10/24.
  */
object EmployeeSource {

  /**
    * 获取每日账号中心 的数据
    *
    * @param huijuContext            运行环境
    * @param employeeSourceDailyPath 员工数据源
    * @return RDD[Employee]
    */
  def getEmpFromFile(huijuContext: HuijuContext, employeeSourceDailyPath: String): RDD[Employee] = {

    val employeeRdd = huijuContext.sparkContext.textFile(employeeSourceDailyPath).map(r => {
      var emp = new Employee
      try {
        emp = JSON.parseObject(r, classOf[Employee])
      } catch {
        case ex: Exception =>
      }
      emp
    })
    employeeRdd
  }

  /**
    *  从员工源表里面获取到各企业的开通账号数(各企业的员工数)
    * @param huiJuContext  运行环境
    * @param employeeSourceDailyPath  员工源数据
    * @param runDate 运行日期
    * @return  (eid,accountNumber)  RDD[(Int, Int)]
    */
  def getEnterpriseAccountNumberFromRDD(huiJuContext: HuijuContext, employeeSourceDailyPath: String, runDate: java.util.Date): RDD[(Int, Int)] = {
    val sparkContext = huiJuContext.sparkContext
    val cal = Calendar.getInstance()
    cal.setTime(runDate)
    cal.add(Calendar.DATE,1)
    val enterpriseAccountNumber = getEmpFromFile(huiJuContext,employeeSourceDailyPath)
      .map(employee => {
        (employee.getEnterpriseId, employee.getEmployeeId, employee.getCreateTime)
      })
      .filter(_._3.before(cal.getTime)) //TODO 坑
      .map(x => {
      //(eid,accountNumber)
      (x._1, 1)
    })
      .reduceByKey(_ + _)

    enterpriseAccountNumber
  }


}
