package com.example.demo.actor.myactor

import com.example.demo.actor.ActorSimpleTests
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor

// https://github.com/lgwillmore/klab/blob/master/src/main/kotlin/com/binarymonks/klab/actors/ooactors.kt

class Account : CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.Unconfined + job
    private val actor = accountActor()

    suspend fun getBalance(): Double {
        val result = CompletableDeferred<Double>()
        actor.send(AccountActorMsg.GetBalance(result))
        return result.await()
    }
}

private fun CoroutineScope.accountActor() = actor<AccountActorMsg> {

    // This is the state that we want to protect - keep safe from concurrent modification
    var balance = 0.0

    for (message in channel) {
        when (message) {
            is AccountActorMsg.GetBalance -> {
                message.result.complete(balance)
            }
        }
    }
}

private sealed class AccountActorMsg {
    data class GetBalance(val result: CompletableDeferred<Double>) : AccountActorMsg()
}

fun main() = runBlocking {
    val myAccount = Account()
    println(myAccount.getBalance())
}
