package com.fxiaoke.fhc.utils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.DataFrame

/**
  * Created by jief on 2017/3/21.
  */
object HdfsHelper {

  /**
    * data frame 存储parquet
    * @param saveDF  data frame
    * @param outputPath 输出目录
    * @param repartition 是否重分区
    */
  def saveAsParquet(saveDF: DataFrame, outputPath: String,repartition:Int=1):Unit ={
      // 如果输出路径存在,删除
      val fs = FileSystem.newInstance(new Configuration())
      val path = new Path(outputPath)
      if (fs.exists(path)){
          fs.delete(path, true)
      }
      fs.close()
      saveDF.coalesce(repartition).write.format("parquet").save(outputPath)
  }


}
