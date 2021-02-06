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
    fun CoroutineScope.myactor() = actor<Msg> {
        for (message in channel) {
            when (message) {
                is MsgIncrement -> {
                    println("received")
                    number++
                }
            }
        }
    }

    suspend fun inc() {
        println("sending inc...")
        actor.send(MsgIncrement())
    }

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

// private fun CoroutineScope.myactor2() = actor<Msg> {
//     var number: Int = 0
//
//     for (message in channel) {
//         when (message) {
//             is MsgIncrement -> {
//                 number++
//             }
//             is MsgRetrieve -> {
//                 message.result.complete(number)
//             }
//         }
//     }
// }

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
