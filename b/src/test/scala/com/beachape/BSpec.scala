package com.beachape

class BSpec extends ASpecLike {

  it("should yo") {
    assert(yo == "yo")
  }

  it("should lol") {
    assert(Lol.lol == "lol")
  }

  it("should meh") {
    assert(Meh.speak == "meh")
  }
}
