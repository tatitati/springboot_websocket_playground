package com.example.demo

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Test
import reactor.core.publisher.EmitterProcessor
import java.time.Duration
import java.util.*
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.scheduler.Scheduler


class FluxConsumerTests{
    fun buildConsumer(): KafkaConsumer<String, String> {
        val properties = Properties().apply{
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
            put(ConsumerConfig.GROUP_ID_CONFIG, "bbbc")
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest") // by default
        }

        val consumer = KafkaConsumer<String, String>(properties)
        consumer.subscribe(listOf("mystream"))
        return consumer
    }

    val consumer = buildConsumer()

    @Test
    fun`Flux with create`(){
        var publisher: Flux<ConsumerRecord<String, String>> = Flux.create { sink: FluxSink<ConsumerRecord<String, String>> ->
            while(true){
                val records = consumer.poll(Duration.ofSeconds(1))
                for (record in records) {
                    sink.next(record!!)
                }
            }
        }

        publisher.subscribe({item ->
            println(item)
        })

        println("the while(true){..} is actually executed on subscribe(), so this line is never appearing as the publisher is blocking")

        // DOC:
        // In Reactor, when you write a Publisher chain, data does not start pumping into it by default. Instead, you
        // create an abstract description of your asynchronous process (which can help with reusability and composition).
        // By the act of subscribing, you tie the Publisher to a Subscriber, which triggers the flow of data in the whole chain.
    }

    @Test
    fun `flux with push`(){
        var publisher: Flux<ConsumerRecord<String, String>> = Flux.push { sink: FluxSink<ConsumerRecord<String, String>> ->
            while(true){
                val records = consumer.poll(Duration.ofSeconds(1))
                for (record in records) {
                    sink.next(record!!)
                }
            }
        }

        publisher.subscribe({item -> println(item)})
        println("is this previous line blocking?: Yes")
    }
}
