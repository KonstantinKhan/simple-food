-- Create products table
CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,

    -- Calories
    calories_title VARCHAR(100),
    calories_short_title VARCHAR(50),
    calories_value DOUBLE PRECISION,
    calories_measure_name VARCHAR(100),
    calories_measure_short_name VARCHAR(20),

    -- Proteins
    proteins_title VARCHAR(100),
    proteins_short_title VARCHAR(50),
    proteins_value DOUBLE PRECISION,
    proteins_measure_name VARCHAR(100),
    proteins_measure_short_name VARCHAR(20),

    -- Fats
    fats_title VARCHAR(100),
    fats_short_title VARCHAR(50),
    fats_value DOUBLE PRECISION,
    fats_measure_name VARCHAR(100),
    fats_measure_short_name VARCHAR(20),

    -- Carbohydrates
    carbohydrates_title VARCHAR(100),
    carbohydrates_short_title VARCHAR(50),
    carbohydrates_value DOUBLE PRECISION,
    carbohydrates_measure_name VARCHAR(100),
    carbohydrates_measure_short_name VARCHAR(20),

    -- Weight
    weight_value DOUBLE PRECISION,
    weight_measure_name VARCHAR(100),
    weight_measure_short_name VARCHAR(20),

    -- Author
    author_id UUID,
    author_name VARCHAR(255),
    author_email VARCHAR(255)
);

-- Create product_categories join table
CREATE TABLE product_categories (
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    category VARCHAR(100) NOT NULL,
    PRIMARY KEY (product_id, category)
);

-- Create indexes for performance
CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_categories_product_id ON product_categories(product_id);
