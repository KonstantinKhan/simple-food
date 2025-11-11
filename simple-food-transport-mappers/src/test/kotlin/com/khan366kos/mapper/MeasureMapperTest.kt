package com.khan366kos.mapper

import com.khan366kos.common.model.BeId
import com.khan366kos.mapper.toContext.toContext
import com.khan366kos.mapper.toTransport.toMeasureTranslation
import com.khan366kos.transport.model.MeasureDetail
import com.khan366kos.transport.model.MeasureDetailCreateRequest
import com.khan366kos.transport.model.MeasureTranslation
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.UUID

class MeasureMapperTest : ShouldSpec({

    should("MeasureDetailCreateRequest.toContext() generates placeholder ID") {
        val createRequest = MeasureDetailCreateRequest(
            code = "GRAM",
            translations = listOf(
                MeasureTranslation(locale = "en", measureName = "gram", measureShortName = "g"),
                MeasureTranslation(locale = "ru", measureName = "грамм", measureShortName = "г")
            )
        )

        val beMeasure = createRequest.toContext()

        with(beMeasure.measure) {
            id shouldBe BeId.NONE
            code shouldBe "GRAM"
            createdAt shouldNotBe null
        }

        with(beMeasure.translations) {
            size shouldBe 2
            get(0).locale shouldBe "en"
            get(0).name shouldBe "gram"
            get(0).shortName shouldBe "g"
            get(1).locale shouldBe "ru"
            get(1).name shouldBe "грамм"
            get(1).shortName shouldBe "г"
        }
    }

    should("MeasureDetail.toContext() preserves ID") {
        val measureId = UUID.randomUUID()
        val measureDetail = MeasureDetail(
            id = measureId,
            code = "KILOGRAM",
            translations = listOf(
                MeasureTranslation(locale = "en", measureName = "kilogram", measureShortName = "kg")
            )
        )

        val beMeasure = measureDetail.toContext()

        with(beMeasure.measure) {
            id.value shouldBe measureId.toString()
            code shouldBe "KILOGRAM"
        }

        with(beMeasure.translations) {
            size shouldBe 1
            get(0).id.value shouldBe measureId.toString()
            get(0).locale shouldBe "en"
        }
    }

    should("MeasureDetail roundtrip preserves all data") {
        val measureId = UUID.randomUUID()
        val original = MeasureDetail(
            id = measureId,
            code = "LITER",
            translations = listOf(
                MeasureTranslation(locale = "en", measureName = "liter", measureShortName = "L"),
                MeasureTranslation(locale = "ru", measureName = "литр", measureShortName = "л")
            )
        )

        val beMeasure = original.toContext()
        val backToTransport = beMeasure.toMeasureTranslation()

        with(backToTransport) {
            id shouldBe original.id
            code shouldBe original.code
            translations.size shouldBe original.translations.size
            translations[0].locale shouldBe original.translations[0].locale
            translations[0].measureName shouldBe original.translations[0].measureName
            translations[0].measureShortName shouldBe original.translations[0].measureShortName
            translations[1].locale shouldBe original.translations[1].locale
            translations[1].measureName shouldBe original.translations[1].measureName
            translations[1].measureShortName shouldBe original.translations[1].measureShortName
        }
    }

    should("MeasureDetailCreateRequest handles empty translations") {
        val createRequest = MeasureDetailCreateRequest(
            code = "PIECE",
            translations = emptyList()
        )

        val beMeasure = createRequest.toContext()

        beMeasure.measure.code shouldBe "PIECE"
        beMeasure.translations.size shouldBe 0
    }

    should("MeasureDetail and MeasureDetailCreateRequest map the same code differently") {
        val code = "MILLILITER"
        val translations = listOf(
            MeasureTranslation(locale = "en", measureName = "milliliter", measureShortName = "ml")
        )

        // Create request - no ID
        val createRequest = MeasureDetailCreateRequest(code = code, translations = translations)
        val beFromCreate = createRequest.toContext()
        beFromCreate.measure.id shouldBe BeId.NONE

        // Detail - has ID
        val measureId = UUID.randomUUID()
        val detail = MeasureDetail(id = measureId, code = code, translations = translations)
        val beFromDetail = detail.toContext()
        beFromDetail.measure.id.value shouldBe measureId.toString()
        beFromDetail.measure.id shouldNotBe BeId.NONE
    }
})
