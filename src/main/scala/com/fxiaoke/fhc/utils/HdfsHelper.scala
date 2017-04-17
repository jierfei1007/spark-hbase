package com.fxiaoke.fhc.utils


import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs._
import org.apache.hadoop.util.ToolRunner
import org.apache.spark.rdd.RDD
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


  def save2Text(rdd: RDD[String], filePath: String) = {
    println("-------------- save to text - begin -------------------")
    rdd.repartition(1).saveAsTextFile(filePath)
    println("-------------- save to text- end -------------------")
  }

  def commonSaveText(rdd: RDD[String], outputPath: String, toOverWrite: Boolean) = {
    if (toOverWrite) {
      // 如果输出路径存在,删除
      val fs = FileSystem.newInstance(new Configuration())
      val path = new Path(outputPath)
      if (fs.exists(path)) {
        fs.delete(path, true)
      }
    }
    if (System.getProperty("os.name").toLowerCase.startsWith("win")){
      rdd.top(10)
    }
    save2Text(rdd, outputPath)
  }

  /**
    * 拷贝文件到目标目录中
    * @param srcs 源文件
    * @param target 目标目录 dir
    */
  def cp(srcs:Array[String],target:String): Unit ={
    val argv=Array[String]("-cp","-f")
    val argv1 =argv ++ srcs
    val argv2 = argv1 :+ target
    val conf: Configuration = new Configuration
    val shell: FsShell = new FsShell(conf)
    conf.setQuietMode(false)
    shell.setConf(conf)
    var res: Int = 0
    try {
      res = ToolRunner.run(shell, argv2)
    }finally {
      shell.close()
    }
  }

  /**
    * 判断文件或目录是否存在
    * 如果存在则删除
    * @param filePath
    */
  def delteIfExists(filePath:String): Unit ={
    val fs = FileSystem.newInstance(new Configuration())
    val path = new Path(filePath)
    if (fs.exists(path)) {
      fs.delete(path, true)
    }
  }

  /**
    * 创建目录
    * @param dir
    */
  def mkdir(dir:String): Unit ={
    val fs = FileSystem.newInstance(new Configuration())
    val path = new Path(dir)
    if (!fs.exists(path)) {
      fs.mkdirs(path)
    }
  }
}
