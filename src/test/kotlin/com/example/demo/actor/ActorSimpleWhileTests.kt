package com.example.demo.actor

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import org.junit.jupiter.api.Test

class ActorSimpleWhileTests {

    open class CounterMsg(){}
    class MsgIncCounter: CounterMsg() // one-way message to increment counter
    class MsgGetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply


    fun CoroutineScope.counterActor() = actor<CounterMsg>{
        var counter = 0 // actor state

        while(true){
            println("channel?....")
            for (msg in channel) { // it
                println("\tnew msg")
                when (msg) {
                    is MsgIncCounter -> {
                        println("\tsleeping")
                        counter++

                    }
                    is MsgGetCounter -> {
                        msg.response.complete(counter)
                    }
                }
            }

            println('.')
        }
    }

    @Test
    fun `actor`(){
        runBlocking<Unit> {
            val actorCounter = counterActor() // create the actor

            println("sending")
            Thread.sleep(7000)
            actorCounter.send(MsgIncCounter())
            println("sending")
            Thread.sleep(7000)
            actorCounter.send(MsgIncCounter())
            println("sending")
            Thread.sleep(7000)
            actorCounter.send(MsgIncCounter())


            // val response = CompletableDeferred<Int>()
            // actorCounter.send(MsgGetCounter(response))
            // println("Counter = ${response.await()}")
            println("waiting a lot")
            Thread.sleep(7000)
            println("sending after long wait")
            actorCounter.send(MsgIncCounter())
            println("waiting a lot")
            Thread.sleep(7000)
            actorCounter.close() // shutdown the actor
            println("done")
            // OUTPUT:
            // Counter = 3
        }
    }
}
