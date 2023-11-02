package com.ghurtchu.loadbalancer.services

def apply(backendUrl: String, request: Request[IO]): String

trait AddRequestPathToBackendUrl:

  object AddRequestPathToBackendUrl:
    override def apply(backendUrl: String, request: Request[IO]): String =
      val requestPath = request.uri.path.renderString
        .dropWhile(_ != '/')

      backendUrl concat requestPath
