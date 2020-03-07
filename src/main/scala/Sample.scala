import optparse_applicative.{help, _}
import scalaz.syntax.apply._

case class Sample(hello: String, quiet: Boolean)

object SampleMain {

  val sample: Parser[Sample] =
    ^(
      strOption(short('h'), long("hello"), metavar("TARGET"), help("Target for the greeting")),
      switch(long("quiet"), help("Whether to be quiet"))
    )(Sample.apply)

  val print: Parser[] =

  def greet(s: Sample): Unit = s match {
    case Sample(h, false) => println("Hello, " ++ h)
    case _ =>
  }

  def main(args: Array[String]): Unit = {
    val opts = info(sample <*> helper,
      progDesc("Print a greeting for TARGET"),
      header("hello - a test for scala-optparse-applicative"))
    greet(execParser(args, "SampleMain", opts))
  }

}