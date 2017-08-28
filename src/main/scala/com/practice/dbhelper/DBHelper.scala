package com.practice.dbhelper

import java.sql.{DriverManager, ResultSet, Statement}

import org.slf4j.{Logger, LoggerFactory}

class DBHelper{
  val logger: Logger = LoggerFactory.getLogger(getClass)

  private val con_str = "jdbc:postgresql://192.168.1.210:5432/shorternurldb?user=span&password=msu1234"
  private val conn = DriverManager.getConnection(con_str)
  val stm: Statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

  //println(getShortUrlFromLongUrl("www.google.com"))

  //println(getShortUrlFromId(1))

  //println(updateLongUrls("www.cnn.com"))

  //println(updateShortUrls("00000g", "www.cnn.com"))


  def getIdFromLongUrl(longUrl: String): Long = {
    //check if given url exist
    //val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    val rs = stm.executeQuery(s"SELECT * FROM urls WHERE url = '$longUrl'")
    if(rs.next) {
      val res: Long = rs.getString("id").toLong
      return res
    }else{
      return -1
    }
  }

  def getShortUrlFromLongUrl(longUrl: String): String = {
    //get short URL from long URL
    //val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    val rs = stm.executeQuery(s"SELECT * FROM shortened_urls WHERE long_url = '$longUrl'")
    if(rs.next) {
      rs.getString("shortened_url")
    }else{
      ""
    }
  }

  def getLongUrlFromShortUrl(shortUrl: String): String = {
    //get long URL from short URL
    //val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    val rs = stm.executeQuery(s"SELECT * FROM shortened_urls WHERE shortened_url = '$shortUrl'")
    if(rs.next) {
      rs.getString("long_url")
    }else{
      ""
    }
  }

  def updateLongUrls(longUrl: String): Int = {
    //update urls table with a new url
    //val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    stm.executeUpdate(s"INSERT INTO urls (url) VALUES ('$longUrl')")
  }

  def updateShortUrls(shortUrl: String, longUrl: String): Int = {
    //update urls table with a new url
    //val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    stm.executeUpdate(s"INSERT INTO shortened_urls (shortened_url, long_url) VALUES ('$shortUrl', '$longUrl')")
  }

  def closeConnection(): Unit ={
    conn.close()
  }
}
