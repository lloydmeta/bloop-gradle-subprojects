package com.beachape

class CSpec extends BSpecLike {

  it("should yo") {
    assert(yo == "yo")
  }

  it("should lol") {
    assert(Lol.lol == "lol")
  }

  it("should use beHelper") {
    assert(beHelper == "helpful")
  }

  it("should meh") {
    assert(Meh.speak == "meh")
  }
}
