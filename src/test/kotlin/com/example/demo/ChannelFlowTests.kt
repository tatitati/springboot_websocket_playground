package com.example.demo

import org.junit.jupiter.api.Test
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class ChannelFlowTests {

    @Test
    fun `channel1`(){
        fun main() = runBlocking {
            val channel = Channel<Int>()
            launch {
                for (x in 1..5) channel.send(x * x)
            }

            repeat(5) {
                Thread.sleep(2000)
                println(channel.receive())
            }
            println("Done!")
        }

        main()
        // 1
        // 4
        // 9
        // 16
        // 25
    }

    @Test
    fun `flow1`(){

    }
}
