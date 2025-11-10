# API Reference

## Base URL
```
http://localhost:8080
```

## Endpoints

### Products

#### GET /products
Get list of all products

**Response:** `200 OK`
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Chicken Breast",
    "categories": ["Meat", "Poultry", "Protein Foods"],
    "weight": {
      "value": 100.0,
      "measure": {
        "id": "gram-id",
        "name": "Gram"
      }
    },
    "calories": 165.0,
    "proteins": 31.0,
    "fats": 3.6,
    "carbohydrates": 0.0
  }
]
```

#### POST /products
Create new product

**Request Body:**
```json
{
  "name": "Buckwheat",
  "categories": ["Grains", "Side Dish"],
  "weight": {
    "value": 100.0,
    "measure": {
      "id": "gram-id",
      "name": "Gram"
    }
  },
  "calories": 343.0,
  "proteins": 13.3,
  "fats": 3.4,
  "carbohydrates": 62.1
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440004",
  "name": "Buckwheat",
  ...
}
```

**Errors:**
- `400 Bad Request` - invalid data

#### GET /products/{id}
Get product by ID

**Parameters:**
- `id` (path, UUID) - product ID

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Chicken Breast",
  ...
}
```

**Errors:**
- `400 Bad Request` - invalid UUID
- `404 Not Found` - product not found

#### PUT /products/{id}
Update product

**Parameters:**
- `id` (path, UUID) - product ID

**Request Body:**
```json
{
  "name": "Chicken Breast (updated)",
  "categories": ["Meat", "Poultry"],
  ...
}
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Chicken Breast (updated)",
  ...
}
```

**Errors:**
- `400 Bad Request` - invalid data
- `404 Not Found` - product not found

#### DELETE /products/{id}
Delete product

**Parameters:**
- `id` (path, UUID) - product ID

**Response:** `204 No Content`

**Errors:**
- `404 Not Found` - product not found

#### POST /products/search
Search products

**Request Body:**
```json
{
  "query": "chick",
  "categories": ["Meat"]
}
```

**Response:** `200 OK`
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Chicken Breast",
    ...
  }
]
```

**Features:**
- Case-insensitive search by name
- Filter by categories (optional)
- Empty `query` returns all products

## Error Format

All errors are returned in unified format:

```json
{
  "code": "NOT_FOUND",
  "message": "Product with id 550e8400-e29b-41d4-a716-446655440999 not found",
  "details": []
}
```

### Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| BAD_REQUEST | 400 | Invalid request data |
| NOT_FOUND | 404 | Resource not found |
| INTERNAL_SERVER_ERROR | 500 | Internal server error |

## Usage Examples

### curl

```bash
# Get all products
curl http://localhost:8080/products

# Get product by ID
curl http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001

# Create product
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Buckwheat",
    "categories": ["Grains"],
    "weight": {"value": 100.0, "measure": {"id": "gram", "name": "Gram"}},
    "calories": 343.0,
    "proteins": 13.3,
    "fats": 3.4,
    "carbohydrates": 62.1
  }'

# Search products
curl -X POST http://localhost:8080/products/search \
  -H "Content-Type: application/json" \
  -d '{"query": "chick"}'

# Update product
curl -X PUT http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001 \
  -H "Content-Type: application/json" \
  -d '{...}'

# Delete product
curl -X DELETE http://localhost:8080/products/550e8400-e29b-41d4-a716-446655440001
```

### httpie

```bash
# Get all products
http GET :8080/products

# Search
http POST :8080/products/search query="chick"

# Create
http POST :8080/products name="Buckwheat" categories:='["Grains"]' ...
```

More details: [simple-food-product-app/API_EXAMPLES.md](../simple-food-product-app/API_EXAMPLES.md)
