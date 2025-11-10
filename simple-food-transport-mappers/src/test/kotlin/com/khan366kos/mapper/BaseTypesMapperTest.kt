package com.khan366kos.mapper

import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.measure.BeMeasureTranslation
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
            val testId = UUID.randomUUID()
            val expected = TransportMeasure(id = testId, code = "MILLILITER", measureName = "milliliter", measureShortName = "ml")
            val actual: BeMeasureTranslation = expected.toContext()
            with(actual) {
                id.asUUID() shouldBe testId
                code shouldBe "MILLILITER"
                name shouldBe "milliliter"
                shortName shouldBe "ml"
                toTransport() shouldBe expected
            }
        }

        should("Weight mapping roundtrip preserves value and measure") {
            val testId = UUID.randomUUID()
            val measure = TransportMeasure(id = testId, code = "GRAM", measureName = "gram", measureShortName = "g")
            val expected = TransportWeight(weightValue = 123.4f, measure = measure)
            val actual: BeWeight = expected.toContext()
            with(actual) {
                value shouldBe (123.4 plusOrMinus 1e-4)
                this.measure shouldBe measure.toContext()
                val back = toMeasureTranslation()
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
            val back = actual.toMeasureTranslation()
            with(back) {
                id shouldBe UUID.fromString(actual.authorId.value)
                name.shouldNotBeNull()
                email.shouldNotBeNull()
            }
        }
    })
