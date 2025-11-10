#!/bin/bash
# Create product
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Buckwheat",
    "categories": ["Grains"],
    "weight": {
      "weightValue": 100.0,
      "measure": {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "code": "GRAM",
        "measureName": "Gram",
        "measureShortName": "g"
      }
    },
    "productCalories": {
      "title": "Calories",
      "shortTitle": "kcal",
      "nutritionalValue": 343.0,
      "measure": {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "code": "GRAM",
        "measureName": "Gram",
        "measureShortName": "g"
      }
    },
    "productProteins": {
      "title": "Proteins",
      "shortTitle": "g",
      "nutritionalValue": 13.3,
      "measure": {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "code": "GRAM",
        "measureName": "Gram",
        "measureShortName": "g"
      }
    },
    "productFats": {
      "title": "Fats",
      "shortTitle": "g",
      "nutritionalValue": 3.4,
      "measure": {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "code": "GRAM",
        "measureName": "Gram",
        "measureShortName": "g"
      }
    },
    "productCarbohydrates": {
      "title": "Carbohydrates",
      "shortTitle": "g",
      "nutritionalValue": 62.1,
      "measure": {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "code": "GRAM",
        "measureName": "Gram",
        "measureShortName": "g"
      }
    },
    "author": {
      "id": "550e8400-e29b-41d4-a716-446655440020",
      "name": "John Doe",
      "email": "john@example.com"
    }
  }'

