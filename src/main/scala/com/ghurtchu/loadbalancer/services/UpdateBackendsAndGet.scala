package com.ghurtchu.loadbalancer.services

def apply(
    backends: Backends,
    url: Url,
    status: ServerHealthStatus,
  ): IO[Urls]

trait UpdateBackendsAndGet:

  object UpdateBackendsAndGet:
    override def apply(
      backends: Backends,
      url: Url,
      status: ServerHealthStatus,
    ): IO[Urls] =
      backends.urls.updateAndGet { urls =>
        status match
          case ServerHealthStatus.Alive => urls.add(url)
          case ServerHealthStatus.Dead  => urls.remove(url)
