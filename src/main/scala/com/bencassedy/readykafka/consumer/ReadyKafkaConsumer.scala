package com.bencassedy.readykafka.consumer

import java.util
import java.util.Properties
import java.util.concurrent.Executors
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import scala.collection.JavaConverters._

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}

/**
  * The high-level and per-thread implementations of a Kafka consumer group
  */
class ReadyKafkaConsumer(topics: List[String], groupId: String, brokerList: String = "0.0.0.0:9092") {
  val consumerProperties = new Properties()
  consumerProperties.put("bootstrap.servers", brokerList)
  consumerProperties.put("key.serializer", classOf[StringSerializer])
  consumerProperties.put("value.serializer", classOf[StringSerializer])
  consumerProperties.put("key.deserializer", classOf[StringDeserializer])
  consumerProperties.put("value.deserializer", classOf[StringDeserializer])
  class ReadyKafkaConsumerRunnable(_topics: util.List[String], val id: Int) extends Runnable {
    val consumer = new KafkaConsumer[String, String](consumerProperties)

    override def run(): Unit = {
      consumer.subscribe(_topics)
      try {
        while (true) {
          val crs: ConsumerRecords[String, String] = consumer.poll(1000)
//          crs.forEach { case cr: ConsumerRecord[String, String] =>
//            // TODO: pass in function here that does something with the consumer record
//            println(cr)
//          }
          crs.iterator().asScala.foreach(println)
        }
      } finally {
        consumer.close()
      }
    }

    def shutdown(): Unit = {
      consumer.wakeup()
    }
  }

  val numConsumers = 3
  val executor = Executors.newFixedThreadPool(numConsumers)
  val consumers = 0 until numConsumers map { n =>
    new ReadyKafkaConsumerRunnable(topics.asJava, n)
  }

  def consume(): Unit = {
    consumers.foreach(executor.submit(_))
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        consumers.foreach(_.shutdown())
        executor.shutdown()
      }
    })
  }



  //  final List<ConsumerLoop> consumers = new ArrayList<>();
  //  for (int i = 0; i < numConsumers; i++) {
  //    ConsumerLoop consumer = new ConsumerLoop(i, groupId, topics);
  //    consumers.add(consumer);
  //    executor.submit(consumer);
  //  }
  //
  //  Runtime.getRuntime().addShutdownHook(new Thread() {
  //    @Override
  //    public void run() {
  //      for (ConsumerLoop consumer : consumers) {
  //        consumer.shutdown();
  //      }
  //      executor.shutdown();
  //      try {
  //        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
  //      } catch (InterruptedException e) {
  //        e.printStackTrace;
  //      }
  //    }
  //  });
}
