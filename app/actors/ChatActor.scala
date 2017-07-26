package actors

import actors.ChatActor.Create
import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import play.api.libs.json.Json

/**
  * Created by xjpz on 2017/7/25.
  */

case class Join(username: String)
case class Leave(username: String)
case class Talk(username: String, message: String)

class ChatActor(actorRef: ActorRef) extends Actor {

  override
  def receive:Receive = {

    case str: String =>

      val json = Json.parse(str)

      (json \ "type").as[String] match {
        case "join"  => self ! Join((json \ "username").as[String])
        case "leave" => self ! Leave((json \ "username").as[String])
        case "talk" => self ! Talk((json \ "username").as[String], (json \ "msg").as[String])
        case _ => self ! Create(json.toString())
      }

    case Join(username) =>
      ChatActor.users = (username, actorRef) :: ChatActor.users
      println(ChatActor.users)
      broadcast(username + "已加入群聊。")
      actorRef ! (s"大家好，我是$username 欢迎随时撩我。")

    case Leave(username) =>
      ChatActor.users = ChatActor.users.filter(u => u._1 != username)
      broadcast(username + "已离开群聊。")
//      actorRef ! ("Leave")
      actorRef ! PoisonPill

    case Talk(username, msg) =>
      println(msg)
      broadcast(username + " : " + msg)

    case Create(msg) =>
      println(msg)
      broadcast(msg)

  }

  def broadcast(msg: String) = {
    ChatActor.users.foreach(u => u._2 ! msg)
  }

}

object ChatActor {
  var users = List[(String, ActorRef)]()
  def props(actor: ActorRef) = Props(new ChatActor(actor))

  case class Create(msg:String)
}

