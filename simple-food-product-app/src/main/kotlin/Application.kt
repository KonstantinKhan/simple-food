package com.khan366kos

import com.khan366kos.plugins.configureStatusPages
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureStatusPages()
    configureHTTP()
    configureRouting()
}


