package com.example.demo.reactorKafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.junit.jupiter.api.Test
import reactor.core.Disposable
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.receiver.ReceiverRecord
import java.util.*


class ReactorKafkaTests {

    @Test
    fun `asfasdf`(){
        val topic = "atopic"
        val properties = Properties().apply{
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
            put(ConsumerConfig.GROUP_ID_CONFIG, "bbbc")
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest") // by default
        }

        val receiverOptions: ReceiverOptions<Int, String> = ReceiverOptions.create(properties);

        val options: ReceiverOptions<Int, String> = receiverOptions.subscription(Collections.singleton(topic))
        val kafkaFlux = KafkaReceiver.create(options).receive()
        val consumer: Disposable =  kafkaFlux.subscribe { record: ReceiverRecord<Int, String> ->
            val offset = record.receiverOffset()
            System.out.printf("Received message: topic-partition=%s offset=%d key=%d value=%s\n",
                    offset.topicPartition(),
                    offset.offset(),
                    record.key(),
                    record.value())
            offset.acknowledge()
        }
    }
}
