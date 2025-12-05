# Drift — Lightweight Vert.x Web Framework

**Drift** is a lightweight, convention-over-configuration web framework built on top of **Vert.x**. It provides a ready-to-use structure for building modern web applications with minimal boilerplate, while keeping full control over your code.

> Note: Currently embedded within a Vert.x project. Independent Maven packaging will be available in the future.

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
mvn compile exec:java -Dexec.mainClass="com.rantomah.drift.api.Application"
```

2. **Add a controller**

```java
@MvcController
public class HomeController {

    @Get(path = "/")
    public String home() {
        return "Welcome to Drift!";
    }
}
```

3. **Define a service**

```java
@Service
public class HealthServiceImpl implements HealthService {
    public HealthDto getStatus() {
        return new HealthDto("UP");
    }
}
```

4. **Configure application profiles**

* `application.yml` — default
* `application-dev.yml` — development
* `application-prod.yml` — production

Use the `DRIFT_PROFILE` environment variable or command-line argument to select the profile.

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

## Contributing

1. Fork the repository
2. Create a branch (`feature/new-feature`)
3. Commit your changes
4. Push and create a Pull Request

---

## License

This project is under the **MIT License** --- free for personal and
commercial use.

---

## Author

**Rantomah** [Linkedin](https://www.linkedin.com/in/rantomah)\
Senior Fullstack Developer & Software Architect
