package com.bencassedy.readykafka

import com.bencassedy.readykafka.consumer.ReadyKafkaConsumer
import com.bencassedy.readykafka.producer.ReadyKafkaProducer
import com.bencassedy.readykafka.utils.JsonParser
import org.scalatest.FunSuite

/**
  * Integration test for producing to and consuming from docker kafka instance
  */
class TestProduceAndConsumeIT extends FunSuite {
  val topic = "test-records"
  val records = JsonParser.getRecords
  val producer = new ReadyKafkaProducer()
  val consumer = new ReadyKafkaConsumer(List(topic), "test-group")

  test("can produce and consume records from Kafka") {
    producer.produce(topic, records)
    consumer.consume()
  }
}
