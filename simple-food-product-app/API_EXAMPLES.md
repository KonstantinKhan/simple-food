# Simple Food Product API - Примеры использования

## Обзор
API для работы с продуктами питания согласно спецификации OpenAPI.

## Запуск приложения

```bash
./gradlew :simple-food-product-app:run
```

Приложение будет доступно по адресу: `http://localhost:8080`

## Endpoints

### 1. Получить список всех продуктов
```bash
GET http://localhost:8080/products
```

**Ответ:**
```json
[
  {
    "productId": "550e8400-e29b-41d4-a716-446655440001",
    "productName": "Куриная грудка",
    "productCalories": {
      "title": "Калории",
      "shortTitle": "ккал",
      "measure": {
        "measureName": "грамм",
        "measureShortName": "г"
      }
    },
    "productProteins": { ... },
    "productFats": { ... },
    "productCarbohydrates": { ... },
    "weight": {
      "weightValue": 100.0,
      "measure": { ... }
    },
    "author": {
      "id": "550e8400-e29b-41d4-a716-446655440099",
      "name": "Admin",
      "email": "admin@example.com"
    },
    "categories": ["Мясо", "Птица", "Белковые продукты"]
  }
]
```

### 2. Получить продукт по ID
```bash
GET http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001
```

**Ответ:** 200 OK или 404 Not Found

### 3. Создать новый продукт
```bash
POST http://localhost:8080/products
Content-Type: application/json

{
  "productId": "550e8400-e29b-41d4-a716-446655440010",
  "productName": "Гречка",
  "productCalories": {
    "title": "Калории",
    "shortTitle": "ккал",
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productProteins": {
    "title": "Белки",
    "shortTitle": "Б",
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productFats": {
    "title": "Жиры",
    "shortTitle": "Ж",
    "measure": {
      "measureName": "грамм",
      "measureShortName": "г"
    }
  },
  "productCarbohydrates": {
    "title": "Углеводы",
    "shortTitle": "У",
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
  "categories": ["Крупы", "Гарнир"]
}
```

**Ответ:** 201 Created

### 4. Обновить продукт
```bash
PUT http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001
Content-Type: application/json

{
  "productId": "550e8400-e29b-41d4-a716-446655440001",
  "productName": "Куриная грудка обновленная",
  ...
}
```

**Ответ:** 200 OK или 404 Not Found

### 5. Удалить продукт
```bash
DELETE http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001
```

**Ответ:** 204 No Content или 404 Not Found

### 6. Поиск продуктов
```bash
POST http://localhost:8080/products/search
Content-Type: application/json

{
  "query": "курин"
}
```

**Ответ:** 
```json
[
  {
    "productId": "550e8400-e29b-41d4-a716-446655440001",
    "productName": "Куриная грудка",
    ...
  }
]
```

## Обработка ошибок

Все ошибки возвращаются в стандартном формате:

```json
{
  "code": "NOT_FOUND",
  "message": "Product with id XXX not found"
}
```

Возможные коды ошибок:
- `BAD_REQUEST` - Неверный формат запроса
- `NOT_FOUND` - Продукт не найден
- `INTERNAL_SERVER_ERROR` - Внутренняя ошибка сервера

## Технологии
- Ktor 2.x
- Jackson для сериализации
- OpenAPI спецификация
- In-memory хранилище (ConcurrentHashMap)

## Тестовые данные
При запуске приложения автоматически загружаются 3 тестовых продукта:
1. Куриная грудка (ID: 550e8400-e29b-41d4-a716-446655440001)
2. Рис белый (ID: 550e8400-e29b-41d4-a716-446655440002)
3. Оливковое масло (ID: 550e8400-e29b-41d4-a716-446655440003)

