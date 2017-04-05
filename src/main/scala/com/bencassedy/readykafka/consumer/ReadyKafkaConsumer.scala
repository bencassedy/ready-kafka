package com.bencassedy.readykafka.consumer

import java.util
import java.util.Properties
import java.util.concurrent.Executors
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}

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

  class ReadyKafkaConsumerRunnable(_topics: util.List[String], val id: Int) extends Runnable {
    val consumer = new KafkaConsumer[String, String](consumerProperties)

    override def run(): Unit = {
      consumer.subscribe(_topics)
      try {
        while (true) {
          val crs: ConsumerRecords[String, String] = consumer.poll(1000)
          crs.iterator().asScala.foreach(r => msgFunc(r))
        }
      }
      finally {
        consumer.close()
      }
    }

    def shutdown(): Unit = {
      consumer.wakeup()
    }
  }


  def consume(): Unit = {
    val numConsumers = 2
    val pool = Executors.newFixedThreadPool(numConsumers)
    val consumers = 0 until numConsumers map { n =>
      new ReadyKafkaConsumerRunnable(topics.asJava, n)
    }
    consumers.foreach(pool.submit(_))
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        consumers.foreach(_.shutdown())
        pool.shutdown()
      }
    })
  }
}
