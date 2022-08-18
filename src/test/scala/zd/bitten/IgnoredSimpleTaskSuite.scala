package zd.bitten

import weaver.monixcompat.SimpleTaskSuite
import weaver.Expectations
import monix.eval.Task

trait IgnoredSimpleTaskSuite extends SimpleTaskSuite {

  def test(name: String)(block: => Task[Expectations]): Unit = {
    logIgnore(name)
  }

  def pureTest(name: String)(block: => Expectations): Unit = {
    logIgnore(name)
  }

  def xpureTest(name: String)(block: => Expectations): Unit = {
    super.pureTest(name)(block)
  }

  def xtest(name: String)(block: => Task[Expectations]): Unit = {
    super.test(name)(block)
  }

  private def logIgnore(testName: String): Unit = {
    val IGNORE = scala.io.AnsiColor.YELLOW
    val RESET = scala.io.AnsiColor.RESET
    println(s"${IGNORE}Ignored Test${RESET}: $testName")
  }

}