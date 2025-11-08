#!/bin/bash

# Примеры команд для тестирования Simple Food Product API

echo "======================================"
echo "Simple Food Product API - Тестирование"
echo "======================================"
echo ""

# 1. Получить список всех продуктов
echo "1. GET /products - Список всех продуктов:"
curl -s http://localhost:8080/products | jq '.[].productName'
echo ""
echo ""

# 2. Получить продукт по ID
echo "2. GET /products/{id} - Получение продукта по ID:"
curl -s http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001 | jq '.productName'
echo ""
echo ""

# 3. Поиск продуктов
echo "3. POST /products/search - Поиск продуктов:"
curl -s -X POST http://localhost:8080/products/search \
  -H "Content-Type: application/json" \
  -d '{"query":"курин"}' | jq '.[].productName'
echo ""
echo ""

# 4. Создание нового продукта
echo "4. POST /products - Создание нового продукта:"
curl -s -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
  "productId": "550e8400-e29b-41d4-a716-446655440010",
  "productName": "Гречка",
  "productCalories": {
    "title": "Калории",
    "shortTitle": "ккал",
    "nutritionalValue": 343.0,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productProteins": {
    "title": "Белки",
    "shortTitle": "Б",
    "nutritionalValue": 13.0,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productFats": {
    "title": "Жиры",
    "shortTitle": "Ж",
    "nutritionalValue": 3.4,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productCarbohydrates": {
    "title": "Углеводы",
    "shortTitle": "У",
    "nutritionalValue": 71.5,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "weight": {
    "weightValue": 100.0,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "author": {
    "id": "550e8400-e29b-41d4-a716-446655440099",
    "name": "Admin",
    "email": "admin@example.com"
  },
  "categories": ["Крупы", "Гарнир"]
}' | jq '.productName'
echo ""
echo ""

# 5. Обновление продукта
echo "5. PUT /products/{id} - Обновление продукта:"
curl -s -X PUT http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001 \
  -H "Content-Type: application/json" \
  -d '{
  "productId": "550e8400-e29b-41d4-a716-446655440001",
  "productName": "Куриная грудка (обновлено)",
  "productCalories": {
    "title": "Калории",
    "shortTitle": "ккал",
    "nutritionalValue": 165.0,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productProteins": {
    "title": "Белки",
    "shortTitle": "Б",
    "nutritionalValue": 31.0,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productFats": {
    "title": "Жиры",
    "shortTitle": "Ж",
    "nutritionalValue": 3.6,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productCarbohydrates": {
    "title": "Углеводы",
    "shortTitle": "У",
    "nutritionalValue": 0.0,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "weight": {
    "weightValue": 100.0,
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "author": {
    "id": "550e8400-e29b-41d4-a716-446655440099",
    "name": "Admin",
    "email": "admin@example.com"
  },
  "categories": ["Мясо", "Птица"]
}' | jq '.productName'
echo ""
echo ""

# 6. Ошибка 404 - продукт не найден
echo "6. GET /products/{invalid-id} - Тест на 404:"
curl -s http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440999 | jq '.code, .message'
echo ""
echo ""

# 7. Ошибка 400 - неверный формат UUID
echo "7. GET /products/{invalid-uuid} - Тест на BAD_REQUEST:"
curl -s http://localhost:8080/products/invalid-uuid | jq '.code, .message'
echo ""
echo ""

# 8. Удаление продукта
echo "8. DELETE /products/{id} - Удаление продукта:"
HTTP_CODE=$(curl -s -o /tmp/delete_response.json -w "%{http_code}" -X DELETE http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440010)
if [ "$HTTP_CODE" = "204" ]; then
    echo "Продукт успешно удален (HTTP $HTTP_CODE)"
elif [ "$HTTP_CODE" = "404" ]; then
    echo "Ошибка: Продукт не найден"
    cat /tmp/delete_response.json | jq '.code, .message'
else
    echo "HTTP Status: $HTTP_CODE"
    if [ -s /tmp/delete_response.json ]; then
        cat /tmp/delete_response.json | jq '.'
    fi
fi
rm -f /tmp/delete_response.json
echo ""
echo ""

echo "======================================"
echo "Тестирование завершено!"
echo "======================================"

