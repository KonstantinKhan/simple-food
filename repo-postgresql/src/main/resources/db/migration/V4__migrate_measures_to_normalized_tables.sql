-- Insert common measures with translations
-- GRAM measure
INSERT INTO measures (id, code, created_at)
VALUES ('00000000-0000-0000-0000-000000000001', 'GRAM', CURRENT_TIMESTAMP);

INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'ru', 'грамм', 'г'),
    ('00000000-0000-0000-0000-000000000001', 'en', 'gram', 'g');

-- KILOGRAM measure
INSERT INTO measures (id, code, created_at)
VALUES ('00000000-0000-0000-0000-000000000002', 'KILOGRAM', CURRENT_TIMESTAMP);

INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ('00000000-0000-0000-0000-000000000002', 'ru', 'килограмм', 'кг'),
    ('00000000-0000-0000-0000-000000000002', 'en', 'kilogram', 'kg');

-- LITER measure
INSERT INTO measures (id, code, created_at)
VALUES ('00000000-0000-0000-0000-000000000003', 'LITER', CURRENT_TIMESTAMP);

INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ('00000000-0000-0000-0000-000000000003', 'ru', 'литр', 'л'),
    ('00000000-0000-0000-0000-000000000003', 'en', 'liter', 'l');

-- MILLILITER measure
INSERT INTO measures (id, code, created_at)
VALUES ('00000000-0000-0000-0000-000000000004', 'MILLILITER', CURRENT_TIMESTAMP);

INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ('00000000-0000-0000-0000-000000000004', 'ru', 'миллилитр', 'мл'),
    ('00000000-0000-0000-0000-000000000004', 'en', 'milliliter', 'ml');

-- PIECE measure
INSERT INTO measures (id, code, created_at)
VALUES ('00000000-0000-0000-0000-000000000005', 'PIECE', CURRENT_TIMESTAMP);

INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ('00000000-0000-0000-0000-000000000005', 'ru', 'штука', 'шт'),
    ('00000000-0000-0000-0000-000000000005', 'en', 'piece', 'pc');

-- SERVING measure
INSERT INTO measures (id, code, created_at)
VALUES ('00000000-0000-0000-0000-000000000006', 'SERVING', CURRENT_TIMESTAMP);

INSERT INTO measure_translations (measure_id, locale, measure_name, measure_short_name)
VALUES
    ('00000000-0000-0000-0000-000000000006', 'ru', 'порция', 'порц'),
    ('00000000-0000-0000-0000-000000000006', 'en', 'serving', 'serv');

-- Add measure_id columns to products table
ALTER TABLE products ADD COLUMN calories_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN proteins_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN fats_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN carbohydrates_measure_id UUID REFERENCES measures(id);
ALTER TABLE products ADD COLUMN weight_measure_id UUID REFERENCES measures(id);

-- Migrate existing data: Set all measure_ids to GRAM (since all existing test data uses "грамм"/"г")
UPDATE products SET
    calories_measure_id = '00000000-0000-0000-0000-000000000001',
    proteins_measure_id = '00000000-0000-0000-0000-000000000001',
    fats_measure_id = '00000000-0000-0000-0000-000000000001',
    carbohydrates_measure_id = '00000000-0000-0000-0000-000000000001',
    weight_measure_id = '00000000-0000-0000-0000-000000000001';

-- Drop old denormalized measure columns
ALTER TABLE products DROP COLUMN calories_measure_name;
ALTER TABLE products DROP COLUMN calories_measure_short_name;
ALTER TABLE products DROP COLUMN proteins_measure_name;
ALTER TABLE products DROP COLUMN proteins_measure_short_name;
ALTER TABLE products DROP COLUMN fats_measure_name;
ALTER TABLE products DROP COLUMN fats_measure_short_name;
ALTER TABLE products DROP COLUMN carbohydrates_measure_name;
ALTER TABLE products DROP COLUMN carbohydrates_measure_short_name;
ALTER TABLE products DROP COLUMN weight_measure_name;
ALTER TABLE products DROP COLUMN weight_measure_short_name;

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
