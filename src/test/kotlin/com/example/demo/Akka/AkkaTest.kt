package com.example.demo.Akka

import org.junit.jupiter.api.Test
import akka.actor.AbstractLoggingActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.japi.pf.ReceiveBuilder

class AkkaTest {

    class HelloKotlinActor : AbstractLoggingActor() {
        override fun createReceive(): Receive? {
            return ReceiveBuilder().match(String::class.java) {
                println("\tReceived: " + it)
            }.build()
        }
    }

    @Test
    fun `whatever`(){
        val actorSystem = ActorSystem.create("part1")

        val actorRef = actorSystem.actorOf(Props.create(HelloKotlinActor::class.java))

        println("Sending: Hello Kotlin")
        actorRef.tell("Hello Kotlin", ActorRef.noSender())
        actorRef.tell("Bye Kotlin", ActorRef.noSender())
    }
}
