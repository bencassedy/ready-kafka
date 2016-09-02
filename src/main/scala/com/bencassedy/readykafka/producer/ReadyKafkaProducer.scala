package com.bencassedy.readykafka.producer

import java.util.Properties

import org.apache.kafka.clients.producer.{ProducerRecord, KafkaProducer}
import org.apache.kafka.common.serialization.{StringSerializer, StringDeserializer}

/**
  * Kafka Producer Class
  */
class ReadyKafkaProducer {
  case class KafkaProducerConfigs(brokerList: String = "0.0.0.0:9092") {
    val properties = new Properties()
    properties.put("bootstrap.servers", brokerList)
    properties.put("key.serializer", classOf[StringSerializer])
    properties.put("value.serializer", classOf[StringSerializer])
    properties.put("key.deserializer", classOf[StringDeserializer])
    properties.put("value.deserializer", classOf[StringDeserializer])
//    properties.put("serializer.class", classOf[StringDeserializer])
//    properties.put("batch.size", 16384)
//    properties.put("linger.ms", 1)
//    properties.put("buffer.memory", 33554432)
  }

  val producer = new KafkaProducer[String, String](KafkaProducerConfigs().properties)

  def produce(topic: String, messages: Iterable[String]): Unit = {
    messages.foreach { m =>
      producer.send(new ProducerRecord[String, String](topic, m))
    }
    producer.close()
  }
}
