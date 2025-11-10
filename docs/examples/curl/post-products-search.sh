#!/bin/bash
# Search products
curl -X POST http://localhost:8080/products/search \
  -H "Content-Type: application/json" \
  -d '{"query": "chick"}'

