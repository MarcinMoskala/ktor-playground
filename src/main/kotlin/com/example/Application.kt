package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("set_cookie") {
            call.apply {
                val item = Cookie(
                    "guid",
                    "SOME_TOKEN",
                    secure = true,
                    extensions = mapOf("SameSite" to "None")
                )
                response.headers.append("Set-Cookie", renderSetCookieHeader(item))
            }.respond("Cookie set")
        }

        get("respond_cookie") {
            val cookie: String? = call.request.cookies["guid"]
            println("Sent cookie: $cookie")
            call.respond("Cookie: " + cookie.orEmpty())
        }
    }
}
