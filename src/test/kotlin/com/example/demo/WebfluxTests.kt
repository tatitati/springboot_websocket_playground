package com.example.demo

import org.junit.jupiter.api.Test
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import java.util.function.BiFunction

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

    @Test
    fun `pragrammatic sequence`(){
        val publisher: Flux<String?> = Flux.generate(
                {0},
                { state: Int, sink: SynchronousSink<String?> ->
                    sink.next("3 x " + state + " = " + 3 * state)
                    if (state == 10) sink.complete()
                    state + 1
                }
        )

        publisher.subscribe(
                { item: String? -> println(item)}
        )

        // OUTPUT
        // 3 x 0 = 0
        // 3 x 1 = 3
        // 3 x 2 = 6
        // 3 x 3 = 9
        // 3 x 4 = 12
        // 3 x 5 = 15
        // 3 x 6 = 18
        // 3 x 7 = 21
        // 3 x 8 = 24
        // 3 x 9 = 27
        // 3 x 10 = 30
    }


}
