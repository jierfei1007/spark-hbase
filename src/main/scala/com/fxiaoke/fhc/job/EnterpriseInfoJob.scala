package com.fxiaoke.fhc.job

import java.util
import java.util.UUID

import com.fxiaoke.fhc.bean._
import com.fxiaoke.fhc.utils.{HbaseCommonUtils, Utils}
import org.apache.hadoop.hbase.TableName
import org.apache.spark.rdd.RDD

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.control.Breaks._

/**
  * Created by jiangxd on 2016/10/11.
  */
object EnterpriseInfoJob {

  /**
    * 企业表主计算逻辑
    *
    * @param enterpriseSourceRDD             企业大宽表数据源
//    * @param enterpriseTypeRDD               企业类型
    * @param enterpriseBlackRDD              黑名单惬意
    * @param enterpriseStaticRDD             昨日的企业静态表
    * @param ibssVendorMap                   代理商企业 代理商名称关联
    * @param runDateStr                      运行时间  拉链表
    * @param ydayDateStr                     昨日运行时间  拉链表
    * @param enterpriseStaticDailyOutPutPath 企业静态表日表留痕输出路径
    * @param enterpriseStaticOutPutPath      企业静态表输出路径
    * @param industryMap                     行业Map
    * @param districtMap                     城市映射
    * @param agentEaMap                      联营/代理商 Ea  企业组别
    * @return
    */
  def getEnterpriseBaseInfo(enterpriseSourceRDD: RDD[EnterpriseBean],
                            enterpriseBlackRDD: RDD[(Int, Int)],
                            enterpriseStaticRDD: RDD[EnterpriseStaticBean],
                            ibssVendorMap: Map[Int, String],
                            runDateStr: String,
                            ydayDateStr: String,
                            enterpriseStaticDailyOutPutPath: String,
                            enterpriseStaticOutPutPath: String,
                            industryMap: util.HashMap[Int, IndustryBean],
                            districtMap: Map[String, String],
                            agentEaMap: util.HashMap[String, Int],propConfig:util.Map[String,String],checkOrderDate:String): RDD[EnterpriseStaticBean] = {

    val neweEnterpriseStaticRDD02 = enterpriseSourceRDD.coalesce(10).mapPartitions(itor => {
      val result = ListBuffer[Tuple2[Int,EnterpriseBean]]()
      val connection = HbaseCommonUtils.getHbaseConnection(propConfig)
      val orderTable=connection.getTable(TableName.valueOf("ENTERPRISE_ORDERS"))
      val refundTable=connection.getTable(TableName.valueOf("ENTERPRISE_REFUNDS"))
      while(itor.hasNext){
        val enterpriseBean=itor.next()
        val eid=enterpriseBean.getEnterpriseId
        val ordersListBuffer=HbaseCommonUtils.scanEnterpriseOrdersByCreateDate(orderTable,eid,checkOrderDate+" "+"23:59:59")
        val refundListBuffer=HbaseCommonUtils.scanEnterpriseOrderRefund(eid,refundTable)
        val eType=getEnterpriseType(eid,ordersListBuffer,refundListBuffer,checkOrderDate+" "+"23:59:59")
        enterpriseBean.setEnterpriseType(eType)
        result.+=((enterpriseBean.getEnterpriseId, enterpriseBean))
      }
      orderTable.close()
      refundTable.close()
      connection.close()
      result.iterator
    })

//    val neweEnterpriseStaticRDD02 = neweEnterpriseStaticRDD01
//      /**
//        * 左外连接
//        * 关联企业类型 (如果企业类型EID重复会导致企业表重复)
//        */
//      .leftOuterJoin(enterpriseTypeRDD)
//      .map(x => {
//        var enterpriseType: Int = -1
//        if (x._2._2.isDefined) {
//          enterpriseType = x._2._2.get
//        }
//        var enterpriseBean = x._2._1
//        enterpriseBean.setEnterpriseType(enterpriseType)
//        (enterpriseBean.getEnterpriseId, enterpriseBean)
//      })

    val neweEnterpriseStaticRDD03 = neweEnterpriseStaticRDD02
      /**
        * 关联黑名单企业标记
        */
      .leftOuterJoin(enterpriseBlackRDD)
      .map(x => {
        var enterpriseGroup: Int = -1
        var enterpriseGroupDesc: String = "NULL"
        if (x._2._2.isDefined) {
          enterpriseGroup = Properties.ENTERPRISE_GROUP_BLACK._1
          enterpriseGroupDesc = Properties.ENTERPRISE_GROUP_BLACK._2
        }
        var enterpriseBean = x._2._1
        enterpriseBean.setEnterpriseGroup(enterpriseGroup)
        enterpriseBean.setEnterpriseGroupDesc(enterpriseGroupDesc)
        (enterpriseBean.getEnterpriseId, enterpriseBean)
      })
    //    PrintLog.log(" neweEnterpriseStaticRDD03 Count : " + neweEnterpriseStaticRDD03.count())
    val neweEnterpriseStaticRDD = neweEnterpriseStaticRDD03

      /**
        * 关联昨日最新的enterpriseStatic企业静态表，字段发生变化就进行数据版本新增
        */
      .leftOuterJoin(enterpriseStaticRDD.map(enterpriseStaticBean => (enterpriseStaticBean.getEnterpriseID, enterpriseStaticBean)))
      .flatMap(x => {
        var enterpriseStaticBeanArray = new ArrayBuffer[EnterpriseStaticBean]()
        //x = (enterpriseID,(enterpriseBean,Option(enterpriseStaticBean)))
        //x._2._2 = enterpriseStaticBean
        /**
          * 根据原始企业表获取原企业基本信息，字段无法获取正确值的，字符串默认赋值为"NULL"，整形无特殊情况默认赋值为-1
          * 企业规模未知的类型赋值为1 为了与之前已经定义好的NBI前端映射兼容
          */
        val enterpriseBean = x._2._1
        val defaultStr = "NULL"
        val defaultInt = -1
        val pubStringJudge = (str: String) => Utils.stringJudge(str, defaultStr)
        val pubString2Int = (str: String) => Utils.string2int(str, defaultInt)
        val enterpriseName = pubStringJudge(enterpriseBean.getEnterpriseName)
        val enterpriseShortName = pubStringJudge(enterpriseBean.getEnterpriseShortName)
        val enterpriseAccount = pubStringJudge(enterpriseBean.getEnterpriseAccount)
        val vendorID = pubString2Int(enterpriseBean.getVendorId)
        val vendorName = pubStringJudge(ibssVendorMap.get(vendorID).getOrElse(defaultStr))
        val creatorID = pubString2Int(enterpriseBean.getCreatorId)
        val keycontactName = pubStringJudge(enterpriseBean.getKeycontactName)
        val keycontactPhone = pubStringJudge(enterpriseBean.getKeycontactPhone)
        val keyContactEmail = pubStringJudge(enterpriseBean.getKeyContactEmail)
        val contactName = pubStringJudge(enterpriseBean.getContactName)
        val contactPhone = pubStringJudge(enterpriseBean.getContactPhone)
        val contactEmail = pubStringJudge(enterpriseBean.getContactEmail)
        val contactIm = pubStringJudge(enterpriseBean.getContactIm)
        val address = pubStringJudge(enterpriseBean.getAddress)
        val remark = pubStringJudge(enterpriseBean.getRemark)
        val accountTotalAmount = pubString2Int(enterpriseBean.getAccounttotalAmount)
        val runStatus = pubString2Int(enterpriseBean.getRunStatus)
        val runStatusDesc = pubStringJudge(Properties.RUN_STATUS_MAP.get(runStatus).getOrElse(defaultStr))
        var source = 0
        if (vendorID == Properties.DEFAULT_VENDORID && creatorID == Properties.DEFAULT_CREATEORID) source = 1 else source = 0
        val sourceDesc = pubStringJudge(Properties.SOURCE_MAP.get(source).getOrElse(defaultStr))
        val registerTime = enterpriseBean.getCreateTime.toString
        val appStartTime = enterpriseBean.getAppStartTime
        //行业类别
        val industry = enterpriseBean.getIndustry
        var industry1 = defaultInt
        var industry2 = defaultInt
        var industry3 = defaultInt
        var industry1Value = defaultStr
        var industry2Value = defaultStr
        var industry3Value = defaultStr
        val industryBean = industryMap.get(industry)
        if (industryBean != null && industryBean.getIndustryType != null) {
          val defaultIndustryBean = new IndustryBean(defaultInt, defaultInt, defaultInt, defaultStr)
          //如果是一级 就只存一级的行业
          if (Properties.INDUSTRY_ONE == industryBean.getIndustryType) {
            industry1 = industry
            industry1Value = pubStringJudge(industryBean.getIndustryName)
          } else if (Properties.INDUSTRY_TWO == industryBean.getIndustryType) {
            //如果是二级的行业就只存一级和二级
            industry2 = industry
            industry2Value = pubStringJudge(industryBean.getIndustryName)
            industry1 = industryBean.getParentIndustryID
            industry1Value = industryMap.getOrDefault(industry1, defaultIndustryBean).getIndustryName
          } else if (Properties.INDUSTRY_THREE == industryBean.getIndustryType) {
            //如果是三级行业就一二三级都存
            industry3 = industry
            industry3Value = pubStringJudge(industryBean.getIndustryName)
            industry2 = industryBean.getParentIndustryID
            val industry2Bean = industryMap.getOrDefault(industry2, defaultIndustryBean)
            industry2Value = industry2Bean.getIndustryName
            industry1 = industry2Bean.getParentIndustryID
            industry1Value = industryMap.getOrDefault(industry1, defaultIndustryBean).getIndustryName
          }
        }

        val province = if (enterpriseBean.getProvince > 0) enterpriseBean.getProvince else defaultInt
        val provinceDesc = pubStringJudge(Properties.PROVINCE_MAP.get(province).getOrElse(defaultStr))
        //城市
        val cityStr = pubStringJudge(enterpriseBean.getCity)
        var city: String = defaultStr
        if (cityStr.matches("[0-9]+")) city = pubStringJudge(districtMap.get(cityStr.trim).getOrElse(defaultStr)) else city = cityStr

        var enterpriseGroup = enterpriseBean.getEnterpriseGroup
        var enterpriseGroupDesc = enterpriseBean.getEnterpriseGroupDesc
        if (agentEaMap.get(enterpriseAccount.trim) != null) {
          enterpriseGroup = Properties.ENTERPRISE_GROUP_AGENT._1
          enterpriseGroupDesc = Properties.ENTERPRISE_GROUP_AGENT._2
        } else if (Properties.FS_ENTERPRISE_ACCOUNT.equals(enterpriseAccount.trim)) {
          enterpriseGroup = Properties.ENTERPRISE_GROUP_FS._1
          enterpriseGroupDesc = Properties.ENTERPRISE_GROUP_FS._2
        } else if (enterpriseGroup == -1) {
          enterpriseGroup = Properties.ENTERPRISE_GROUP_NORMAL._1
          enterpriseGroupDesc = Properties.ENTERPRISE_GROUP_NORMAL._2
        }
        //企业规模 未知的赋值为 1
        val companyScale = if (enterpriseBean.getCompanyScale > 0) enterpriseBean.getCompanyScale else 1
        val companyScaleDesc = pubStringJudge(Properties.COMPANY_SCALE_MAP.get(companyScale).getOrElse(defaultStr))
        val enterpriseType = enterpriseBean.getEnterpriseType
        val enterpriseTypeDesc = Properties.ENTERPRISE_TYPE_MAP.get(enterpriseType).getOrElse(defaultStr)
        // TODO: add enterpriseScale. rules: A1<accountTotalAmount<10; A2; < 21 B<101 C<301 D>300
        val enterpriseScale =
          if (accountTotalAmount < 10) "A1"
          else if (accountTotalAmount < 21) "A2"
          else if (accountTotalAmount < 101) "B"
          else if (accountTotalAmount < 301) "C"
          else "D"

        /**
          * ①源企业信息不是新增企业，能从旧静态表中匹配到一个或多个同企业ID多版本数据的话就进行字段判断
          * ①①如果所有字段信息都没有变化的话，则源数据不变
          * ①②否则更新数据版本数据，源数据记录生效时间改为当前计算日期时间，新增一条数据为信息变化之后的数据，版本号加一，记录生效时间为计算日期
          */
        if (x._2._2.isDefined) {
          val enterpriseStaticBeanOld = x._2._2.get
          //如果是最大版本的数据
          if (Properties.SK_BIG_DATE.equals(enterpriseStaticBeanOld.getSkEndDate)) {
            //每个字段进行判断,如果发生变化就添加一条版本数据
            if (enterpriseName.equals(enterpriseStaticBeanOld.getEnterpriseName) &&
              enterpriseShortName.equals(enterpriseStaticBeanOld.getEnterpriseShortName) &&
              enterpriseAccount.equals(enterpriseStaticBeanOld.getEnterpriseAccount) &&
              vendorID.equals(enterpriseStaticBeanOld.getVendorID) &&
              vendorName.equals(enterpriseStaticBeanOld.getVendorName) &&
              creatorID.equals(enterpriseStaticBeanOld.getCreatorID) &&
              keycontactName.equals(enterpriseStaticBeanOld.getKeyContactName) &&
              keycontactPhone.equals(enterpriseStaticBeanOld.getKeyContactPhone) &&
              keyContactEmail.equals(enterpriseStaticBeanOld.getKeyContactEmail) &&
              contactName.equals(enterpriseStaticBeanOld.getContactName) &&
              contactPhone.equals(enterpriseStaticBeanOld.getContactPhone) &&
              contactEmail.equals(enterpriseStaticBeanOld.getContactEmail) &&
              contactIm.equals(enterpriseStaticBeanOld.getContactIM) &&
              address.equals(enterpriseStaticBeanOld.getEnterpriseAddress) &&
              remark.equals(enterpriseStaticBeanOld.getEnterpriseRemark) &&
              accountTotalAmount == enterpriseStaticBeanOld.getAccountTotalAmount &&
              runStatus == enterpriseStaticBeanOld.getRunStatus &&
              runStatusDesc.equals(enterpriseStaticBeanOld.getRunStatusDesc) &&
              industry1 == enterpriseStaticBeanOld.getIndustry1 &&
              industry1Value.equals(enterpriseStaticBeanOld.getIndustry1Value) &&
              industry2 == enterpriseStaticBeanOld.getIndustry2 &&
              industry2Value.equals(enterpriseStaticBeanOld.getIndustry2Value) &&
              industry3 == enterpriseStaticBeanOld.getIndustry3 &&
              industry3Value.equals(enterpriseStaticBeanOld.getIndustry3Value) &&
              province.equals(enterpriseStaticBeanOld.getEnterpriseProvince) &&
              provinceDesc.equals(enterpriseStaticBeanOld.getEnterpriseProvinceDesc) &&
              city.equals(enterpriseStaticBeanOld.getCity) &&
              registerTime.equalsIgnoreCase(enterpriseStaticBeanOld.getRegisteTime) &&
              appStartTime.equals(enterpriseStaticBeanOld.getAppStartTime) &&
              enterpriseGroup == enterpriseStaticBeanOld.getEnterpriseGroup &&
              enterpriseGroupDesc.equals(enterpriseStaticBeanOld.getEnterpriseGroupDesc) &&
              companyScale == enterpriseStaticBeanOld.getCompanyScale &&
              companyScaleDesc.equals(enterpriseStaticBeanOld.getCompanyScaleDesc) &&
              enterpriseType == enterpriseStaticBeanOld.getEnterpriseType &&
              enterpriseTypeDesc.equals(enterpriseStaticBeanOld.getEnterpriseTypeDesc)&&
            // TODO: add compare enterpriseScale
              enterpriseScale.equals(enterpriseStaticBeanOld.getEnterpriseScale)
            ) {
              enterpriseStaticBeanArray += enterpriseStaticBeanOld
            } else {
              var enterpriseStaticBean = new EnterpriseStaticBean
              enterpriseStaticBean.setSkEnterpriseID(UUID.randomUUID().toString)
              enterpriseStaticBean.setEnterpriseID(if (enterpriseBean.getEnterpriseId > 0) enterpriseBean.getEnterpriseId else defaultInt)
              enterpriseStaticBean.setEnterpriseName(enterpriseName)
              enterpriseStaticBean.setEnterpriseShortName(enterpriseShortName)
              enterpriseStaticBean.setEnterpriseAccount(enterpriseAccount)
              enterpriseStaticBean.setVendorID(vendorID)
              enterpriseStaticBean.setVendorName(vendorName)
              enterpriseStaticBean.setCreatorID(creatorID)
              enterpriseStaticBean.setKeyContactName(keycontactName)
              enterpriseStaticBean.setKeyContactPhone(keycontactPhone)
              enterpriseStaticBean.setKeyContactEmail(keyContactEmail)
              enterpriseStaticBean.setContactName(contactName)
              enterpriseStaticBean.setContactPhone(contactPhone)
              enterpriseStaticBean.setContactEmail(contactEmail)
              enterpriseStaticBean.setContactIM(contactIm)
              enterpriseStaticBean.setEnterpriseAddress(address)
              enterpriseStaticBean.setEnterpriseRemark(remark)
              enterpriseStaticBean.setAccountTotalAmount(accountTotalAmount)
              enterpriseStaticBean.setRunStatus(runStatus)
              enterpriseStaticBean.setRunStatusDesc(runStatusDesc)
              //行业类别
              enterpriseStaticBean.setIndustry1(industry1)
              enterpriseStaticBean.setIndustry2(industry2)
              enterpriseStaticBean.setIndustry3(industry3)
              enterpriseStaticBean.setIndustry1Value(industry1Value)
              enterpriseStaticBean.setIndustry2Value(industry2Value)
              enterpriseStaticBean.setIndustry3Value(industry3Value)
              enterpriseStaticBean.setEnterpriseProvince(province)
              enterpriseStaticBean.setEnterpriseProvinceDesc(provinceDesc)
              enterpriseStaticBean.setCity(city)
              enterpriseStaticBean.setEnterpriseSource(enterpriseStaticBeanOld.getEnterpriseSource)
              enterpriseStaticBean.setEnterpriseSourceDesc(enterpriseStaticBeanOld.getEnterpriseSourceDesc)
              //TODO:是否转换成Date类型
              enterpriseStaticBean.setRegisteTime(enterpriseBean.getCreateTime.toString)
              enterpriseStaticBean.setAppStartTime(enterpriseBean.getAppStartTime)
              enterpriseStaticBean.setEnterpriseGroup(enterpriseGroup)
              enterpriseStaticBean.setEnterpriseGroupDesc(enterpriseGroupDesc)
              enterpriseStaticBean.setCompanyScale(companyScale)
              enterpriseStaticBean.setCompanyScaleDesc(companyScaleDesc)
              enterpriseStaticBean.setEnterpriseType(enterpriseType)
              enterpriseStaticBean.setEnterpriseTypeDesc(enterpriseTypeDesc)
              enterpriseStaticBean.setSkVersion(enterpriseStaticBeanOld.getSkVersion + 1)
              enterpriseStaticBean.setSkBeginDate(runDateStr)
              enterpriseStaticBean.setSkEndDate(Properties.SK_BIG_DATE)
              // TODO: set new enterpriseScale
              enterpriseStaticBean.setEnterpriseScale(enterpriseScale)
              if (enterpriseStaticBean.getEnterpriseID > 0) {
                enterpriseStaticBeanArray += enterpriseStaticBean
              }
              enterpriseStaticBeanOld.setSkEndDate(ydayDateStr)
              enterpriseStaticBeanArray += enterpriseStaticBeanOld
            }

          } else {
            //否则还是原先的数据
            enterpriseStaticBeanArray += enterpriseStaticBeanOld
          }

        }

        /**
          * 无匹配到企业ID,该企业是新增企业信息,赋初始值 ,版本号为1，记录生效时间为19990101，记录失效时间为29990101
          */
        else {
          var enterpriseStaticBean = new EnterpriseStaticBean
          enterpriseStaticBean.setSkEnterpriseID(UUID.randomUUID().toString)
          enterpriseStaticBean.setEnterpriseID(if (enterpriseBean.getEnterpriseId > 0) enterpriseBean.getEnterpriseId else defaultInt)
          enterpriseStaticBean.setEnterpriseName(enterpriseName)
          enterpriseStaticBean.setEnterpriseShortName(enterpriseShortName)
          enterpriseStaticBean.setEnterpriseAccount(enterpriseAccount)
          enterpriseStaticBean.setVendorID(vendorID)
          enterpriseStaticBean.setVendorName(vendorName)
          enterpriseStaticBean.setCreatorID(creatorID)
          enterpriseStaticBean.setKeyContactName(keycontactName)
          enterpriseStaticBean.setKeyContactPhone(keycontactPhone)
          enterpriseStaticBean.setKeyContactEmail(keyContactEmail)
          enterpriseStaticBean.setContactName(contactName)
          enterpriseStaticBean.setContactPhone(contactPhone)
          enterpriseStaticBean.setContactEmail(contactEmail)
          enterpriseStaticBean.setContactIM(contactIm)
          enterpriseStaticBean.setEnterpriseAddress(address)
          enterpriseStaticBean.setEnterpriseRemark(remark)
          enterpriseStaticBean.setAccountTotalAmount(accountTotalAmount)
          enterpriseStaticBean.setRunStatus(runStatus)
          enterpriseStaticBean.setRunStatusDesc(runStatusDesc)
          //行业类别
          enterpriseStaticBean.setIndustry1(industry1)
          enterpriseStaticBean.setIndustry2(industry2)
          enterpriseStaticBean.setIndustry3(industry3)
          enterpriseStaticBean.setIndustry1Value(industry1Value)
          enterpriseStaticBean.setIndustry2Value(industry2Value)
          enterpriseStaticBean.setIndustry3Value(industry3Value)

          enterpriseStaticBean.setEnterpriseProvince(province)
          enterpriseStaticBean.setEnterpriseProvinceDesc(provinceDesc)
          enterpriseStaticBean.setCity(city)
          enterpriseStaticBean.setEnterpriseSource(source)
          enterpriseStaticBean.setEnterpriseSourceDesc(sourceDesc)
          //TODO:是否转换成Date类型
          enterpriseStaticBean.setRegisteTime(enterpriseBean.getCreateTime.toString)
          enterpriseStaticBean.setAppStartTime(enterpriseBean.getAppStartTime)
          enterpriseStaticBean.setEnterpriseGroup(enterpriseGroup)
          enterpriseStaticBean.setEnterpriseGroupDesc(enterpriseGroupDesc)
          enterpriseStaticBean.setCompanyScale(companyScale)
          enterpriseStaticBean.setCompanyScaleDesc(companyScaleDesc)
          enterpriseStaticBean.setEnterpriseType(enterpriseType)
          enterpriseStaticBean.setEnterpriseTypeDesc(enterpriseTypeDesc)
          enterpriseStaticBean.setSkVersion(Properties.DEFAULT_VERSION)
          enterpriseStaticBean.setSkBeginDate(Properties.SK_SMALL_DATE)
          enterpriseStaticBean.setSkEndDate(Properties.SK_BIG_DATE)
          // TODO: set enterpriseScale
          enterpriseStaticBean.setEnterpriseScale(enterpriseScale)
          if (enterpriseStaticBean.getEnterpriseID > 0) {
            enterpriseStaticBeanArray += enterpriseStaticBean
          }
        }

        /**
          * 返回enterpriseStatic集合：应为字段信息变化会导致新增一条数据，所以需要返回集合，用来保存旧数据和新增的数据
          */
        enterpriseStaticBeanArray
      }).filter(enterpriseStaticBean => enterpriseStaticBean != null)

    val resultEnterpriseStaticRDD = neweEnterpriseStaticRDD.map(enterpriseStaticBean => {
      enterpriseStaticBean.getSkEnterpriseID + "\t" +
        enterpriseStaticBean.getEnterpriseID + "\t" +
        enterpriseStaticBean.getEnterpriseName + "\t" +
        enterpriseStaticBean.getEnterpriseShortName + "\t" +
        enterpriseStaticBean.getEnterpriseAccount + "\t" +
        enterpriseStaticBean.getVendorID + "\t" +
        enterpriseStaticBean.getVendorName + "\t" +
        enterpriseStaticBean.getCreatorID + "\t" +
        enterpriseStaticBean.getKeyContactName + "\t" +
        enterpriseStaticBean.getKeyContactPhone + "\t" +
        enterpriseStaticBean.getKeyContactEmail + "\t" +
        enterpriseStaticBean.getContactName + "\t" +
        enterpriseStaticBean.getContactPhone + "\t" +
        enterpriseStaticBean.getContactEmail + "\t" +
        enterpriseStaticBean.getContactIM + "\t" +
        enterpriseStaticBean.getEnterpriseAddress + "\t" +
        enterpriseStaticBean.getEnterpriseRemark + "\t" +
        enterpriseStaticBean.getAccountTotalAmount + "\t" +
        enterpriseStaticBean.getRunStatus + "\t" +
        enterpriseStaticBean.getRunStatusDesc + "\t" +
        enterpriseStaticBean.getIndustry1 + "\t" +
        enterpriseStaticBean.getIndustry2 + "\t" +
        enterpriseStaticBean.getIndustry3 + "\t" +
        enterpriseStaticBean.getIndustry1Value + "\t" +
        enterpriseStaticBean.getIndustry2Value + "\t" +
        enterpriseStaticBean.getIndustry3Value + "\t" +
        enterpriseStaticBean.getEnterpriseProvince + "\t" +
        enterpriseStaticBean.getEnterpriseProvinceDesc + "\t" +
        enterpriseStaticBean.getCity + "\t" +
        enterpriseStaticBean.getEnterpriseSource + "\t" +
        enterpriseStaticBean.getEnterpriseSourceDesc + "\t" +
        enterpriseStaticBean.getRegisteTime + "\t" +
        enterpriseStaticBean.getAppStartTime + "\t" +
        enterpriseStaticBean.getEnterpriseGroup + "\t" +
        enterpriseStaticBean.getEnterpriseGroupDesc + "\t" +
        enterpriseStaticBean.getCompanyScale + "\t" +
        enterpriseStaticBean.getCompanyScaleDesc + "\t" +
        enterpriseStaticBean.getEnterpriseType + "\t" +
        enterpriseStaticBean.getEnterpriseTypeDesc + "\t" +
        enterpriseStaticBean.getSkVersion + "\t" +
        enterpriseStaticBean.getSkBeginDate + "\t" +
        enterpriseStaticBean.getSkEndDate + "\t" +
        enterpriseStaticBean.getEnterpriseScale
    })
//    Hdfs.commonSaveText(resultEnterpriseStaticRDD, enterpriseStaticDailyOutPutPath, true)
//    Hdfs.commonSaveText(resultEnterpriseStaticRDD, enterpriseStaticOutPutPath, true)

    neweEnterpriseStaticRDD
  }

