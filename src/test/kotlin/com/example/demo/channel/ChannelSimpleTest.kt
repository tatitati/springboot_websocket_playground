package com.example.demo.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.junit.jupiter.api.Test

class ChannelSimpleTest {

    @Test
    fun `adsfasdf`() = runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
        }
        // here we print five received integers:
        repeat(5) { println(channel.receive()) }
        println("Done!")
    }
}
