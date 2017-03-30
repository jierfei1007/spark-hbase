package com.fxiaoke.fhc.source

import java.text.SimpleDateFormat

import com.fxiaoke.fhc.bean.EmployeeInfo
import com.fxiaoke.fhc.context.HuijuContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD

/**
  * Created by jiangxd on 2016/10/25.
  */
object EmployeeDynamicSource {

  /**
    * 读取员工动态表昨日数据
    *
    * @param employeeDynamicYtyInPutPath  员工动态表昨日数据路径
    * @param huijuContext 运行环境
    * @return RDD[EmployeeInfo]
    */
  def getEmployeeInfoFromFile(employeeDynamicYtyInPutPath:String,huijuContext: HuijuContext): RDD[EmployeeInfo] = {
      val conf=new Configuration()
      val hdfs=FileSystem.get(conf)
      val findf=new Path(employeeDynamicYtyInPutPath)
      val isExists=hdfs.exists(findf)
      if (!isExists){
          return huijuContext.sparkContext.parallelize(List(-1)).map(r => {
              val employeeInfo = new EmployeeInfo
              employeeInfo.setEid(r)
              employeeInfo
          })
      }
    var employeeRdd = huijuContext.sparkContext.textFile(employeeDynamicYtyInPutPath)
      .map(emp => {
        val sdf = new SimpleDateFormat("yyyy-MM-dd")
        val columns = emp.split("\t")
        //注册时间
        var registerTime: java.util.Date = null
        try {
          if ("null".equals(columns(2))) {
            registerTime = null
          } else {
            registerTime = sdf.parse(columns(2))
          }
        } catch {
          case ex: Exception =>
        }

        //第一次登陆时间
        var firstLoginTime: java.util.Date = null
        try {
          if (columns(3).length == 10) {
            firstLoginTime = sdf.parse(columns(3))
          }
        } catch {
          case ex: Exception =>
        }

        //最后登录时间
        var lastLoginTime: java.util.Date = null
        try {
          if (columns(4).length == 10){
            lastLoginTime = sdf.parse(columns(4))
          }
        } catch {
          case ex: Exception =>
        }

        //流失状态
        val lostStatus = if (columns(5).toInt == 1) true else false

        //流失时间
        var lostTime: java.util.Date = null
        try {
          if (columns(6).length == 10) {
            lostTime = sdf.parse(columns(6))
          }
        } catch {
          case ex: Exception =>
        }

        //最后登录终端
        val lastLoginPf = columns(7).toInt

        //最后登录版本号
        val lastLoginProv = columns(8).toInt

        //最后登录终端版本号
        val lastLoginPfProv = columns(9)

        //最后登录付费状态
        val lastEnterpriseType = columns(10).toInt

        //流失付费状态
        val lostEnterpriseType = columns(11).toInt

        //注册付费状态
        val registerEnterpriseType = columns(12).toInt

        //第一次登录付费状态
        val firstLoginEnterpriseType = columns(13).toInt
        val enterpriseType = columns(14).toInt
        val lastEnterpriseAccountNumber = columns(15).toInt
        val lostEnterpriseAccountNumber = columns(16).toInt
        val registerEnterpriseAccountNumber  = columns(17).toInt
        val firstLoginEnterpriseAccountNumber = columns(18).toInt
        val enterpriseAccountNubmer = columns(19).toInt
          val isStop = columns(20).toInt
          val isStopDesc = columns(21)

        val employeeInfo = new EmployeeInfo
        employeeInfo.setEid(columns(0).toInt)
        employeeInfo.setUid(columns(1).toInt)
        employeeInfo.setRegisteTime(registerTime)
        employeeInfo.setFirstLoginTime(firstLoginTime)
        employeeInfo.setLastLoginTime(lastLoginTime)
        employeeInfo.setLostStatus(lostStatus)
        employeeInfo.setLostTime(lostTime)
        employeeInfo.setLastLoginPf(lastLoginPf)
        employeeInfo.setLastLoginProv(lastLoginProv)
        employeeInfo.setLastLoginPfProv(lastLoginPfProv)
        employeeInfo.setLastEnterpriseType(lastEnterpriseType)
        employeeInfo.setLostEnterpriseType(lostEnterpriseType)
        employeeInfo.setRegisteEnterpriseType(registerEnterpriseType)
        employeeInfo.setFirstLoginEnterpriseType(firstLoginEnterpriseType)
        employeeInfo.setEnterpriseType(enterpriseType)
        employeeInfo.setLastEnterpriseAccountNumber(lastEnterpriseAccountNumber)
        employeeInfo.setLostEnterpriseAccountNumber(lostEnterpriseAccountNumber)
        employeeInfo.setRegisteEnterpriseAccountNumber(registerEnterpriseAccountNumber)
        employeeInfo.setFirstLoginEnterpriseAccountNumber(firstLoginEnterpriseAccountNumber)
        employeeInfo.setEnterpriseAccountNumber(enterpriseAccountNubmer)
        employeeInfo.setIsStop(isStop)
        employeeInfo.setIsStopDesc(isStopDesc)
        employeeInfo
      })
    if (employeeRdd.isEmpty()){
        employeeRdd = huijuContext.sparkContext.parallelize(List(-1)).map(r => {
            val employeeInfo = new EmployeeInfo
            employeeInfo.setEid(r)
            employeeInfo
        })
    }
    employeeRdd
  }


}
