package actors

import actors.PushActor.Push
import akka.actor.Actor

/**
  * Created by xjpz on 2017/7/25.
  */
class PushActor extends Actor{

  val chat = context.actorOf(ChatActor.props(sender()))

  override def receive: Receive = {
    case Push(msg) => chat ! ChatActor.Create(msg)
  }
}

object PushActor{
  case class Push(msg:String)
}