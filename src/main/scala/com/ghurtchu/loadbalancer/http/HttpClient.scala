package com.ghurtchu.loadbalancer.http

def sendAndReceive(uri: Uri, requestOpt: Option[Request[IO]]): IO[String]

trait HttpClient:

  implicit def logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def of(client: Client[IO]): HttpClient = new HttpClient:
    override def sendAndReceive(uri: Uri, requestOpt: Option[Request[IO]]): IO[String] =
      requestOpt match
        case Some(request) => client.expect[String](request.withUri(uri))
        case None          => client.expect[String](uri)

  val Hello: HttpClient                   = (_, _) => IO.pure("Hello")
  val RuntimeException: HttpClient        = (_, _) => IO.raiseError(new RuntimeException("Server is dead"))
  val TestTimeoutFailure: HttpClient      = (_, _) => IO.sleep(6.seconds).as("Hello")
  val BackendResourceNotFound: HttpClient = (_, _) =>
    IO.raiseError:
      UnexpectedStatus(
        org.http4s.Status.NotFound,
        org.http4s.Method.GET,
        Uri.unsafeFromString("localhost:8081"),
      )
