package com.khan366kos.mapper

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.util.UUID
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight1 as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.common.model.Measure as CommonMeasure

class BaseTypesMapperTest : FunSpec({

    test("Measure mapping roundtrip") {
        TransportMeasure.g.toCommon() shouldBe CommonMeasure.g
        CommonMeasure.kg.toTransport() shouldBe TransportMeasure.kg
    }

    test("Weight mapping roundtrip preserves value and measure") {
        val t = TransportWeight(`value` = 123.4f, measure = TransportMeasure.ml)
        val c = t.toCommon()
        c.value shouldBe 123.4
        c.measure shouldBe CommonMeasure.ml

        val back = c.toTransport()
        back.`value` shouldBe 123.4f
        back.measure shouldBe TransportMeasure.ml
    }

    test("UUID/CommonId mapping handles invalid and null") {
        val zero = UUID(0L, 0L)
        val fromNull = (null as UUID?).toCommonId()
        fromNull.value shouldBe zero.toString()

        val invalid = "not-a-uuid".let { com.khan366kos.common.model.Id(it) }.toTransport()
        invalid shouldBe zero

        val valid = UUID.randomUUID()
        valid.toCommonId().value shouldBe valid.toString()
    }

    test("Author mapping fills defaults on null fields") {
        val t = TransportAuthor(id = UUID.randomUUID(), name = null, email = null)
        val c = t.toCommon()
        c.name shouldBe ""
        c.email shouldBe ""

        val back = c.toTransport()
        back.id shouldBe UUID.fromString(c.id.value)
        back.name.shouldNotBeNull()
        back.email.shouldNotBeNull()
    }
})


