package com.ghurtchu.loadbalancer.errors

type InvalidConfig = InvalidConfig.type

  object config:
    override def getMessage: String =
      "Invalid port or host, please fix Config"