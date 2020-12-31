package com.example.demo

import org.junit.jupiter.api.Test
import reactor.core.Disposable
import reactor.core.publisher.Flux
import java.time.Duration

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

    @Test
    fun `basic test3`(){
        val publisher: Flux<String> = Flux.just("aaa", "bbbb", "cccc", "dddd", "eeee")
        val subscription: Disposable = publisher.subscribe(
                { item: String -> println(item)},
                {e -> println(e)},
                {println("completed")},
                {sub -> sub.request(3)}
        )
        //OUTPUT:
        // aaa
        // bbbb
        // cccc
    }

    @Test
    fun `inifinite flux1`(){
        val publisher: Flux<Long> = Flux.interval(Duration.ofSeconds(2))
        val subscription: Disposable = publisher.subscribe(
                { item: Long -> println(item)}
        )

        Thread.sleep(10000)

        //OUTPUT:
        // 0
        // 1
        // 2
        // 3
        // ...
    }


}
