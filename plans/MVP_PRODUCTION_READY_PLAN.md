# Production-Ready MVP Plan (10-12 days)

## Phase 1: Security Foundation (3-4 days)
**Day 1-2: JWT Authentication**
- Add `ktor-server-auth-jwt` dependency
- Create JWT configuration (secret, issuer, audience)
- Implement `/auth/login` endpoint (returns JWT token)
- Create JWT validation middleware
- Protect all API endpoints (Products, Measures, future Dishes)
- Add user model & repository (in-memory for MVP)

**Day 3: Security Hardening**
- Fix CORS: restrict to specific origins (configurable via env)
- Add Ktor rate limiting plugin
- Implement input validation with `ktor-server-request-validation`
- Add security headers (X-Frame-Options, CSP, etc.)
- Move secrets to environment variables

## Phase 2: Dishes API Implementation (2-3 days)
**Day 4-5: Backend Implementation**
- Create BeDish, BeDishIngredient models (already in common-models)
- Implement DishRepository interface & PostgreSQL implementation
- Add Flyway migration for dishes tables
- Create in-memory repository implementation
- Write repository unit tests

**Day 6: Routes & Integration**
- Implement DishRoutes (CRUD + search)
- Verify mappers (DishMapperTest exists)
- Add JWT protection to endpoints
- Integration testing

## Phase 3: Observability (1-2 days)
**Day 7: Health Checks & Logging**
- Add `/health` endpoint (checks DB connection)
- Add `/ready` and `/alive` endpoints
- Enhance logging: add correlation IDs
- Add request/response logging middleware
- Configure structured logging format

**Day 8: Basic Metrics (Optional)**
- Add Micrometer core dependency
- Expose `/metrics` endpoint
- Track: request count, response times, error rates

## Phase 4: Deployment (2 days)
**Day 9: Docker Setup**
- Create multi-stage Dockerfile (build + runtime)
- Create docker-compose.yml (app + PostgreSQL)
- Add .dockerignore
- Configure environment variables
- Test local container deployment

**Day 10: Production Config**
- Create production application.conf
- Add startup scripts
- Document deployment steps
- Create backup/restore scripts
- Write troubleshooting guide

## Testing & Polish (1-2 days)
**Day 11-12: Integration & Load Testing**
- Write integration tests for all endpoints
- Test authentication flow end-to-end
- Basic load testing (100-1000 req/s)
- Security audit checklist
- Update API documentation

---

## Deliverables
✅ JWT authentication with user management
✅ Complete Dishes API implementation
✅ Docker Compose deployment setup
✅ Health checks for monitoring
✅ Rate limiting & input validation
✅ Production-ready configuration
✅ Integration test suite
✅ Deployment documentation

## Out of Scope (Post-MVP)
❌ Swagger UI (can add in 1 day later)
❌ Advanced monitoring dashboard
❌ CI/CD pipeline
❌ Auto-scaling & clustering

**Total Estimated Time:** 10-12 working days
**Risk Level:** Medium (good security, basic observability)

---

## Configuration Summary
- **Authentication:** JWT with /auth/login endpoint
- **Deployment:** Docker Compose (app + PostgreSQL)
- **Included APIs:** Products, Measures, Dishes (complete implementation)
- **Security:** JWT auth, rate limiting, input validation, security headers, CORS restrictions
- **Observability:** Health checks, structured logging, request tracing
- **Testing:** Repository unit tests + integration tests for all endpoints
