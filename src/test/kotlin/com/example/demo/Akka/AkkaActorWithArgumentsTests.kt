package com.example.demo.Akka

import akka.actor.AbstractLoggingActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.japi.pf.ReceiveBuilder
import org.junit.jupiter.api.Test
import kotlin.concurrent.thread

class AkkaActorWithArgumentsTests {
    class MyActor(val firstname: String, val age: Int) : AbstractLoggingActor() {

        override fun preStart() {
            super.preStart()
            println("\tpreStart(): Starting Child Actor")
            thread{
                while(true){
                    println("name: ${this.firstname}, age: ${this.age}")
                    Thread.sleep(300)
                }
            }
        }

        override fun createReceive(): Receive? {
            return ReceiveBuilder().match(String::class.java){ message: String ->
                when (message) {
                    "DIE" -> {
                        println("killing")

                    }
                    else -> {
                        println(message)
                    }
                }
            }
                    .build()
        }
    }

    @Test
    fun `adsfads`(){
        val actorSystem = ActorSystem.create("part2")
        val actorRef = actorSystem.actorOf(Props.create(MyActor::class.java, "francisco", 34))
        Thread.sleep(1000)
        println("Sending: Hello Kotlin")
        actorRef.tell("Hello Kotlin", ActorRef.noSender())
        Thread.sleep(1000)
        println("Sending: DIE")
        Thread.sleep(1000)
        actorRef.tell("DIE", ActorRef.noSender())
        Thread.sleep(5000)
        println("DONE")
    }
}
