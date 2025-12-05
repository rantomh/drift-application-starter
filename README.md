# Drift — Lightweight Vert.x Web Framework

**Drift** is a lightweight, convention-over-configuration web framework built on top of **Vert.x**. It provides a ready-to-use structure for building modern web applications with minimal boilerplate, while keeping full control over your code.

> Note: Currently embedded within a Vert.x starter project. Independent Maven packaging will be available soon.

---

## Features

* ✅ **Convention over configuration** — sensible defaults for rapid development.
* ✅ **Lightweight and modular** — small footprint, easy to extend.
* ✅ **Vert.x based** — fully asynchronous, reactive web framework.
* ✅ **Structured and organized** — clear package separation for controllers, services, DTOs, events, and more.
* ✅ **Annotations-driven** — easy HTTP mapping and DI support.
* ✅ **Built-in i18n and configuration management** — YAML-based config files, environment profiles, and UTF-8 resource support.

---

## Project Structure

```
src/main/java/com/rantomah/drift
├── api
│   ├── Application.java
│   ├── controller
│   ├── dto
│   ├── event
│   ├── mapper
│   ├── model
│   └── service
│
├── framework
│   ├── annotation
│   ├── core
│   ├── di
│   ├── mapping
│   ├── stereotype
│   ├── event
│   ├── exception
│   ├── impl
│   └── web
│       └── handler
│
├── resources
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-prod.yml
│   ├── i18n
│   ├── public
│   └── templates
│
└── test
    └── java/com/rantomah/drift
        └── TestWebVerticle.java
```

---

## Quick Start

1. **Run the application**

```bash
mvnw -Ddrift.profile=dev -DskipTests -Dexec.mainClass="com.rantomah.drift.api.Application" exec:java
```

2. **Sample controller**

```java
@Controller
public class HealthController extends AbstractController {

    @Inject
    private HealthService healthService;

    @Get(path = "/api/health")
    public Future<Response<HealthDto>> health(RoutingContext ctx) {
        return healthService.getHealth().map(this::success);
    }
}
```

3. **Sample MVC controller**

```java
@MvcController
public class HomeController {

    @Property("app.version")
    private String version;

    @Get
    public Future<String> home(RoutingContext ctx) {
        String message = I18n.t(ctx, "message.home", version);
        ctx.put("message", message);
        return Future.succeededFuture("index");
    }
}
```

4. **Sample service**

```java
@Service
public class HealthServiceImpl implements HealthService {

    @Inject
    private Environment environment;

    @Inject
    private EventPublisher eventPublisher;

    @Override
    public Future<HealthDto> getHealth() {
        Health health = new Health();
        health.setStatus("UP");
        health.setVersion(environment.getProperty("app.version"));
        eventPublisher.publish(new TestEvent("admin", "admin@drift.io")); // automaticaly async
        return Future.succeededFuture(HealthMapper.INSTANCE.toDto(health));
    }
}
```

5. **Configure application profiles**

* `application.yml` — default
* `application-dev.yml` — development
* `application-prod.yml` — production

Use the `DRIFT_PROFILE` environment variable or `drift.profile` property to select the profile.

---

## Dependency Injection

```java
@Service
public class MyService {

    @Inject
    private SomeHelper helper;
}
```

---

## Routing & HTTP Mapping

* `@Request` — custom HTTP mapping
* `@Get`, `@Post`, `@Put`, `@Patch`, `@Delete` — RESTful mappings
* Works seamlessly with MVC controllers using `@MvcController`.

---

## Events

* Event-driven programming supported via `@EventListener` and `EventPublisher`.
* Define events and listeners in `framework/event`.

---

## i18n & Localization

* Place `.properties` files in `resources/i18n`.
* Automatic UTF-8 loading with `I18n` helper.
* Locale detection via `LocaleHandler`.

---

## Logging

* Logback-based logging (`logback.xml`)
* Configurable per environment.

---

## License

This project is under the **MIT License** --- free for personal and
commercial use.

---

## Author

**Rantomah** [Linkedin](https://www.linkedin.com/in/rantomah)\
Senior Fullstack Developer & Software Architect
