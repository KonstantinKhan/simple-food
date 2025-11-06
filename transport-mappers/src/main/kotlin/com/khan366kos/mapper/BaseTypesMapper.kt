package com.khan366kos.mapper

import com.khan366kos.common.model.Author as CommonAuthor
import com.khan366kos.common.model.simple.Category as CommonCategory
import com.khan366kos.common.model.Id as CommonId
import com.khan366kos.common.model.Measure as CommonMeasure
import com.khan366kos.common.model.Title as CommonTitle
import com.khan366kos.common.model.Weight as CommonWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight1 as TransportWeight
import java.util.UUID

fun TransportMeasure.toCommon(): CommonMeasure = when (this) {
    TransportMeasure.g -> CommonMeasure.g
    TransportMeasure.kg -> CommonMeasure.kg
    TransportMeasure.ml -> CommonMeasure.ml
    TransportMeasure.l -> CommonMeasure.l
    TransportMeasure.pcs -> CommonMeasure.pcs
}

fun CommonMeasure.toTransport(): TransportMeasure = when (this) {
    CommonMeasure.g -> TransportMeasure.g
    CommonMeasure.kg -> TransportMeasure.kg
    CommonMeasure.ml -> TransportMeasure.ml
    CommonMeasure.l -> TransportMeasure.l
    CommonMeasure.pcs -> TransportMeasure.pcs
}

fun TransportWeight.toCommon(): CommonWeight = CommonWeight(
    value = `value`.toDouble(),
    measure = measure.toCommon()
)

fun CommonWeight.toTransport(): TransportWeight = TransportWeight(
    `value` = value.toFloat(),
    measure = measure.toTransport()
)

private val ZERO_UUID: UUID = UUID(0L, 0L)

private fun String.toUuidOrZero(): UUID = runCatching { UUID.fromString(this) }.getOrElse { ZERO_UUID }

fun UUID?.toCommonId(): CommonId = CommonId((this ?: ZERO_UUID).toString())
fun CommonId.toTransport(): UUID = value.takeIf { it.isNotBlank() }?.toUuidOrZero() ?: ZERO_UUID

fun String?.toCommonTitle(): CommonTitle = CommonTitle(this ?: "")
fun CommonTitle.toTransport(): String = value

fun String?.toCommonCategory(): CommonCategory = CommonCategory(this ?: "")
fun CommonCategory.toTransport(): String = value

fun TransportAuthor?.toCommon(): CommonAuthor = if (this == null) {
    CommonAuthor.NONE
} else {
    CommonAuthor(
        id = id.toCommonId(),
        name = name ?: "",
        email = email ?: ""
    )
}

fun CommonAuthor.toTransport(): TransportAuthor = TransportAuthor(
    id = id.toTransport(),
    name = name,
    email = email
)


