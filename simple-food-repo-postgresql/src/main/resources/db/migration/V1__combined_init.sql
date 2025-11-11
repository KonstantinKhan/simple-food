-- Combined migration: Create schema and seed data with random UUIDs

-- Create products table
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,

    -- Calories
    calories_title VARCHAR(100),
    calories_short_title VARCHAR(50),
    calories_value DOUBLE PRECISION,

    -- Proteins
    proteins_title VARCHAR(100),
    proteins_short_title VARCHAR(50),
    proteins_value DOUBLE PRECISION,

    -- Fats
    fats_title VARCHAR(100),
    fats_short_title VARCHAR(50),
    fats_value DOUBLE PRECISION,

    -- Carbohydrates
    carbohydrates_title VARCHAR(100),
    carbohydrates_short_title VARCHAR(50),
    carbohydrates_value DOUBLE PRECISION,

    -- Weight
    weight_value DOUBLE PRECISION,

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

-- Insert test products with random UUIDs
-- Product 1: Chicken breast (Куриная грудка)
INSERT INTO products (
    name,
    calories_title, calories_short_title, calories_value,
    proteins_title, proteins_short_title, proteins_value,
    fats_title, fats_short_title, fats_value,
    carbohydrates_title, carbohydrates_short_title, carbohydrates_value,
    weight_value,
    author_id, author_name, author_email
) VALUES (
    'Куриная грудка',
    'Калорийность', 'К', 165.0,
    'Белки', 'Б', 31.0,
    'Жиры', 'Ж', 3.6,
    'Углеводы', 'У', 0.0,
    100.0,
    gen_random_uuid(), 'Admin', 'admin@example.com'
);

-- Product 2: White rice (Рис белый)
INSERT INTO products (
    name,
    calories_title, calories_short_title, calories_value,
    proteins_title, proteins_short_title, proteins_value,
    fats_title, fats_short_title, fats_value,
    carbohydrates_title, carbohydrates_short_title, carbohydrates_value,
    weight_value,
    author_id, author_name, author_email
) VALUES (
    'Рис белый',
    'Калорийность', 'К', 365.0,
    'Белки', 'Б', 7.5,
    'Жиры', 'Ж', 0.6,
    'Углеводы', 'У', 79.0,
    100.0,
    gen_random_uuid(), 'Admin', 'admin@example.com'
);

-- Product 3: Olive oil (Оливковое масло)
INSERT INTO products (
    name,
    calories_title, calories_short_title, calories_value,
    proteins_title, proteins_short_title, proteins_value,
    fats_title, fats_short_title, fats_value,
    carbohydrates_title, carbohydrates_short_title, carbohydrates_value,
    weight_value,
    author_id, author_name, author_email
) VALUES (
    'Оливковое масло',
    'Калорийность', 'К', 884.0,
    'Белки', 'Б', 0.0,
    'Жиры', 'Ж', 100.0,
    'Углеводы', 'У', 0.0,
    100.0,
    gen_random_uuid(), 'Admin', 'admin@example.com'
);

-- Insert categories for Product 1: Chicken breast
INSERT INTO product_categories (product_id, category) VALUES
    ((SELECT id FROM products WHERE name = 'Куриная грудка' LIMIT 1), 'Мясо'),
    ((SELECT id FROM products WHERE name = 'Куриная грудка' LIMIT 1), 'Птица'),
    ((SELECT id FROM products WHERE name = 'Куриная грудка' LIMIT 1), 'Белковые продукты');

-- Insert categories for Product 2: White rice
INSERT INTO product_categories (product_id, category) VALUES
    ((SELECT id FROM products WHERE name = 'Рис белый' LIMIT 1), 'Крупы'),
    ((SELECT id FROM products WHERE name = 'Рис белый' LIMIT 1), 'Гарнир'),
    ((SELECT id FROM products WHERE name = 'Рис белый' LIMIT 1), 'Углеводные продукты');

-- Insert categories for Product 3: Olive oil
INSERT INTO product_categories (product_id, category) VALUES
    ((SELECT id FROM products WHERE name = 'Оливковое масло' LIMIT 1), 'Масла'),
    ((SELECT id FROM products WHERE name = 'Оливковое масло' LIMIT 1), 'Жиры');

