package com.ghurtchu.loadbalancer.services

def apply(ref: UrlsRef): IO[F[Url]]

trait RoundRobin[F[_]]:

  type BackendsRoundRobin     = RoundRobin[Option]
  type HealthChecksRoundRobin = RoundRobin[Id]

  def forBackends: BackendsRoundRobin = new BackendsRoundRobin:
    override def apply(ref: UrlsRef): IO[Option[Url]] =
      ref.urls
        .getAndUpdate(_.next)
        .map(_.currentOpt)

  def forHealthChecks: HealthChecksRoundRobin = new HealthChecksRoundRobin:
    override def apply(ref: UrlsRef): IO[Id[Url]] =
      ref.urls
        .getAndUpdate(_.next)
        .map(_.currentUnsafe)

  val TestId: RoundRobin[Id] = _ => IO.pure(Url("localhost:8081"))
  val LocalHost8081: RoundRobin[Option] = _ => IO.pure(Some(Url("localhost:8081")))
