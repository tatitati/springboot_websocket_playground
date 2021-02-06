package com.example.demo.actor.myactor

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.*

// https://github.com/lgwillmore/klab/blob/master/src/main/kotlin/com/binarymonks/klab/actors/ooactors.kt

class CounterRegistry : CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.Unconfined + job
    private val actor = myactor()
    var number = 0
    // writes through the actor
    fun CoroutineScope.myactor() = actor<Msg> {
        for (message in channel) {
            when (message) {
                is MsgIncrement -> {
                    println("received")
                    number++
                } else -> whatever()
            }
        }
    }

    fun whatever(){
        println("hi")
    }

    suspend fun inc() {
        println("sending inc...")
        actor.send(MsgIncrement())
    }

    // reads directly from the value, not needed through the actor
    fun getNumber(){
        println(number)
    }

    // suspend fun getNumber() {
    //     val result = CompletableDeferred<Int>()
    //     actor.send(MsgRetrieve(result))
    //     val finalValue = result.await()
    //     println(finalValue)
    // }
}

open class Msg
class MsgIncrement: Msg()
class MsgRetrieve(val result: CompletableDeferred<Int>): Msg()

fun main() = runBlocking {
    val counterRegistry = CounterRegistry()
    counterRegistry.inc()
    counterRegistry.inc()
    counterRegistry.inc()
    counterRegistry.getNumber()
}
