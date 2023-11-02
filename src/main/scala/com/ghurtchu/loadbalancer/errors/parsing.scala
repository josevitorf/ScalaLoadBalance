package com.ghurtchu.loadbalancer.errors

object parsing:
    override def getMessage: String =
      s"Could not construct proper URI from $uri"
