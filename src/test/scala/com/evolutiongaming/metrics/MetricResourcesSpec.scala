package com.evolutiongaming.metrics

import akka.http.scaladsl.coding.{Gzip, _}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpEncodings._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.matchers.Matcher
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class MetricResourcesSpec extends FunSuite with Matchers with Directives with ScalatestRouteTest {

  test("Dispatches 404") {
    val resources = new MetricResources(Report("test example"))

    Get("/metrics") ~> resources.route ~> check {
      handled shouldBe true
    }

    Get("/metricus") ~> resources.route ~> check {
      handled shouldBe false
    }
  }

  test("Handles metrics") {
    val report = Report("test example")
    val resources = new MetricResources(report)

    Get("/metrics") ~> resources.route ~> check {
      response should haveContentEncoding(gzip)
      
      decompressBody shouldEqual report.text
    }
  }

  private def decompressBody = {
    Await.result(decompress(responseAs[HttpResponse], Gzip).entity.toStrict(1.second) map (_.data) map (_.utf8String), 1.second)
  }

  def haveContentEncoding(encoding: HttpEncoding): Matcher[HttpResponse] =
    be(Some(`Content-Encoding`(encoding))) compose {(_: HttpResponse).header[`Content-Encoding`]}

  def strictify(entity: HttpEntity): HttpEntity.Strict = Await.result(entity.toStrict(1.second), 1.second)

  def compress(input: String, encoder: Encoder): ByteString = {
    val compressor = encoder.newCompressor
    compressor.compressAndFlush(ByteString(input)) ++ compressor.finish()
  }

  def decompress(input: HttpResponse, decoder: Decoder) = {
    decoder.decodeMessage(input)
  }
}
