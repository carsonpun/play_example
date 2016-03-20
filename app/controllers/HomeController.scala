package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import akka.actor._
import play.api.libs.json._
import play.api.Play.current
import akka.stream.Materializer
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (implicit val mat: Materializer) extends Controller {
  def getTitleTextFromURL(url: String): Option[String] = {
    import org.htmlcleaner.HtmlCleaner
    import java.net.URL
    val titleStr = try {
      val cleaner = new HtmlCleaner
      val rootNode = cleaner.clean(new URL(url))
      Some(rootNode.getElementsByName("title", true)(0).getText.toString)
    } catch {
      case _ : Throwable => None
    }
    titleStr
  }
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def page = Action {
    Ok(views.html.page(""))
  }

  def socket = WebSocket.acceptWithActor[JsValue, JsValue] { request => out =>
    MyActor.props(out)
  }
  object MyActor {
    def props(out: ActorRef) = Props(new MyActor(out))
  }
  /**
   * MyActor handles client/server side websocket communication
   */
  class MyActor(out: ActorRef) extends Actor {
    override def receive: Receive = {
      case message: JsValue => {
        try {
          val url = (message \ "url").as[String]
          val titleStr: String = getTitleTextFromURL(url).getOrElse("Invalid input URL!!")

          case class Title(url: String,
                           title: String)
          implicit val titleWrites = new Writes[Title] {
            def writes(title: Title) = Json.obj(
              "url" -> title.url,
              "title" -> title.title
              )
          }

          out ! Json.toJson(Title(url, titleStr))
        } catch {
          case _ : Throwable => println(s"problem with input message = $message")
        }
      }

      case _ => println("only support Json message.")
    }
  }
}
