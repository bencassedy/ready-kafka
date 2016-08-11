package com.bencassedy.readykafka.consumer

import java.util
import java.util.Properties
import java.util.concurrent.Executors

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}

/**
  * Created by bencassedy on 8/8/16.
  */
class ReadyKafkaConsumerRunnable(val id: Int) extends Runnable {
  val consumer = new KafkaConsumer[String, String](new Properties())

  override def run(): Unit = {
    consumer.subscribe(util.Arrays.asList("foo", "bar"))

    try {
      while (true) {
        val crs: ConsumerRecords[String, String] = consumer.poll(1000)
        crs.forEach { case cr: ConsumerRecord[String, String] =>
          // TODO: pass in function here that does something with the consumer record
        }
      }
    }
  }

  def shutdown(): Unit = {
    consumer.wakeup()
  }
}

class ReadyKafkaConsumer() {
  val numConsumers = 3
  val groupId = "kafka-consumer-group"
  val topics = util.Arrays.asList("foo", "bar")
  val executor = Executors.newFixedThreadPool(numConsumers)
  val consumers = 0 until numConsumers map { n =>
    new ReadyKafkaConsumerRunnable(n)
  }
//  int numConsumers = 3;
//  String groupId = "consumer-tutorial-group"
//  List<String> topics = Arrays.asList("consumer-tutorial");
//  ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
//
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
