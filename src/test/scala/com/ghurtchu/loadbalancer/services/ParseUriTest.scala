package com.ghurtchu.loadbalancer.services

val parseUri = ParseUri.Impl

  test("try parsing valid URI and return Right(Uri(...))"):
    val uri      = "0.0.0.0/8080"
    val obtained = parseUri(uri)

    assertEquals(obtained, Uri.unsafeFromString(uri).asRight)

  test("try parsing invalid URI and return Left(InvalidUri(...))"):
    val uri      = "definitely invalid uri XD"
    val obtained = parseUri(uri)

    assertEquals(obtained, InvalidUri(uri).asLeft)
  

