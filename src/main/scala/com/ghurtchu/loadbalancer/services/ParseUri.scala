package com.ghurtchu.loadbalancer.services

def apply(uri: String): Either[InvalidUri, Uri]

trait ParseUri:

  object ParseUri:
    override def apply(uri: String): Either[InvalidUri, Uri] =
      Uri
        .fromString(uri)
        .leftMap(_ => InvalidUri(uri))
