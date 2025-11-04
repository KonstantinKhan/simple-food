package com.khan366kos.mapper

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.UUID
import com.khan366kos.transport.model.Dish as TransportDish
import com.khan366kos.transport.model.ProductPortion as TransportProductPortion
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight1 as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.common.model.Dish as CommonDish

class DishMapperTest : FunSpec({

    fun sampleDish(): TransportDish = TransportDish(
        id = UUID.randomUUID(),
        title = "Chicken salad",
        calories = 210f,
        proteins = 28f,
        fats = 8f,
        carbohydrates = 6f,
        weight = TransportWeight(`value` = 250f, measure = TransportMeasure.g),
        author = TransportAuthor(id = UUID.randomUUID(), name = "John", email = "john@example.com"),
        type = "salad",
        categories = listOf("healthy", "lunch"),
        recipes = listOf("Cut", "Mix", "Serve"),
        products = listOf(
            TransportProductPortion(
                productId = UUID.randomUUID(),
                weight = TransportWeight(`value` = 150f, measure = TransportMeasure.g)
            ),
            TransportProductPortion(
                productId = UUID.randomUUID(),
                weight = TransportWeight(`value` = 100f, measure = TransportMeasure.g)
            )
        )
    )

    test("Transport -> Common maps nested fields and collections") {
        val t = sampleDish()
        val c = t.toCommon()

        c.id.value shouldBe t.id.toString()
        c.title.value shouldBe t.title
        c.calories.value shouldBe t.calories.toDouble()
        c.proteins.value shouldBe t.proteins.toDouble()
        c.fats.value shouldBe t.fats.toDouble()
        c.carbohydrates.value shouldBe t.carbohydrates.toDouble()
        c.weight.value shouldBe t.weight.`value`.toDouble()
        c.author.id.value shouldBe t.author.id.toString()
        c.type.value shouldBe t.type
        c.categories.map { it.value } shouldBe t.categories
        c.recipes.map { it.value } shouldBe t.recipes
        c.products.size shouldBe t.products.size
        c.products.map { it.productId.value } shouldBe t.products.map { it.productId.toString() }
    }

    test("Common -> Transport roundtrip preserves semantic values") {
        val t = sampleDish()
        val c: CommonDish = t.toCommon()
        val back: TransportDish = c.toTransport()

        back.id shouldBe t.id
        back.title shouldBe t.title
        back.calories shouldBe t.calories
        back.proteins shouldBe t.proteins
        back.fats shouldBe t.fats
        back.carbohydrates shouldBe t.carbohydrates
        back.weight.`value` shouldBe t.weight.`value`
        back.weight.measure shouldBe t.weight.measure
        back.author.id shouldBe t.author.id
        back.type shouldBe t.type
        back.categories shouldBe t.categories
        back.recipes shouldBe t.recipes
        back.products.map { it.productId } shouldBe t.products.map { it.productId }
        back.products.map { it.weight.`value` } shouldBe t.products.map { it.weight.`value` }
    }
})