-- Create measures table
CREATE TABLE measures (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create measure_translations table
CREATE TABLE measure_translations (
    measure_id UUID NOT NULL REFERENCES measures(id) ON DELETE CASCADE,
    locale VARCHAR(10) NOT NULL,
    measure_name VARCHAR(255) NOT NULL,
    measure_short_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (measure_id, locale)
);

-- Create indexes for performance
CREATE INDEX idx_measures_code ON measures(code);
CREATE INDEX idx_measure_translations_locale ON measure_translations(locale);
CREATE INDEX idx_measure_translations_measure_id ON measure_translations(measure_id);

-- Insert common measures with translations
-- GRAM measure
INSERT INTO measures (code, created_at) VALUES ('GRAM', CURRENT_TIMESTAMP);
INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ((SELECT id FROM measures WHERE code = 'GRAM' LIMIT 1), 'ru', 'грамм', 'г'),
    ((SELECT id FROM measures WHERE code = 'GRAM' LIMIT 1), 'en', 'gram', 'g');

-- KILOGRAM measure
INSERT INTO measures (code, created_at) VALUES ('KILOGRAM', CURRENT_TIMESTAMP);
INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ((SELECT id FROM measures WHERE code = 'KILOGRAM' LIMIT 1), 'ru', 'килограмм', 'кг'),
    ((SELECT id FROM measures WHERE code = 'KILOGRAM' LIMIT 1), 'en', 'kilogram', 'kg');

-- LITER measure
INSERT INTO measures (code, created_at) VALUES ('LITER', CURRENT_TIMESTAMP);
INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ((SELECT id FROM measures WHERE code = 'LITER' LIMIT 1), 'ru', 'литр', 'л'),
    ((SELECT id FROM measures WHERE code = 'LITER' LIMIT 1), 'en', 'liter', 'l');

-- MILLILITER measure
INSERT INTO measures (code, created_at) VALUES ('MILLILITER', CURRENT_TIMESTAMP);
INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ((SELECT id FROM measures WHERE code = 'MILLILITER' LIMIT 1), 'ru', 'миллилитр', 'мл'),
    ((SELECT id FROM measures WHERE code = 'MILLILITER' LIMIT 1), 'en', 'milliliter', 'ml');

-- PIECE measure
INSERT INTO measures (code, created_at) VALUES ('PIECE', CURRENT_TIMESTAMP);
INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ((SELECT id FROM measures WHERE code = 'PIECE' LIMIT 1), 'ru', 'штука', 'шт'),
    ((SELECT id FROM measures WHERE code = 'PIECE' LIMIT 1), 'en', 'piece', 'pc');

-- SERVING measure
INSERT INTO measures (code, created_at) VALUES ('SERVING', CURRENT_TIMESTAMP);
INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ((SELECT id FROM measures WHERE code = 'SERVING' LIMIT 1), 'ru', 'порция', 'порц'),
    ((SELECT id FROM measures WHERE code = 'SERVING' LIMIT 1), 'en', 'serving', 'serv');

-- KILOCALORIE measure (for energy/calories)
INSERT INTO measures (code, created_at) VALUES ('KILOCALORIE', CURRENT_TIMESTAMP);
INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ((SELECT id FROM measures WHERE code = 'KILOCALORIE' LIMIT 1), 'ru', 'килокалория', 'ккал'),
    ((SELECT id FROM measures WHERE code = 'KILOCALORIE' LIMIT 1), 'en', 'kilocalorie', 'kcal');

-- Add measure_id columns to products table
ALTER TABLE products ADD COLUMN calories_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN proteins_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN fats_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN carbohydrates_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN weight_measure_id UUID REFERENCES measures(id);

-- Migrate existing data:
-- Calories use KILOCALORIE, nutrients (proteins/fats/carbs) and weight use GRAM
UPDATE products SET
    calories_measure_id = (SELECT id FROM measures WHERE code = 'KILOCALORIE' LIMIT 1),
    proteins_measure_id = (SELECT id FROM measures WHERE code = 'GRAM' LIMIT 1),
    fats_measure_id = (SELECT id FROM measures WHERE code = 'GRAM' LIMIT 1),
    carbohydrates_measure_id = (SELECT id FROM measures WHERE code = 'GRAM' LIMIT 1),
    weight_measure_id = (SELECT id FROM measures WHERE code = 'GRAM' LIMIT 1);

-- Make measure_id columns NOT NULL now that they're populated
ALTER TABLE products ALTER COLUMN calories_measure_id SET NOT NULL;
ALTER TABLE products ALTER COLUMN proteins_measure_id SET NOT NULL;
ALTER TABLE products ALTER COLUMN fats_measure_id SET NOT NULL;
ALTER TABLE products ALTER COLUMN carbohydrates_measure_id SET NOT NULL;
ALTER TABLE products ALTER COLUMN weight_measure_id SET NOT NULL;

-- Create indexes for performance
CREATE INDEX idx_products_calories_measure_id ON products(calories_measure_id);
CREATE INDEX idx_products_proteins_measure_id ON products(proteins_measure_id);
CREATE INDEX idx_products_fats_measure_id ON products(fats_measure_id);
CREATE INDEX idx_products_carbohydrates_measure_id ON products(carbohydrates_measure_id);
CREATE INDEX idx_products_weight_measure_id ON products(weight_measure_id);