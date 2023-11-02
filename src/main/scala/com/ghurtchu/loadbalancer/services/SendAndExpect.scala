package com.ghurtchu.loadbalancer.services

def apply(uri: Uri): IO[A]

trait SendAndExpect[A]:

  implicit def logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def toBackend(httpClient: HttpClient, req: Request[IO]): SendAndExpect[String] =
    new SendAndExpect[String]:
      override def apply(uri: Uri): IO[String] =
        info"[LOAD-BALANCER] sending request to $uri" *> httpClient
          .sendAndReceive(uri, req.some)
          .handleErrorWith:
            case UnexpectedStatus(org.http4s.Status.NotFound, _, _) =>
              s"resource at uri: $uri was not found"
                .pure[IO]
                .flatTap(msg => warn"$msg")
            case _                   =>
              s"server with uri: $uri is dead"
                .pure[IO]
                .flatTap(msg => warn"$msg")

  def toHealthCheck(httpClient: HttpClient): SendAndExpect[ServerHealthStatus] =
    new SendAndExpect[ServerHealthStatus]:
      override def apply(uri: Uri): IO[ServerHealthStatus] =
        info"[HEALTH-CHECK] checking $uri health" *>
          httpClient
            .sendAndReceive(uri, none)
            .as(ServerHealthStatus.Alive)
            .flatTap(ss => info"$uri is alive")
            .timeout(5.seconds)
            .handleErrorWith(_ => warn"$uri is dead" *> IO.pure(ServerHealthStatus.Dead))

  val BackendSuccessTest: SendAndExpect[String] = _ => IO("Success")
