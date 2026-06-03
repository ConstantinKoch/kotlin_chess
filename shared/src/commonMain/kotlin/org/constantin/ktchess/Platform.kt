package org.constantin.ktchess

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform