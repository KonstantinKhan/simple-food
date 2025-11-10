# Анализ документации проекта

## Таблица файлов документации

| Название файла | Количество строк | Связь с CLAUDE.md |
|----------------|------------------|-------------------|
| CLAUDE.md | 156 | Корневой файл |
| docs/DEVELOPMENT.md | 269 | Связан напрямую |
| docs/ARCHITECTURE.md | 129 | Связан напрямую |
| docs/API_REFERENCE.md | 112 | Связан напрямую |
| VSCODE_SETUP.md | 116 | Связан через docs/DEVELOPMENT.md |
| plans/MVP_PRODUCTION_READY_PLAN.md | 98 | Не связан |
| README_SETUP.md | 68 | Не связан |
| docs/examples/curl/put-products-id.sh | 68 | Связан через docs/examples/curl-examples.md → docs/API_REFERENCE.md |
| docs/examples/curl/post-products.sh | 67 | Связан через docs/examples/curl-examples.md → docs/API_REFERENCE.md |
| docs/examples/responses/post-products-search-200.json | 66 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/responses/get-products-200.json | 66 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/responses/put-products-id-200.json | 64 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/responses/post-products-201.json | 64 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/responses/get-products-id-200.json | 64 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/requests/put-products-id.json | 64 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/requests/post-products.json | 63 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/curl-examples.md | 25 | Связан через docs/API_REFERENCE.md |
| README.md | 45 | Не связан |
| docs/examples/curl/post-products-search.sh | 6 | Связан через docs/examples/curl-examples.md → docs/API_REFERENCE.md |
| docs/examples/responses/error-format.json | 6 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/requests/post-products-search.json | 4 | Связан напрямую через docs/API_REFERENCE.md |
| docs/examples/curl/get-products.sh | 4 | Связан через docs/examples/curl-examples.md → docs/API_REFERENCE.md |
| docs/examples/curl/get-products-id.sh | 4 | Связан через docs/examples/curl-examples.md → docs/API_REFERENCE.md |
| docs/examples/curl/delete-products-id.sh | 4 | Связан через docs/examples/curl-examples.md → docs/API_REFERENCE.md |

## Статистика

- **Всего файлов:** 23
- **Связаны напрямую с CLAUDE.md:** 3 файла
- **Связаны через промежуточные файлы:** 18 файлов
- **Не связаны:** 3 файла (README.md, README_SETUP.md, plans/MVP_PRODUCTION_READY_PLAN.md)
- **Общее количество строк:** 1,632

## Граф связей

```
CLAUDE.md
├── docs/ARCHITECTURE.md (напрямую)
├── docs/DEVELOPMENT.md (напрямую)
│   └── VSCODE_SETUP.md
└── docs/API_REFERENCE.md (напрямую)
    ├── docs/examples/curl-examples.md
    │   ├── docs/examples/curl/get-products.sh
    │   ├── docs/examples/curl/get-products-id.sh
    │   ├── docs/examples/curl/post-products.sh
    │   ├── docs/examples/curl/post-products-search.sh
    │   ├── docs/examples/curl/put-products-id.sh
    │   └── docs/examples/curl/delete-products-id.sh
    ├── docs/examples/requests/post-products.json
    ├── docs/examples/requests/put-products-id.json
    ├── docs/examples/requests/post-products-search.json
    ├── docs/examples/responses/get-products-200.json
    ├── docs/examples/responses/get-products-id-200.json
    ├── docs/examples/responses/post-products-201.json
    ├── docs/examples/responses/post-products-search-200.json
    ├── docs/examples/responses/put-products-id-200.json
    └── docs/examples/responses/error-format.json
```

