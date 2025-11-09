-- Insert test products
-- Product 1: Chicken breast (Куриная грудка)
INSERT INTO products (
    id, name,
    calories_title, calories_short_title, calories_value, calories_measure_name, calories_measure_short_name,
    proteins_title, proteins_short_title, proteins_value, proteins_measure_name, proteins_measure_short_name,
    fats_title, fats_short_title, fats_value, fats_measure_name, fats_measure_short_name,
    carbohydrates_title, carbohydrates_short_title, carbohydrates_value, carbohydrates_measure_name, carbohydrates_measure_short_name,
    weight_value, weight_measure_name, weight_measure_short_name,
    author_id, author_name, author_email
) VALUES (
    '550e8400-e29b-41d4-a716-446655440001', 'Куриная грудка',
    'Калории', 'ккал', 165.0, 'грамм', 'г',
    'Белки', 'Б', 31.0, 'грамм', 'г',
    'Жиры', 'Ж', 3.6, 'грамм', 'г',
    'Углеводы', 'У', 0.0, 'грамм', 'г',
    100.0, 'грамм', 'г',
    '550e8400-e29b-41d4-a716-446655440099', 'Admin', 'admin@example.com'
);

-- Product 2: White rice (Рис белый)
INSERT INTO products (
    id, name,
    calories_title, calories_short_title, calories_value, calories_measure_name, calories_measure_short_name,
    proteins_title, proteins_short_title, proteins_value, proteins_measure_name, proteins_measure_short_name,
    fats_title, fats_short_title, fats_value, fats_measure_name, fats_measure_short_name,
    carbohydrates_title, carbohydrates_short_title, carbohydrates_value, carbohydrates_measure_name, carbohydrates_measure_short_name,
    weight_value, weight_measure_name, weight_measure_short_name,
    author_id, author_name, author_email
) VALUES (
    '550e8400-e29b-41d4-a716-446655440002', 'Рис белый',
    'Калории', 'ккал', 365.0, 'грамм', 'г',
    'Белки', 'Б', 7.5, 'грамм', 'г',
    'Жиры', 'Ж', 0.6, 'грамм', 'г',
    'Углеводы', 'У', 79.0, 'грамм', 'г',
    100.0, 'грамм', 'г',
    '550e8400-e29b-41d4-a716-446655440099', 'Admin', 'admin@example.com'
);

-- Product 3: Olive oil (Оливковое масло)
INSERT INTO products (
    id, name,
    calories_title, calories_short_title, calories_value, calories_measure_name, calories_measure_short_name,
    proteins_title, proteins_short_title, proteins_value, proteins_measure_name, proteins_measure_short_name,
    fats_title, fats_short_title, fats_value, fats_measure_name, fats_measure_short_name,
    carbohydrates_title, carbohydrates_short_title, carbohydrates_value, carbohydrates_measure_name, carbohydrates_measure_short_name,
    weight_value, weight_measure_name, weight_measure_short_name,
    author_id, author_name, author_email
) VALUES (
    '550e8400-e29b-41d4-a716-446655440003', 'Оливковое масло',
    'Калории', 'ккал', 884.0, 'грамм', 'г',
    'Белки', 'Б', 0.0, 'грамм', 'г',
    'Жиры', 'Ж', 100.0, 'грамм', 'г',
    'Углеводы', 'У', 0.0, 'грамм', 'г',
    100.0, 'грамм', 'г',
    '550e8400-e29b-41d4-a716-446655440099', 'Admin', 'admin@example.com'
);

-- Insert categories for Product 1: Chicken breast
INSERT INTO product_categories (product_id, category) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Мясо'),
    ('550e8400-e29b-41d4-a716-446655440001', 'Птица'),
    ('550e8400-e29b-41d4-a716-446655440001', 'Белковые продукты');

-- Insert categories for Product 2: White rice
INSERT INTO product_categories (product_id, category) VALUES
    ('550e8400-e29b-41d4-a716-446655440002', 'Крупы'),
    ('550e8400-e29b-41d4-a716-446655440002', 'Гарнир'),
    ('550e8400-e29b-41d4-a716-446655440002', 'Углеводные продукты');

-- Insert categories for Product 3: Olive oil
INSERT INTO product_categories (product_id, category) VALUES
    ('550e8400-e29b-41d4-a716-446655440003', 'Масла'),
    ('550e8400-e29b-41d4-a716-446655440003', 'Жиры');
