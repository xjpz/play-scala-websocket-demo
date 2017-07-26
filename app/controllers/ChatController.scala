package controllers

import javax.inject.{Inject, Named}

import actors.{ChatActor, HelloActor, PushActor}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, ControllerComponents, WebSocket}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

/**
  * Created by xjpz on 2017/7/25.
  */
class ChatController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {



  def chatStart = Action { request =>
    Ok(views.html.chat(request))
  }

  def chat = WebSocket.accept[String, String] { _ =>
    ActorFlow.actorRef(actor => ChatActor.props(actor))
  }

  def push = Action {

    implicit val timeout = Timeout(1.second)

    val actor = system.actorOf(Props.create(classOf[PushActor]))
    actor ! PushActor.Push("push")

    Ok("success")
  }

}
