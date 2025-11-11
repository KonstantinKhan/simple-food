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
