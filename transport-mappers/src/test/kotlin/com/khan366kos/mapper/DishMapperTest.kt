package com.khan366kos.mapper

import io.kotest.core.spec.style.FunSpec
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
import com.khan366kos.common.model.BeDish as CommonDish

class DishMapperTest : FunSpec({

    fun sampleDish(): TransportDish {
        val measureGram = TransportMeasure(measureName = "gram", measureShortName = "g")
        fun nv(t: String, s: String) = TransportNutritionalValue(title = t, shortTitle = s, measure = measureGram)
        return TransportDish(
            id = UUID.randomUUID(),
            title = "Chicken salad",
            calories = nv("Calories", "kcal"),
            proteins = nv("Proteins", "g"),
            fats = nv("Fats", "g"),
            carbohydrates = nv("Carbohydrates", "g"),
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

    test("Transport -> Common maps nested fields and collections") {
        val t = sampleDish()
        val c = t.toContext()

        c.id.value shouldBe t.id.toString()
        c.title shouldBe t.title
        c.calories.title shouldBe t.calories.title
        c.proteins.title shouldBe t.proteins.title
        c.fats.title shouldBe t.fats.title
        c.carbohydrates.title shouldBe t.carbohydrates.title
        c.weight.value shouldBe t.weight.weightValue.toDouble()
        c.author.authorId.value shouldBe t.author?.id.toString()
        c.categories.value.map { it.value } shouldBe t.categories
        c.recipes shouldBe (t.recipes ?: emptyList())
        c.products.size shouldBe (t.products?.size ?: 0)
        c.products.map { it.productId.value } shouldBe (t.products?.map { it.productId.toString() } ?: emptyList())
    }

    test("Common -> Transport roundtrip preserves semantic values") {
        val t = sampleDish()
        val c: CommonDish = t.toContext()
        val back: TransportDish = c.toTransport()

        back shouldBe t
        back.author?.id shouldBe t.author?.id
        back.categories shouldBe t.categories
        back.recipes shouldBe t.recipes
        back.products?.map { it.productId } shouldBe t.products?.map { it.productId }
        back.products?.map { it.weight.weightValue } shouldBe t.products?.map { it.weight.weightValue }
    }
})


