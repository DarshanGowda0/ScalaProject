sealed trait Calc

sealed trait Op extends Calc {
  def eval: Lit
}

sealed trait Reducer

case class Add(lhs: Calc, rhs: Calc) extends Op {
  override def eval: Lit = {
    Lit(lhs.asInstanceOf[Lit].i + rhs.asInstanceOf[Lit].i)
  }
}

case class Mul(lhs: Calc, rhs: Calc) extends Op {
  override def eval: Lit = {
    Lit(lhs.asInstanceOf[Lit].i * rhs.asInstanceOf[Lit].i)
  }
}

case class Lit(i: Long) extends Calc

case object AddReduce extends Reducer

case object MulReduce extends Reducer

case class Let(expr: Calc, sym: Symbol, body: Calc) extends Calc

case class Subs(sym: Symbol) extends Calc

case class Stmts(stmts: List[Calc], reduce_op: Reducer) extends Calc


object App {

  implicit def LitSyntax(i: Long): Lit = Lit(i)

  def eval(statements: Stmts): Lit = {
    val mappedList = statements.stmts.map(c => c.asInstanceOf[Op])
    statements.reduce_op match {
      case AddReduce =>
        var result: Long = 0
        for (op <- mappedList) {
          result += op.eval.i
        }
        Lit(result)
      case MulReduce =>
        var result: Long = 0
        for (op <- mappedList) {
          result *= op.eval.i
        }
        Lit(result)
    }
  }

  def main(args: Array[String]): Unit = {
    val test = Stmts(List(Add(Lit(1), Lit(2)), Mul(Lit(5), Lit(7)), Mul(Lit(-1), Lit(3)), Mul(Lit(5), Lit(-1))), AddReduce)
    val someTemp: Lit = eval(test)
    println(someTemp.i)
  }
}