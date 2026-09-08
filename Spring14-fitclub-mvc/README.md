# FitClub — Spring MVC + Spring Data JPA + Spring Core Practice Project

A fitness class booking portal: register -> login -> browse classes ->
book a class -> view your bookings. Built to match your diagrams (Index
page -> Register page -> Success page -> Controller/Service/Repository/DB
flow) with a fresh domain, real MySQL, and all three JUnit test levels.

## How to run locally (no Docker)
1. Have a MySQL server running locally (or change `DB_HOST` env var to point elsewhere).
2. `File -> Open` this folder in IntelliJ, let Maven import finish.
3. Run `FitClubApplication.main()`.
4. Visit `http://localhost:8080` — you'll land on the Index page from your diagram.
5. Run tests: they use embedded H2 automatically (see "Why tests don't need MySQL" below) — no MySQL needed just to run the test suite.

## How to run with Docker (2 separate containers)
```bash
docker compose up --build
```
This builds the app image, starts a `mysql` container and an `app`
container, waits for MySQL's healthcheck to pass, then starts the app
pointed at the `mysql` service by name. Visit `http://localhost:8080`.
```bash
docker compose down          # stop both containers
docker compose down -v       # stop AND delete the MySQL data volume
```

## Why tests don't need MySQL running
`src/test/resources/application.properties` overrides the datasource to
embedded H2 for every test — Java's classpath puts test resources ahead
of main resources automatically. See the comment at the top of that file.

---

## Where each concept from your diagrams/context lives

| Concept | Where |
|---|---|
| Index page -> Register -> Success/Login flow (Images 1-2) | `AuthController` + `index.html`/`register.html`/`login.html` |
| DispatcherServlet -> HandlerMapping -> Controller -> ViewResolver -> View (Image 3, "mav" label) | Comment block in `FitClubApplication.java` |
| Controller <-> Service <-> Repository <-> DB (Images 1-2, right side) | `controller/` -> `service/` -> `repository/` -> MySQL |
| `@Controller`, `@RequestMapping`/`@GetMapping`/`@PostMapping` | Every controller |
| Return type `String` vs `ModelAndView` | `AuthController.home()` (String) vs `AuthController.register()` (ModelAndView) |
| `th:field`, `th:action`, `th:href`, `th:value` | `register.html`, `login.html`, `dashboard.html` |
| Bean Validation (`@NotBlank`, `@Email`, `@Size`) | `MemberRegistrationForm`, `LoginForm` |
| `@Valid` + `BindingResult` | `AuthController.register()` / `.login()` |

## Where Spring Core concepts live (your requested revision)

| Concept | Where |
|---|---|
| IoC container / ApplicationContext | Comment in `FitClubApplication.java` |
| Dependency Injection (constructor injection) | Every service/controller constructor |
| Stereotype annotations (`@Service`, `@Component`, `@Controller`, `@Repository`) | `MemberServiceImpl`, `DataSeeder`, controllers, repository interfaces |
| Coding to an interface (Dependency Inversion) | `MemberService`/`MemberServiceImpl`, `ClassBookingService`/`ClassBookingServiceImpl` |
| Bean lifecycle hook (`CommandLineRunner`) | `DataSeeder` |
| `@Transactional` | `ClassBookingServiceImpl.bookClass()` |

## Where Spring Data JPA concepts live
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column` — every class in `entity/`
- `@ManyToOne` + `@JoinColumn` (foreign keys) — `Booking.java`
- `@Enumerated(EnumType.STRING)` — `Booking.status`
- Derived query methods (`findByUsername`, `existsByEmail`, `countByFitnessClass_IdAndStatus`) — every repository
- `@Query` with `JOIN FETCH` (avoiding N+1 queries) — `BookingRepository.findMemberBookingsWithDetails`

## Your #1 priority — the three JUnit test levels

| Test class | Type | What's loaded |
|---|---|---|
| `service/MemberServiceTest`, `service/ClassBookingServiceTest` | **Unit test** | Nothing — Mockito mocks only |
| `controller/AuthControllerSliceTest` | **Slice test** | Only the web layer (`@WebMvcTest` + `@MockBean`) |
| `repository/MemberRepositoryTest` | **Integration test** | Real JPA + embedded H2 (`@DataJpaTest`) |
| `integration/BookingFlowIntegrationTest`, `FitClubApplicationTests` | **Integration test** | Full app, nothing mocked (`@SpringBootTest`) |

**Note on `@MockBean`:** this project pins Spring Boot 3.3.4, where
`@MockBean` is still standard. If you later upgrade this project to
Spring Boot 4.x, switch to `@MockitoBean` from
`org.springframework.test.context.bean.override.mockito.MockitoBean` —
same idea, new package, exactly like we covered for your other project.

**Practice tip:** try adding a test for `BookingController` yourself
(`@WebMvcTest(BookingController.class)`) — nothing currently covers the
`/dashboard/book/{classId}` and `/my-bookings` endpoints at the slice
level, only through the full integration test. Good next exercise.

## Topics not covered yet — your likely next Spring MVC/Spring lessons
- Spring Security (real password hashing, login, CSRF protection — this
  project's plain-text password + HttpSession login is a simplified stand-in)
- `@RestController` / `@ResponseBody` / `ResponseEntity` — a JSON API
  sibling of this HTML-view app, same DispatcherServlet flow underneath
- Flyway/Liquibase migrations instead of `ddl-auto=update`
- Interceptors (`HandlerInterceptor`) for cross-cutting request logic
  (e.g. checking `loggedInMemberId` in one place instead of in every controller)
- Actuator + health checks for real container orchestration readiness

## Project layout
```
src/main/java/com/learning/fitclub/
  FitClubApplication.java
  entity/         Member, FitnessClass, Booking, BookingStatus
  repository/     MemberRepository, FitnessClassRepository, BookingRepository
  dto/            MemberRegistrationForm, LoginForm
  service/        MemberService(+Impl), ClassBookingService(+Impl)
  controller/     AuthController, DashboardController, BookingController
  exception/      5 custom exceptions + GlobalExceptionHandler
  config/         DataSeeder (CommandLineRunner)
src/main/resources/
  templates/*.html, static/css/style.css, application.properties (MySQL)
src/test/resources/application.properties (H2 - overrides main for tests)
src/test/java/com/learning/fitclub/
  service/, controller/, repository/, integration/
Dockerfile, docker-compose.yml, .dockerignore
```
