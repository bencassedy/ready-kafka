package com.bencassedy.readykafka.consumer

import java.util
import java.util.Properties
import java.util.concurrent.Executors
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * The high-level and per-thread implementations of a Kafka consumer group
  */
class ReadyKafkaConsumer[A](
                             topics: List[String],
                             groupId: String,
                             brokerList: String = "localhost:9092",
                             msgFunc: (ConsumerRecord[String, String]) => A
                           ) {
  val consumerProperties = new Properties()
  consumerProperties.put("bootstrap.servers", brokerList)
  consumerProperties.put("group.id", groupId)
  consumerProperties.put("auto.offset.reset", "earliest")
  consumerProperties.put("key.deserializer", classOf[StringDeserializer])
  consumerProperties.put("value.deserializer", classOf[StringDeserializer])

  def consume(): Unit = {
    val numConsumers = 2
    0 until numConsumers foreach { n =>
      val f = Future {
        val consumer = new KafkaConsumer[String, String](consumerProperties)
        consumer.subscribe(topics.asJava)
        while (true) {
          val crs: ConsumerRecords[String, String] = consumer.poll(1000)
          crs.iterator().asScala.foreach(r => msgFunc(r))
        }
      }

      f onComplete {
        case Success(_) => println("success! consumer future shutting down")
        case Failure(e) => println(s"failure! bah! exception: $e")
      }
    }
  }
}
