package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import java.time.Duration

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toIntOrNull() ?: 8090, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
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

        webSocket("sample_socket") {
            send(Frame.Text("Hello from websocket"))

            val guid: String? = call.request.cookies["guid"]
            repeat(100) {
                delay(1000)
                send(Frame.Text("Pint$it, guid was $guid"))
            }
        }
    }
}
