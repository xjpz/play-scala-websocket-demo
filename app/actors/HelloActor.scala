package actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}

/**
  * Created by xjpz on 2017/7/25.
  */
class HelloActor(out: ActorRef) extends Actor {

  override def preStart(): Unit = out ! "hello server by play"

  override def postStop() = {
    self ! PoisonPill
  }

  override def receive = {
    case s => out ! ("I received your message: " + s)
  }
}

object HelloActor {
  def props(out:ActorRef) = {
    Props(new HelloActor(out))
  }
}
