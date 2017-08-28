package com.practice.urlshortener

import com.practice.dbhelper.DBHelper
import org.slf4j.{Logger, LoggerFactory}

object URLShortener extends App{
  val HEADER = "www.shorten.com"
  val logger: Logger = LoggerFactory.getLogger(getClass)
  if(args.length < 2){
    logger.error("Usage: URLShortener.jar -s www.google.com or URLShortener.jar -l A1B2c3")
    System.exit(-1)
  }

  val dbHelper = new DBHelper
  val shortener = new URLShortener

  if(args(0) == "-s"){
    //call shortening API
    logger.info("Checking if this long url is in our db...")
    val shortUrl = dbHelper.getShortUrlFromLongUrl(args(1))
    if(shortUrl != ""){
      logger.info("Found the corresponding short url: " + shortUrl)
      print(HEADER + "/" + shortUrl)
    }else{
      logger.info("Given long url not found in the db, so update it...")
      if(dbHelper.updateLongUrls(args(1)) != 1){
        logger.error("Error persisting the long url")
        System.exit(-1)
      }

      val id = dbHelper.getIdFromLongUrl(args(1))
      val sUrl: String = shortener.idToShortURL(id)
      if(dbHelper.updateShortUrls(sUrl, args(1)) != 1){
        logger.error("Error persisting the shortened url")
        System.exit(-1)
      }
      logger.info("Url " + args(1) + " has been persisted, the corresponding short url is " + sUrl)
      print(sUrl)
    }
  }else if(args(0) == "-l"){
    //get long url from given short url
    logger.info("Checking if this short url is in our db...")
    val longUrl = dbHelper.getLongUrlFromShortUrl(args(1))
    if(longUrl != ""){
      logger.info("Found the corresponding long url: " + longUrl)
      print(longUrl) //output to stdout
    }else{
      logger.error("The corresponding long url was NOT found!")
    }
  }
}

class URLShortener {
  val STR = "abcedfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
  val SCALE: Int = 61

  def extractShortURL(fullURL: String): String = {
    val pattern = """(?<=\/)[a-zA-Z1-9]*\b""".r //match everything in the first segment
    pattern.findFirstIn(fullURL).getOrElse("")
  }

  def constructURL(shortURL: String): String = {
    HEADER + "/" + shortURL
  }

  def idToShortURL(id: Long): String = {
    //convert from Long type id to short URL
    var value: Long = id
    val sb: StringBuilder = new StringBuilder
    while(value > 0L){
      val remainder: Long = value % SCALE
      sb.insert(0, long2Char(remainder))
      value = value / SCALE
    }
    if(sb.length < 6){
      //append 0 to the front to pad the string
      val charArr = Array.fill[Char](6 - sb.length)('0')
      sb.insertAll(0, charArr)
    }
    sb.toString()
  }

  def shortURLToId(shortURL: String): Long = {
    //convert from short URL to the Long type id
    var j = 0
    var res = 0L
    for(i <- 0 until shortURL.length){
      val tmp = scala.math.pow(SCALE, j).toLong
      res = res + char2Int(shortURL.charAt(shortURL.length - i - 1)) * tmp
      j = j + 1
    }
    res
  }

  def char2Int(c: Char): Int = {
    if(c == '0'){
      return 0
    }

    if(c >= 'a' && c <= 'z'){
      return c - 'a'
    }else if(c >= 'A' && c <= 'Z'){
      return 26 + (c - 'A')
    }else{
      return 52 + (c - '1')
    }
  }

  def long2Char(l: Long): Char = {
    STR.charAt(l.toInt)
  }
}
