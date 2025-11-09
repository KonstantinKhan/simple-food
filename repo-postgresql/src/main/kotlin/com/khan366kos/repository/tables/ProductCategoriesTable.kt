package com.khan366kos.repository.tables

import org.jetbrains.exposed.sql.Table

/**
 * Exposed table definition for product categories junction table
 */
object ProductCategoriesTable : Table("product_categories") {
    val productId = uuid("product_id").references(ProductsTable.id, onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE)
    val category = varchar("category", 100)

    override val primaryKey = PrimaryKey(productId, category)
}
