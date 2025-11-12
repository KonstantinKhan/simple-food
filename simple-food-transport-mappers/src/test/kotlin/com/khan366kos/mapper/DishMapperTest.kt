package com.khan366kos.mapper

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.util.UUID
import com.khan366kos.mapper.toContext.*
import com.khan366kos.mapper.toTransport.*
import com.khan366kos.transport.model.Dish as TransportDish
import com.khan366kos.transport.model.ProductPortion as TransportProductPortion
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.transport.model.NutritionalValue as TransportNutritionalValue
import com.khan366kos.common.model.dish.BeDish as CommonDish

class DishMapperTest : ShouldSpec({

    fun sampleDish(): TransportDish {
        val measureGram = TransportMeasure(id = UUID.randomUUID(), code = "GRAM", measureName = "gram", measureShortName = "g")
        fun nv(t: String, s: String, v: Float = 0f) = TransportNutritionalValue(title = t, shortTitle = s, nutritionalValue = v, measure = measureGram)
        return TransportDish(
            id = UUID.randomUUID(),
            title = "Chicken salad",
            calories = nv("Calories", "kcal", 300f),
            proteins = nv("Proteins", "g", 25f),
            fats = nv("Fats", "g", 10f),
            carbohydrates = nv("Carbohydrates", "g", 15f),
            weight = TransportWeight(weightValue = 250f, measure = measureGram),
            author = TransportAuthor(id = UUID.randomUUID(), name = "John", email = "john@example.com"),
            categories = listOf("healthy", "lunch"),
            recipes = listOf("Cut", "Mix", "Serve"),
            products = listOf(
                TransportProductPortion(
                    productId = UUID.randomUUID(),
                    weight = TransportWeight(weightValue = 150f, measure = measureGram)
                ),
                TransportProductPortion(
                    productId = UUID.randomUUID(),
                    weight = TransportWeight(weightValue = 100f, measure = measureGram)
                )
            )
        )
    }

    should("Transport -> Common maps nested fields and collections") {
        val expected = sampleDish()
        val actual = expected.toContext()

        actual.id.value shouldBe expected.id.toString()
        actual.title shouldBe expected.title
        actual.calories.title shouldBe expected.calories.title
        actual.proteins.title shouldBe expected.proteins.title
        actual.fats.title shouldBe expected.fats.title
        actual.carbohydrates.title shouldBe expected.carbohydrates.title
        actual.weight.value shouldBe expected.weight.weightValue.toDouble()
        actual.author.authorId.value shouldBe expected.author?.id.toString()
        actual.categories.value.map { it.value } shouldBe expected.categories
        actual.recipes shouldBe (expected.recipes ?: emptyList())
        actual.products.size shouldBe (expected.products?.size ?: 0)
        actual.products.map { it.productId.value } shouldBe (expected.products?.map { it.productId.toString() } ?: emptyList())
    }

    should("Common -> Transport roundtrip preserves semantic values") {
        val expected = sampleDish()
        val context: CommonDish = expected.toContext()
        val actual: TransportDish = context.toMeasureTranslation()

        // Note: measure code is lost in roundtrip since it's no longer part of BeMeasureTranslation
        // So we check semantic values instead of full equality
        actual.id shouldBe expected.id
        actual.title shouldBe expected.title
        actual.author?.id shouldBe expected.author?.id
        actual.categories shouldBe expected.categories
        actual.recipes shouldBe expected.recipes
        actual.products?.map { it.productId } shouldBe expected.products?.map { it.productId }
        actual.products?.map { it.weight.weightValue } shouldBe expected.products?.map { it.weight.weightValue }
    }
})


