package com.khan366kos.mapper

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.common.BeWeight
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
                name.value shouldBe "milliliter"
                shortName.value shouldBe "ml"
                // Note: code is not part of BeMeasureTranslation anymore, it belongs to BeMeasure
                // So we only verify that translation fields are preserved
                locale.value shouldBe ""  // locale not provided by TransportMeasure
            }
        }

        should("Weight mapping roundtrip preserves value and measure") {
            val testId = UUID.randomUUID()
            val measure = TransportMeasure(id = testId, code = "GRAM", measureName = "gram", measureShortName = "g")
            val expected = TransportWeight(weightValue = 123.4f, measure = measure)
            val actual: BeWeight = expected.toContext()
            with(actual) {
                value.value shouldBe (123.4 plusOrMinus 1e-4)
                this.measure shouldBe measure.toContext()
                val back = toMeasureTranslation()
                // Check that weight value and measure's translation fields are preserved
                // Note: code is lost in roundtrip since it's no longer part of BeMeasureTranslation
                back.weightValue shouldBe expected.weightValue
                back.measure.id shouldBe expected.measure.id
                back.measure.measureName shouldBe expected.measure.measureName
                back.measure.measureShortName shouldBe expected.measure.measureShortName
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
                name.value shouldBe ""
                email.value shouldBe ""
            }
            val back = actual.toMeasureTranslation()
            with(back) {
                id shouldBe UUID.fromString(actual.authorId.value)
                name.shouldNotBeNull()
                email.shouldNotBeNull()
            }
        }
    })
