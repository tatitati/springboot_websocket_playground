package com.example.demo

import org.junit.jupiter.api.Test
import reactor.core.Disposable
import reactor.core.publisher.Flux

class WebfluxTests {

    @Test
    fun `basic test`(){
        val publisher: Flux<String> = Flux.just("aaa", "bbbb")
        val subscription: Disposable = publisher.subscribe{ item: String ->
            println(item)
        }

        //OUTPUT:
        // aaa
        // bbbb
    }

    @Test
    fun `basic test2`(){
        val publisher: Flux<String> = Flux.just("aaa", "bbbb")
        val subscription: Disposable = publisher.subscribe(
                { item: String -> println(item)},
                {e -> println(e)},
                {println("completed")}
        )
        //OUTPUT:
        // aaa
        // bbbb
        // completed
    }
}
