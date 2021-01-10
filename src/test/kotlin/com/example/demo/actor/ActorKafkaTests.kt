package com.example.demo.actor

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.*
import kotlin.concurrent.thread

class ActorKafkaTests {
    fun buildConsumer(): KafkaConsumer<String, String> {
        val properties = Properties().apply{
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
            put(ConsumerConfig.GROUP_ID_CONFIG, "bbbc")
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest") // by default
        }

        val consumer = KafkaConsumer<String, String>(properties)
        consumer.subscribe(listOf("mystream"))
        return consumer
    }

    val consumer = buildConsumer()


    open class MsgKafka(){}
    object MsgStart: MsgKafka()
    object MsgPause: MsgKafka()
    object MsgResume: MsgKafka()

    fun CoroutineScope.actorKafka(kafkaConsumer: Consumer<String, String>) = actor<MsgKafka> {
        var desiredStated: MsgKafka? = null
        for (msg in channel) { // iterate over incoming messages
            desiredStated = msg
        }

        thread{
            while(true){
                when (desiredStated) {
                    is MsgStart -> {
                        val records = kafkaConsumer.poll(Duration.ofSeconds(1L))
                        println(records)
                        for (record in records){
                            println(record)
                        }
                    }
                    is MsgPause -> {
                        println("pausing...")
                        kafkaConsumer.pause(kafkaConsumer.assignment())
                    }
                    is MsgResume -> {
                        println("resuming...")
                        kafkaConsumer.resume(kafkaConsumer.assignment())
                    }
                }
            }
        }
    }


    @Test
    fun `my test`(){
        runBlocking<Unit> {
            val actorKafka = actorKafka(consumer) // create the actor
            withContext(Dispatchers.Default) {
                actorKafka.send(MsgStart)
                delay(50000)

                println("trying to pause")
                actorKafka.send(MsgPause)
                delay(50000)

                println("trying to resume")
                actorKafka.send(MsgResume)
            }
        }

    }
}
