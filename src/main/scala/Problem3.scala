import Problem3.DataStoreImpl
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import scala.collection.mutable
import scala.collection.mutable.HashMap

trait DataStore {
  def addKey(key: String, value: String): IO[Unit]

  def getKey(key: String): IO[Option[String]]

}

object Problem3 {

  class DataStoreImpl extends DataStore {

    val storage: mutable.Map[String, String] = HashMap[String, String]().empty

    override def addKey(key: String, value: String): IO[Unit] = IO {
      storage.put(key, value)
    }

    override def getKey(key: String): IO[Option[String]] = IO {
      val res = storage.get(key)
      res
    }
  }

}

object Key extends QueryParamDecoderMatcher[String]("key")
object Value extends QueryParamDecoderMatcher[String]("value")

object MyRoutes {
  def routes(dataStore: DataStore): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case _@GET -> Root / "get" :? Key(keyParam) =>
        dataStore.getKey(keyParam) flatMap {
          case None => Ok("This key is not entered into the Key-Value store yet.\nYou can enter " +
            "the key value pair using " +
            s"${Root}/set?key=${keyParam}&value=yourvalue")
          case Some(value) => Ok(s"Value for the key ${keyParam} is ${value}")
        }
      case _@GET -> Root/ "set" :? Key(keyParam) +& Value(valueParam) =>
        dataStore.addKey(keyParam, valueParam) flatMap (_ => Ok(s"Your ${keyParam} - " +
          s"${valueParam} has been successfully saved. \nNow try doing ${Root}/get?key=${keyParam}"))
    }
  }
}

object Main extends IOApp {

  private val dataStore: DataStore = new DataStoreImpl()
  val httpRoutes = Router("/" -> MyRoutes.routes(dataStore)).orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO]
      .bindHttp(4000, "localhost")
      .withHttpApp(httpRoutes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}