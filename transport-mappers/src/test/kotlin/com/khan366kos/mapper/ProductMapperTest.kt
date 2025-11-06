package com.khan366kos.mapper

import io.kotest.core.spec.style.FunSpec
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

class ProductMapperTest : FunSpec({

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

    test("Transport -> Common mapping maps all fields") {
        val t = sampleTransportProduct()
        val c = t.toContext()

        c.productId.value shouldBe t.productId.toString()
        c.productName shouldBe t.productName
        c.productCalories.title shouldBe t.productCalories.title
        c.productCalories.shortTitle shouldBe t.productCalories.shortTitle
        c.productCalories.measure shouldBe t.productCalories.measure.toContext()
        c.productProteins.title shouldBe t.productProteins.title
        c.productFats.title shouldBe t.productFats.title
        c.productCarbohydrates.title shouldBe t.productCarbohydrates.title
        c.weight.value shouldBe t.weight.weightValue.toDouble()
        c.weight.measure shouldBe t.weight.measure.toContext()
        c.author.authorId.value shouldBe t.author?.id.toString()
        c.categories.value.map { it.value } shouldBe t.categories
    }

    test("Common -> Transport roundtrip preserves semantic values") {
        val t = sampleTransportProduct()
        val c: CommonProduct = t.toContext()
        val back: TransportProduct = c.toTransport()

        back shouldBe t
        back.author?.id shouldBe t.author?.id
        back.categories shouldBe t.categories
    }
})


