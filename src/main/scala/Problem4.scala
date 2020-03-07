import optparse_applicative._
import scalaz.syntax.apply._

sealed trait Cmd

sealed trait Print extends Cmd

case class PrintOptions(randomNum: RandomNum)

case class RandomNum(lb: Int = 0, ub: Int = 100) extends Print

object Problem4Main {

  val print: Parser[RandomNum] =
    subparser(
      command(
        "random_number",
        info(
          ^(
            intOption(long("min"), help("Int (between min and max)"), metavar("Int (between min and max)")),
            intOption(long("max"), help("Int (between min and max)"), metavar("Int (between min and max)"))
          )
          (RandomNum),
          progDesc(
            "|- --max Int (between min and max)\n|- --min Int (between " +
              "min and max)")
        )
      )
    )


  def eval(randomNum: RandomNum): Unit = randomNum match {
    case RandomNum(lb, ub) => val rnd = new scala.util.Random
      println(lb + rnd.nextInt((ub - lb) + 1))
    case _ =>
  }

  def main(args: Array[String]): Unit = {
    val opts = info(print <*> helper,
      progDesc("print a random number between [min] and [max]"),
      header("A simple random-number generator!"))
    eval(execParser(args, "print", opts))
  }

}