  //付费产品id列表
  val paidPid= Array(1,4,5,7,10,12,13,14,15,19,20,22,23,25,26)
  //非付费产品id列表
  val freePid= Array(2,3,7,8,11,16,17,18,21,24,-1)
  /**
    * 根据订单和退订单计算企业付费类型
    * @param eid
    * @param orders
    * @param refundsOption
    * @return
    */
  def getEnterpriseType(eid:Int,orders:Option[ListBuffer[Orders]],refundsOption:Option[ListBuffer[Refunds]],runDate:String):Int={
    if(!orders.isDefined){
      println("error enterprise id " + eid + "order is undefined please check")
    }else{
      val ordersList=orders.get
      //订单数为0则为免费
      if(ordersList.isEmpty){
        Properties.MF
      }else{
        //判断去除退订单后的订单数和订单中是否只有小微版本订单
        val (orderNums,flag)=orderNumsAndOnlyHaveSmallVersion(ordersList,refundsOption)
        if(orderNums==0 || flag){
          Properties.MF
        }else{
          val (paidOrderNums,paidAmount,isPast) =getPayContractAmount(ordersList,refundsOption,runDate)
          val trainingFee=getTrainingFee(ordersList)
          //检查培训费实施费是否大于0如果大于0则验证付费产品个数和合同金额
          if(trainingFee>0.0d){
            if(paidOrderNums==0 || paidAmount==0.0d){
              Properties.KY
            }else{
              if(isPast){
                Properties.WXF
              }else{
                Properties.FF
              }
            }
          }else{
            if(paidOrderNums==0 || paidAmount==0.0d){
              Properties.MF
            }else{
              if(isPast){
                Properties.WXF
              }else{
                Properties.FF
              }
            }
          }
        }
      }
    }
    Properties.WZ
  }

