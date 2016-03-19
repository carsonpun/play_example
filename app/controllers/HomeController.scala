package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {
  def getTitleTextFromURL(url: String): String = {
    import org.htmlcleaner.HtmlCleaner
    import java.net.URL
    val cleaner = new HtmlCleaner
    val rootNode = cleaner.clean(new URL(url))
    val titleStr = rootNode.getElementsByName("title", true)(0).getText.toString
    titleStr
  }
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    Ok(getTitleTextFromURL("http://www.cnn.com"))
  }

}
