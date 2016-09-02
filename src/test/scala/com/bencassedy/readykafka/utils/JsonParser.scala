package com.bencassedy.readykafka.utils

import play.api.libs.json.{JsArray, Json, JsValue}

import scala.io.Source

/**
  * Utility script for parsing sample json input
  */
object JsonParser {
  val fileContents = Source.fromFile("src/test/resources/consumer_complaints.json").getLines.mkString
  val json = Json.parse(fileContents).asInstanceOf[JsArray]
  def getRecords: Iterable[String] = json.value.map(_.toString())
}