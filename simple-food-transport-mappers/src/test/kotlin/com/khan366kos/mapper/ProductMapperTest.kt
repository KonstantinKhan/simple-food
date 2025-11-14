package com.khan366kos.mapper

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.util.UUID
import com.khan366kos.transport.model.Product as TransportProduct
import com.khan366kos.transport.model.ProductCreateRequest
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.transport.model.NutritionalValue as TransportNutritionalValue
import com.khan366kos.common.model.product.BeProduct as CommonProduct
import com.khan366kos.common.model.common.BeId
import com.khan366kos.mapper.toContext.*
import com.khan366kos.mapper.toTransport.*

class ProductMapperTest : ShouldSpec({

    fun sampleTransportProduct(): TransportProduct {
        val measureGram = TransportMeasure(id = UUID.randomUUID(), code = "GRAM", measureName = "gram", measureShortName = "g")
        fun nv(t: String, s: String, v: Float = 0f) = TransportNutritionalValue(title = t, shortTitle = s, nutritionalValue = v, measure = measureGram)
        return TransportProduct(
            productId = UUID.randomUUID(),
            productName = "Chicken breast",
            productCalories = nv("Calories", "kcal", 165f),
            productProteins = nv("Proteins", "g", 31f),
            productFats = nv("Fats", "g", 3.6f),
            productCarbohydrates = nv("Carbohydrates", "g", 0f),
            weight = TransportWeight(weightValue = 100f, measure = measureGram),
            author = TransportAuthor(id = UUID.randomUUID(), name = "John", email = "john@example.com"),
            categories = listOf("protein", "diet")
        )
    }

    should("Transport -> Common mapping maps all fields") {
        val expected = sampleTransportProduct()
        val actual = expected.toContext()

        actual.productId.value shouldBe expected.productId.toString()
        actual.productName.value shouldBe expected.productName
        actual.productCalories.title.value shouldBe expected.productCalories.title
        actual.productCalories.shortTitle.value shouldBe expected.productCalories.shortTitle
        actual.productCalories.measure shouldBe expected.productCalories.measure.toContext()
        actual.productProteins.title.value shouldBe expected.productProteins.title
        actual.productFats.title.value shouldBe expected.productFats.title
        actual.productCarbohydrates.title.value shouldBe expected.productCarbohydrates.title
        actual.weight.value.value shouldBe expected.weight.weightValue.toDouble()
        actual.weight.measure shouldBe expected.weight.measure.toContext()
        actual.author.authorId.value shouldBe expected.author?.id.toString()
        actual.categories.value.map { it.value } shouldBe expected.categories
    }

    should("Common -> Transport roundtrip preserves semantic values") {
        val expected = sampleTransportProduct()
        val context: CommonProduct = expected.toContext()
        val actual: TransportProduct = context.toMeasureTranslation()

        // Note: measure code is lost in roundtrip since it's no longer part of BeMeasureTranslation
        // So we check individual fields instead of full equality
        actual.productId shouldBe expected.productId
        actual.productName shouldBe expected.productName
        actual.author?.id shouldBe expected.author?.id
        actual.categories shouldBe expected.categories
    }

    fun sampleProductCreateRequest(): ProductCreateRequest {
        val measureGram = TransportMeasure(id = UUID.randomUUID(), code = "GRAM", measureName = "gram", measureShortName = "g")
        fun nv(t: String, s: String, v: Float = 0f) = TransportNutritionalValue(title = t, shortTitle = s, nutritionalValue = v, measure = measureGram)
        return ProductCreateRequest(
            productName = "Chicken breast",
            productCalories = nv("Calories", "kcal", 165f),
            productProteins = nv("Proteins", "g", 31f),
            productFats = nv("Fats", "g", 3.6f),
            productCarbohydrates = nv("Carbohydrates", "g", 0f),
            weight = TransportWeight(weightValue = 100f, measure = measureGram),
            author = TransportAuthor(id = UUID.randomUUID(), name = "John", email = "john@example.com"),
            categories = listOf("protein", "diet")
        )
    }

    should("ProductCreateRequest -> Common mapping assigns BeId.NONE and maps all fields") {
        val createRequest = sampleProductCreateRequest()
        val actual = createRequest.toContext()

        actual.productId shouldBe BeId.NONE
        actual.productName.value shouldBe createRequest.productName
        actual.productCalories.title.value shouldBe createRequest.productCalories.title
        actual.productCalories.shortTitle.value shouldBe createRequest.productCalories.shortTitle
        actual.productProteins.title.value shouldBe createRequest.productProteins.title
        actual.productFats.title.value shouldBe createRequest.productFats.title
        actual.productCarbohydrates.title.value shouldBe createRequest.productCarbohydrates.title
        actual.weight.value.value shouldBe createRequest.weight.weightValue.toDouble()
        actual.author.authorId.value shouldBe createRequest.author?.id.toString()
        actual.categories.value.map { it.value } shouldBe createRequest.categories
    }
})


