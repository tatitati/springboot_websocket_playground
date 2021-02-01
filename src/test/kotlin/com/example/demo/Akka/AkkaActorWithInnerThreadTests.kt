package com.example.demo.Akka

import akka.actor.AbstractLoggingActor
import akka.actor.ActorInitializationException
import akka.actor.ActorKilledException
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.AllForOneStrategy
import akka.actor.DeathPactException
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.japi.pf.ReceiveBuilder
import org.junit.jupiter.api.Test
import scala.concurrent.duration.Duration
import kotlin.concurrent.thread


class AkkaActorWithInnerThreadTests {
    class MyActor : AbstractLoggingActor() {

        override fun preStart() {
            super.preStart()
            println("\tpreStart(): Starting Child Actor")
            thread{
                while(true){
                    println(".")
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
        val actorRef = actorSystem.actorOf(Props.create(MyActor::class.java))
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
