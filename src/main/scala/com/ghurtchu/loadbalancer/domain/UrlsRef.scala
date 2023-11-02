package com.ghurtchu.loadbalancer.domain

case Backends(override val urls: Ref[IO, Urls])     extends UrlsRef(urls)
  case HealthChecks(override val urls: Ref[IO, Urls]) extends UrlsRef(urls)
