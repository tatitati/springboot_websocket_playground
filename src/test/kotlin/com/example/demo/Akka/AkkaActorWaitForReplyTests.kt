package com.example.demo.Akka

import akka.actor.AbstractLoggingActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.pattern.Patterns.ask;
import akka.actor.Props
import akka.dispatch.OnComplete
import akka.japi.pf.ReceiveBuilder
import org.junit.jupiter.api.Test
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class AkkaActorWaitForReplyTests {
    class HelloKotlinActor : AbstractLoggingActor() {
        override fun createReceive(): Receive? {
            return ReceiveBuilder().match(String::class.java) {
                println("\tReceived: " + it)
                Thread.sleep(2000)
                sender.tell("hello, this is the reply from the actor!", self)
            }.build()
        }
    }

    @Test
    fun `whatever`(){
        val actorSystem = ActorSystem.create("part1")

        val actorRef = actorSystem.actorOf(Props.create(HelloKotlinActor::class.java))

        println("Sending: Hello Kotlin")
        val futureResponse: scala.concurrent.Future<Any>? = ask(actorRef, "Hello Kotlin", 5000)
        val response = Await.result(futureResponse, Duration.apply(10, TimeUnit.SECONDS))

        println(response)
    }
}
