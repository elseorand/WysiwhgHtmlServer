package com.elseorand.wysiwyghtml

import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.ws.{Message, TextMessage}

import spray.json._

class WebService(implicit fm: Materializer, system: ActorSystem) extends Directives {

  def route = get {
    pathPrefix("wysiwyghtml") {
      pathSingleSlash {
        getFromResource("HtmlDeveloping.htm")
      } ~
      pathPrefix("jscss") {
        getFromResourceDirectory("jscss")
      } ~
      pathPrefix("ws") {
        Console println "path ws"
        parameter('userId) { userId =>
        Console println s"userId : $userId"
        handleWebSocketMessages(wsFlow(senderId = userId))
      }
    }
  }

  def wsFlow(senderId: String): Flow[Message, Message, _] =
    Flow[Message]
      .collect {
        case TextMessage.Strict(msg) => msg
      }
  }


}
