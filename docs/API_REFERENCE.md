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

See example response: [examples/responses/get-products-200.json](examples/responses/get-products-200.json)

#### POST /products
Create new product

**Request Body:**

See example request: [examples/requests/post-products.json](examples/requests/post-products.json)

**Response:** `201 Created`

See example response: [examples/responses/post-products-201.json](examples/responses/post-products-201.json)

**Errors:**
- `400 Bad Request` - invalid data

#### GET /products/{id}
Get product by ID

**Parameters:**
- `id` (path, UUID) - product ID

**Response:** `200 OK`

See example response: [examples/responses/get-products-id-200.json](examples/responses/get-products-id-200.json)

**Errors:**
- `400 Bad Request` - invalid UUID
- `404 Not Found` - product not found

#### PUT /products/{id}
Update product

**Parameters:**
- `id` (path, UUID) - product ID

**Request Body:**

See example request: [examples/requests/put-products-id.json](examples/requests/put-products-id.json)

**Response:** `200 OK`

See example response: [examples/responses/put-products-id-200.json](examples/responses/put-products-id-200.json)

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

See example request: [examples/requests/post-products-search.json](examples/requests/post-products-search.json)

**Response:** `200 OK`

See example response: [examples/responses/post-products-search-200.json](examples/responses/post-products-search-200.json)

**Features:**
- Case-insensitive search by name and categories
- Free-text query over title and categories
- Empty `query` returns all products

## Error Format

All errors are returned in unified format:

See example error format: [examples/responses/error-format.json](examples/responses/error-format.json)

### Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| BAD_REQUEST | 400 | Invalid request data |
| NOT_FOUND | 404 | Resource not found |
| INTERNAL_SERVER_ERROR | 500 | Internal server error |

## Usage Examples

### curl

See curl examples: [examples/curl-examples.md](examples/curl-examples.md)

More details: [simple-food-product-app/API_EXAMPLES.md](../simple-food-product-app/API_EXAMPLES.md)
