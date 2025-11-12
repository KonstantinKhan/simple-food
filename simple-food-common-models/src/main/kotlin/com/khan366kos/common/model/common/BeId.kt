package com.khan366kos.common.model.common

import java.util.UUID

@JvmInline
value class BeId(val value: String) {

    constructor(id: UUID) : this(id.toString())

    companion object {
        val NONE = BeId("")
    }

    fun asUUID(): UUID = UUID.fromString(value)
}