  /**
    * 获取培训费或实施费
    * @param orders
    * @return
    */
  def getTrainingFee(orders:ListBuffer[Orders]):Double={
    var fee = 0.0d
    orders.filter(order=>{order!=null&&(order.getProductId==3 || order.getProductId==24)})
      .foreach(order=>{fee+=order.getPurchaseAmount})
    fee
  }

  /**
    * 去掉所有退订单后的订单数和检测是否是只有小微版
    * @param orders
    * @param refundsOption
    * @return
    */
  def orderNumsAndOnlyHaveSmallVersion(orders:ListBuffer[Orders],refundsOption:Option[ListBuffer[Refunds]]):(Int,Boolean)={
    var flag=false
    var orderNums=0
    val orderIdSet = mutable.HashSet[(Int,Int)]()
    orders.filter(order => {
      order != null
    }).foreach(order => {
      orderIdSet.add((order.getOrderId,order.getProductId))
    })
    if(refundsOption.isDefined){
      val refunds=refundsOption.get
      refunds.filter(refund=>{
        refund!=null
      }).foreach(refund=>{
        if(orderIdSet.contains((refund.getOrderId, refund.getProductId))) {
          orderIdSet.remove((refund.getOrderId, refund.getProductId))
        }
      })
    }
    orderNums=orderIdSet.size
    if(orderNums==1){
      if(orderIdSet.head._2==freePid(8)){
        flag=true
      }
    }
    (orderNums,flag)
  }
  /**
    *
    * 付费产品订单数量 = 付费产品订单数 - 付费产品的退订数
    * 获取付费产品的金额 = 付费产品的金额 - 付费产品的退款金额
    * 订单是否过期 = 产品订单结束日期 > 查询日期
    * @param orders
    * @param refundsOption
    * @return
    */
  def getPayContractAmount(orders:ListBuffer[Orders],refundsOption:Option[ListBuffer[Refunds]],runDate:String):(Int,Double,Boolean)={
    var pFee = 0.0d
    var upFee = 0.0d
    var isPast = true
    val orderIdMap = mutable.HashMap[(Int,Int),Orders]()
    orders.filter(order=>{
      order!=null && paidPid.contains(order.getProductId)
    }).foreach(order=>{
      pFee+=order.getPurchaseAmount
      orderIdMap.put((order.getOrderId,order.getProductId),order)
    })

    if(refundsOption.isDefined) {
      val refunds=refundsOption.get
      refunds.filter(refund => {
        refund != null && orderIdMap.contains((refund.getOrderId, refund.getProductId))
      }).foreach(refund => {
        val o = orderIdMap.get((refund.getOrderId, refund.getProductId))
        //如果订单金额小于等于退款单金额则从有效订单中去掉，（小于几乎不可能吧）
        if (o.get.getPurchaseAmount <= refund.getSubRefundableTotalAmount) {
          orderIdMap.remove((refund.getOrderId, refund.getProductId))
        }
        upFee += refund.getSubRefundableTotalAmount
      })
    }
    import scala.util.control.Breaks._
    breakable{
      for(key <- orderIdMap.keySet){
        val order=orderIdMap.get(key)
        if(order.get.getPurchaseAmount>0.0d && order.get.getProductEndTime > runDate){
          isPast=false
          break
        }
      }
    }
    (orderIdMap.keySet.size,pFee-upFee,isPast)
  }
}
