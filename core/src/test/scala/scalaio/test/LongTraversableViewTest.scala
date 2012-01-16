package scalaio.test

import scalax.io.LongTraversable

class LongTraversableViewTest extends LongTraversableTest{
  override def traversable[U, A](tsize: Int,
                                 callback: (Int) => U,
                                 dataFunc: (Int) => Traversable[Int],
                                 conv: (Int) => A,
                                 closeFunction: () => Unit = () => ()): LongTraversable[A] =
    super.traversable(tsize,callback,dataFunc,conv,closeFunction)
}