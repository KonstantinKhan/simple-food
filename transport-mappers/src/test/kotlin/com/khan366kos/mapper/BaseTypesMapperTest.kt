package com.khan366kos.mapper

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.doubles.plusOrMinus
import java.util.UUID
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.common.model.BeMeasure
import com.khan366kos.common.model.BeWeight
import com.khan366kos.common.model.BeId
import com.khan366kos.mapper.toContext.*
import com.khan366kos.mapper.toTransport.*

class BaseTypesMapperTest : FunSpec({

    test("Measure mapping roundtrip") {
        val t = TransportMeasure(measureName = "milliliter", measureShortName = "ml")
        val c: BeMeasure = t.toContext()
        c.measureName shouldBe "milliliter"
        c.measureShortName shouldBe "ml"
        c.toTransport() shouldBe t
    }

    test("Weight mapping roundtrip preserves value and measure") {
        val tm = TransportMeasure(measureName = "gram", measureShortName = "g")
        val t = TransportWeight(weightValue = 123.4f, measure = tm)
        val c: BeWeight = t.toContext()
        c.value shouldBe (123.4 plusOrMinus 1e-4)
        c.measure shouldBe tm.toContext()

        val back = c.toTransport()
        back shouldBe t
    }

    test("BeId <-> UUID roundtrip") {
        val valid = UUID.randomUUID()
        val id = BeId(valid)
        id.asUUID() shouldBe valid
    }

    test("Author mapping fills defaults on null fields") {
        val t = TransportAuthor(id = UUID.randomUUID(), name = null, email = null)
        val c = t.toContext()
        c.name shouldBe ""
        c.email shouldBe ""

        val back = c.toTransport()
        back.id shouldBe UUID.fromString(c.authorId.value)
        back.name.shouldNotBeNull()
        back.email.shouldNotBeNull()
    }
})


