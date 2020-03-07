sealed trait Cmd
sealed trait Print extends Cmd
case class RandomNum(lb: Int = 0, ub: Int = 100) extends Print
sealed trait Run extends Cmd
case class Q1(expr: String) extends Run {
  def parse: Calc = ???
}
case class Q2(greedy: Boolean, k: Int) extends Run
sealed trait Q3 extends Run
case class Q3Put(key: String, value: String) extends Q3
case class Q3Get(key: String) extends Q3
case object Q3Dump extends Q3
case object Q3Start extends Q3
case object Q3Stop extends Q3

class Problem4 {

}
