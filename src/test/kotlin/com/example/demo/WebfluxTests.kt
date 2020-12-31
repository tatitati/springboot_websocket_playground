package com.example.demo

import org.junit.jupiter.api.Test
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import java.util.function.BiFunction
import java.util.concurrent.atomic.AtomicLong

import org.apache.coyote.http11.Constants.a
import reactor.core.publisher.FluxSink
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.function.LongConsumer


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
    fun `SINK pragrammatic sequence`(){
        data class Person(val uuid: String)


        val publisher: Flux<Person?> = Flux.generate(
                {0},
                { state, sink: SynchronousSink<Person?> ->
                    sink.next(Person(UUID.randomUUID().toString()))
                    if (state == 10) sink.complete()
                    state+1
                }
        )

        publisher.subscribe(
                { item: Person? -> println(item)}
        )

        // OUTPUT
        // Person(uuid=43364d87-499b-4376-b61b-1eb7d287dbc6)
        // Person(uuid=511b8726-6acb-4fd1-83de-94bc99dd4fe1)
        // Person(uuid=32a3678f-1095-4741-92c4-72dc16d72ac4)
        // Person(uuid=c4985143-66b7-41e9-b093-e5bcd00e56c6)
        // Person(uuid=c5c1b420-0340-4185-aa90-ed55c6e37a78)
        // Person(uuid=483f891d-394f-45c2-9981-9e5aa23c133b)
        // Person(uuid=e6acbd0e-83a7-435e-9e9e-30355a06bf54)
        // Person(uuid=15072ffa-97ec-4b95-8dcf-7252b52486ea)
        // Person(uuid=3496144a-c7d0-4f84-a27a-62461485f91a)
        // Person(uuid=c9de8541-5bfa-44eb-a54b-fb1565ca456b)
        // Person(uuid=16327883-33ea-421a-9fa3-7a5a12c612c1)
    }

    @Test
    fun `SINK with a consumer`(){
        val publisher = Flux.generate({ AtomicLong() },
                { state: AtomicLong, sink: SynchronousSink<String?> ->
                    val i = state.getAndIncrement()
                    sink.next("3 x " + i + " = " + 3 * i)
                    if (i == 10L) sink.complete()
                    state
                }) { state: AtomicLong -> println("state: $state") } // on complete function

        publisher.subscribe(
                { item: String? -> println(item)}
        )
    }

    @Test
    fun `create`() {
        val publisher = Flux.create { sink: FluxSink<Any?> ->
            val words = listOf("aaaa", "bbbbbb")
            for (word in words){
                sink.next(word + "!!!")
            }
            sink.complete()
        }

        publisher.subscribe({item -> println(item)})

        // OUTPU:
        // aaaa!!!
        // bbbbbb!!!
    }
}
