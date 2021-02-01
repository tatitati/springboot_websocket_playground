package com.example.demo.flow


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test

class FlowTests {

    fun simple(): Flow<Int> = flow { // flow builder
        for (i in 1..100) {
            delay(100) // pretend we are doing something useful here
            emit(i) // emit next value
        }
    }

    @Test
    fun `my flow`() = runBlocking<Unit> {
        // Launch a concurrent coroutine to check if the main thread is blocked
        launch {
            for (k in 1..100) {
                println("I'm not blocked $k")
                delay(100)
            }
        }
        // Collect the flow
        simple().collect { value -> println(value) }
        println("done")
    }
}
