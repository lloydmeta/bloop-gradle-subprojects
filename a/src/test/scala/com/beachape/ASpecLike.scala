package com.beachape

import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.diagrams.Diagrams

@RunWith(classOf[JUnitRunner])
abstract class ASpecLike
    extends AnyFunSpec
    with TypeCheckedTripleEquals
    with Diagrams {
  def yo: String = "yo"
}
