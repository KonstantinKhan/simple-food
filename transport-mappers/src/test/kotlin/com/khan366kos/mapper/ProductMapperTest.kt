package com.khan366kos.mapper

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.UUID
import com.khan366kos.transport.model.Product as TransportProduct
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.Weight1 as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.common.model.Product as CommonProduct

class ProductMapperTest : FunSpec({

    fun sampleTransportProduct(): TransportProduct = TransportProduct(
        id = UUID.randomUUID(),
        title = "Chicken breast",
        calories = 165f,
        proteins = 31f,
        fats = 3.6f,
        carbohydrates = 0f,
        weight = TransportWeight(`value` = 100f, measure = TransportMeasure.g),
        author = TransportAuthor(id = UUID.randomUUID(), name = "John", email = "john@example.com"),
        type = "meat",
        categories = listOf("protein", "diet")
    )

    test("Transport -> Common mapping maps all fields") {
        val t = sampleTransportProduct()
        val c = t.toCommon()

        c.id.value shouldBe t.id.toString()
        c.title.value shouldBe t.title
        c.calories.value shouldBe t.calories.toDouble()
        c.proteins.value shouldBe t.proteins.toDouble()
        c.fats.value shouldBe t.fats.toDouble()
        c.carbohydrates.value shouldBe t.carbohydrates.toDouble()
        c.weight.value shouldBe t.weight.`value`.toDouble()
        c.weight.measure.value shouldBe c.weight.measure.value
        c.author.id.value shouldBe t.author.id.toString()
        c.type.value shouldBe t.type
        c.categories.map { it.value } shouldBe t.categories
    }

    test("Common -> Transport roundtrip preserves semantic values") {
        val t = sampleTransportProduct()
        val c: CommonProduct = t.toCommon()
        val back: TransportProduct = c.toTransport()

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
    }
})


