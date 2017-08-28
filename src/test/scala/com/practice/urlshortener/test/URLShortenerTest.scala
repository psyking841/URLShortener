package com.practice.urlshortener.test
import com.practice.urlshortener.URLShortener
import org.scalatest.{FlatSpec, ShouldMatchers}

class URLShortenerTest extends FlatSpec with ShouldMatchers{
  val fullURL = "www.shorten.com/A1b2C3"
  val shortener = new URLShortener
  val res = shortener.extractShortURL(fullURL)
  res should equal("A1b2C3")

  val res2 = shortener.idToShortURL(1235L)
  res2 should equal("0000up")

  val res3 = shortener.shortURLToId("0000up")
  res3 should equal(1235L)
}
