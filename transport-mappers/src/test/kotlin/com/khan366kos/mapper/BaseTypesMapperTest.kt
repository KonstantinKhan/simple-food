package com.khan366kos.mapper

import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.BeMeasure
import com.khan366kos.common.model.BeWeight
import com.khan366kos.mapper.toContext.*
import com.khan366kos.mapper.toTransport.*
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight as TransportWeight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.util.UUID

class BaseTypesMapperTest :
    ShouldSpec({
        should("Measure mapping roundtrip") {
            val expected = TransportMeasure(measureName = "milliliter", measureShortName = "ml")
            val actual: BeMeasure = expected.toContext()
            with(actual) {
                measureName shouldBe "milliliter"
                measureShortName shouldBe "ml"
                toTransport() shouldBe expected
            }
        }

        should("Weight mapping roundtrip preserves value and measure") {
            val measure = TransportMeasure(measureName = "gram", measureShortName = "g")
            val expected = TransportWeight(weightValue = 123.4f, measure = measure)
            val actual: BeWeight = expected.toContext()
            with(actual) {
                value shouldBe (123.4 plusOrMinus 1e-4)
                this.measure shouldBe measure.toContext()
                val back = toTransport()
                back shouldBe expected
            }
        }

        should("BeId <-> UUID roundtrip") {
            val expected = UUID.randomUUID()
            val actual = BeId(expected).asUUID()
            actual shouldBe expected
        }

        should("Author mapping fills defaults on null fields") {
            val expected = TransportAuthor(id = UUID.randomUUID(), name = null, email = null)
            val actual = expected.toContext()
            with(actual) {
                name shouldBe ""
                email shouldBe ""
            }
            val back = actual.toTransport()
            with(back) {
                id shouldBe UUID.fromString(actual.authorId.value)
                name.shouldNotBeNull()
                email.shouldNotBeNull()
            }
        }
    })
