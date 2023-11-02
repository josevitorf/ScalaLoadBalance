package com.ghurtchu.loadbalancer.services

def from(
    backends: Backends,
    sendAndExpectResponse: Request[IO] => SendAndExpect[String],
    parseUri: ParseUri,
    addRequestPathToBackendUrl: AddRequestPathToBackendUrl,
    backendsRoundRobin: BackendsRoundRobin,
  ): HttpRoutes[IO] =
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] { anyRequest =>
      backendsRoundRobin(backends).flatMap {
        _.fold(Ok("All backends are inactive")) { backendUrl =>
          val url = addRequestPathToBackendUrl(backendUrl.value, anyRequest)
          for
            uri      <- IO.fromEither(parseUri(url))
            response <- sendAndExpectResponse(anyRequest)(uri)
            result   <- Ok(response)
          yield result
        }
      }
