package com.example.demo.actor

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import org.junit.jupiter.api.Test

class ActorSimpleTests {

    open class CounterMsg(){}
    object MsgIncCounter: CounterMsg() // one-way message to increment counter
    class MsgGetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply

    fun CoroutineScope.counterActor() = actor<CounterMsg> {
        var counter = 0 // actor state
        for (msg in channel) { // iterate over incoming messages
            when (msg) {
                is MsgIncCounter -> counter++
                is MsgGetCounter -> msg.response.complete(counter)
            }
        }
    }

    @Test
    fun `actor`(){
        runBlocking<Unit> {
            val actorCounter = counterActor() // create the actor
            withContext(Dispatchers.Default) {
                actorCounter.send(MsgIncCounter)
                actorCounter.send(MsgIncCounter)
                actorCounter.send(MsgIncCounter)
            }

            val response = CompletableDeferred<Int>()
            actorCounter.send(MsgGetCounter(response))
            println("Counter = ${response.await()}")
            actorCounter.close() // shutdown the actor

            // OUTPUT:
            // Counter = 3
        }
    }
}
