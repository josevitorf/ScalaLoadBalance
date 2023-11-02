package com.ghurtchu.loadbalancer.domain

given urlsReader: ConfigReader[Urls] = ConfigReader[Vector[Url]].map(Urls.apply)

  given urlReader: ConfigReader[Url] = ConfigReader[String].map(Url.apply)

  given healthCheckReader: ConfigReader[HealthCheckInterval] =
    ConfigReader[Long].map(HealthCheckInterval.apply)
