package com.khan366kos.mapper

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.util.UUID
import com.khan366kos.transport.model.Product as TransportProduct
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.transport.model.NutritionalValue as TransportNutritionalValue
import com.khan366kos.common.model.BeProduct as CommonProduct
import com.khan366kos.mapper.toContext.*
import com.khan366kos.mapper.toTransport.*

class ProductMapperTest : ShouldSpec({

    fun sampleTransportProduct(): TransportProduct {
        val measureGram = TransportMeasure(measureName = "gram", measureShortName = "g")
        fun nv(t: String, s: String) = TransportNutritionalValue(title = t, shortTitle = s, measure = measureGram)
        return TransportProduct(
            productId = UUID.randomUUID(),
            productName = "Chicken breast",
            productCalories = nv("Calories", "kcal"),
            productProteins = nv("Proteins", "g"),
            productFats = nv("Fats", "g"),
            productCarbohydrates = nv("Carbohydrates", "g"),
            weight = TransportWeight(weightValue = 100f, measure = measureGram),
            author = TransportAuthor(id = UUID.randomUUID(), name = "John", email = "john@example.com"),
            categories = listOf("protein", "diet")
        )
    }

    should("Transport -> Common mapping maps all fields") {
        val expected = sampleTransportProduct()
        val actual = expected.toContext()

        actual.productId.value shouldBe expected.productId.toString()
        actual.productName shouldBe expected.productName
        actual.productCalories.title shouldBe expected.productCalories.title
        actual.productCalories.shortTitle shouldBe expected.productCalories.shortTitle
        actual.productCalories.measure shouldBe expected.productCalories.measure.toContext()
        actual.productProteins.title shouldBe expected.productProteins.title
        actual.productFats.title shouldBe expected.productFats.title
        actual.productCarbohydrates.title shouldBe expected.productCarbohydrates.title
        actual.weight.value shouldBe expected.weight.weightValue.toDouble()
        actual.weight.measure shouldBe expected.weight.measure.toContext()
        actual.author.authorId.value shouldBe expected.author?.id.toString()
        actual.categories.value.map { it.value } shouldBe expected.categories
    }

    should("Common -> Transport roundtrip preserves semantic values") {
        val expected = sampleTransportProduct()
        val context: CommonProduct = expected.toContext()
        val actual: TransportProduct = context.toTransport()

        actual shouldBe expected
        actual.author?.id shouldBe expected.author?.id
        actual.categories shouldBe expected.categories
    }
})


