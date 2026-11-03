# Service Specification: API Aggregator Service
**Автор:** Александр Кузив  
**Проект:** <a href="https://github.com/makklays/secure-credit-hub">Secure Credit Hub</a>

## 1. Business Capability & Domain (Бизнес-контекст)
* **Domain (DDD Context):** Относится к ограниченному контексту (Bounded Context) `API Aggregator Service`.
* **Responsibility:** Служит единой точкой входа для микросервисов системы Secure Credit Hub. Обеспечивает сквозную безопасность, маршрутизацию, сквозное логирование (Correlation ID) и устойчивость к сбоям при интеграции с сервисом Валют и Статистики.*

## 2. Architectural Design Decisions (Архитектурные решения)
*Здесь вы говорите как **Software Architect**, объясняя «Почему» вы сделали именно так.*

* **Communication Style:** Синхронный HTTP (WebClient) для критических бизнес-транзакций (Обновление курсов, Сага) и Реактивный неблокирующий для транзакций и авторизации.
* **Resilience Patterns:**
    * `Circuit Breaker` (Резистентность к каскадным падениям целевых сервисов).
    * `Retry` (Повтор запросов при кратковременных сетевых сбоях).
* **Data Isolation:** Сервис является stateless (без собственной базы данных) ИЛИ имеет изолированную БД `gateway_db` (PostgreSQL) для ведения логов интеграционных сбоев (DLQ / Failures).

## 3. API & Integration Contracts (Контракты и Эндпоинты)
*Здесь вы говорите как **Team Lead**, фиксируя правила для разработчиков.*

### HTTP Endpoints

| Method | Path                                                        | Auth Required | Roles | Description                                                                               |
|:-------|:------------------------------------------------------------| :--- |:------|:------------------------------------------------------------------------------------------|
| `GET`  | `/aggregate/v1/currencies/test`                             | Yes | -     | Проверить работу контроллера                                |
| `GET`  | `/aggregate/v1/currencies/base-currencies/{code}/propagate` | Yes | -     | Получить последние 3 кредитные карты (Используя оптимизированную таблицу для чтения CQRS) |
| `GET`  | `/aggregate/v1/payments/test`                               | Yes | -     | Проверить работу контроллера       |
| `GET`  | `/aggregate/v1/payments`                                    | Yes | -     | Проверить работу контроллера       |

### Required Headers (Обязательные заголовки)
* `X-Correlation-ID`: Сквозной UUID для распределенного трассирования. Если не передан клиентом, генерируется шлюзом автоматически (генерируется в Gateway API микросервисе).

## 4. Distributed Transaction Management (Управление транзакциями / Сага)
*Описание логики распределенной транзакции, которую вы написали.*

* **Pattern:** Orchestration-based Saga (Синхронный оркестратор).
* **Happy Path (Успешный сценарий):**
    1. Валидация цен (`buy < sell`).
    2. Бэкап старого курса из `Currencies Service`.
    3. Обновление курса в `Currencies Service`.
    4. Отправка ивэнта `CURRENCY_RATE_UPDATED` в `Statistics Service`.
* **Compensating Transaction (Откат):** При сбое на шаге 3 или 4 вызывается метод `rollbackSaga()`, который возвращает старый курс назад и шлет эвент отката. При тотальном крахе сети инцидент пишется в локальный эндпоинт отказов `/api/v1/integration/failures` (DLQ).

## 5. Observability & Monitoring (Наблюдаемость)
* **Metrics:** Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`).
* **Logs Pattern:** Все логи стандартизированы под формат `[Correlation-ID: {uuid}] [Service: GATEWAY-API] [Message]`.

## 6. Technical Debt & Future Roadmap (Технический долг)
*Показывает вашу зрелость как инженера. Что бы вы улучшили в следующей итерации.*
1. **Temporal Coupling:** Избавиться от блокирующих вызовов `.block()` на путях Саги и полностью перевести её на реактивные цепочки WebFlux.
2. **Asynchronous Saga:** Перевести координацию Саги с синхронного HTTP на асинхронные события через брокер сообщений (Kafka / RabbitMQ), чтобы развязать сервисы во времени.
3. **Caching:** Внедрить Redis для кэширования ответов `/base-currencies`, чтобы снизить нагрузку на сервис Валют.

