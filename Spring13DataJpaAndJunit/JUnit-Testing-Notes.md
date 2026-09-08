# JUnit Testing Notes — from your 4 project test files

Your project has one example of each of the three testing levels, plus a
fourth file that's actually two different slice tests. Use this as a
reference while you write your own tests in `OrderServiceTest` /
`OrderRepositoryTest`.

---

## The big picture: which file is which test type

| File | Test type | Annotation | What's real, what's fake |
|---|---|---|---|
| `OrderServiceTest.java` | **Unit test** | `@ExtendWith(MockitoExtension.class)` | `OrderService` is real. `CustomerRepository` + `OrderRepository` are Mockito mocks. No Spring, no database. |
| `OrderControllerTest.java` | **Slice test** | `@WebMvcTest(OrderController.class)` | `OrderController` + web layer are real. `OrderService` + `OrderRepository` are `@MockBean` fakes. |
| `CustomerControllerTest.java` | **Slice test** | `@WebMvcTest(CustomerController.class)` | `CustomerController` + web layer are real. `CustomerRepository` is a `@MockBean` fake. |
| `OrderRepositoryTest.java` | **Integration test** | `@DataJpaTest` | Everything is real: real Hibernate, real embedded H2 database, real SQL. Nothing is mocked. |

This is the exact same three-level pyramid from your Book Issue Desk
project (`BookServiceTest` / `BookControllerSliceTest` /
`BookIssueDeskIntegrationTest`) — same ideas, applied to `Order`/`Customer`
instead of `Book`.

---

## 1. `OrderServiceTest.java` — Unit test

**What makes it a unit test:** `orderService` is a real `OrderService`
object created via `@InjectMocks`. Its two dependencies
(`CustomerRepository`, `OrderRepository`) are `@Mock` fakes. No Spring
context loads — this is why it's the fastest of the three.

**Key patterns used:**

- `@ExtendWith(MockitoExtension.class)` — plugs Mockito into JUnit 5 so
  `@Mock` and `@InjectMocks` fields get created automatically before each
  test.
- `@BeforeEach void setUp()` — builds a fresh `testCustomer` before every
  test method, so tests don't share state or leak data into each other.
- `when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer))`
  — stubbing: tells the mock what to return for a given input, so the real
  method under test has something deterministic to work with.
- `assertThrows(RuntimeException.class, () -> orderService.placeOrder(99L, newOrder))`
  — confirms the real business logic throws when a customer doesn't exist.
- `verify(customerRepository, times(1)).save(testCustomer)` — confirms a
  mock method was actually **called**, not just that the result looks
  right. This is different from an assertion on a return value — it
  checks *interaction*, not *output*.
- `verify(customerRepository, never()).save(any())` — the negative case:
  proves `save()` was never called when the customer lookup failed.
  Important because "the exception was thrown" doesn't by itself prove
  the code didn't *also* accidentally save something first.
- Stream API test (`buildSummaryFor_computesTotalsAndGroupsByStatus`) —
  this test doesn't mock any Stream logic; it lets the real
  `Collectors.groupingBy` / `.sum()` code run against a fixed list of
  `Order` objects, then asserts on the real computed totals.
- Multithreading test (`generateReport_returnsOneSummaryPerCustomer`) —
  tests a method that internally spins up an `ExecutorService` and
  collects results via `future.get()`. Note the assertion style:
  `containsExactlyInAnyOrder(100.0, 200.0)` instead of checking a specific
  order — because thread scheduling means you can't guarantee which
  customer's summary comes back first.

**⚠️ Import problem to fix:** the top of this file has several imports
that don't belong in a normal JUnit/Mockito test and will likely cause
compile errors or IDE confusion:
```java
import static javax.management.Query.times;
import static jdk.internal.classfile.impl.verifier.VerifierImpl.verify;
import static jdk.jfr.internal.jfc.model.Constraint.any;
```
These are unrelated JDK internals that happen to share method names
(`times`, `verify`, `any`) with Mockito's own static methods. They're
almost certainly IDE auto-import mistakes — you want these instead,
which the file also already has further down:
```java
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
```
Since `Mockito.*` already covers `times()`, `verify()`, and `never()`,
the three internal-JDK imports above should simply be deleted.

---

## 2 & 3. `OrderControllerTest.java` / `CustomerControllerTest.java` — Slice tests

**What makes these slice tests:** `@WebMvcTest(XController.class)` loads
only the web MVC layer — `DispatcherServlet`, `HandlerMapping`,
`ViewResolver`/message converters — and only registers that one
controller. No real service or repository beans exist; `@MockBean`
replaces them with fakes registered inside the (partial) Spring context.

**Key patterns used (same in both files):**

- `@Autowired private MockMvc mockMvc;` — lets you fire a fake HTTP
  request at the DispatcherServlet without a real running server.
- `@MockBean` — puts a Mockito fake **inside the Spring context** itself,
  so when the real controller asks Spring for a `CustomerRepository` or
  `OrderService`, it gets the fake. (Note: on newer Spring Boot 4.x this
  annotation is renamed to `@MockitoBean` from
  `org.springframework.test.context.bean.override.mockito.MockitoBean` —
  worth checking which your project's Spring Boot version needs.)
- `mockMvc.perform(get(...)/post(...)/delete(...))` — builds and sends
  the fake request.
- `.andExpect(status().isOk())` — checks the HTTP status code.
- `.andExpect(jsonPath("$.name").value("Ravi Kumar"))` — digs into the
  JSON response body without manually parsing it. `$` is the JSON root,
  `$[0].name` reaches into the first element of a JSON array.
- `objectMapper.writeValueAsString(requestBody)` — converts a Java object
  into a JSON string for the request body, mirroring what a real HTTP
  client would send.
- `verify(customerRepository, times(1)).save(any(Customer.class))` —
  same verification idea as the unit test, but here it's confirming the
  **controller correctly delegated** to the (fake) repository/service,
  not that any real business logic ran.
- `anyLong()`, `any(Order.class)` — loose argument matchers, used when the
  test only cares *that* a method was called with *some* value of that
  type, not the exact value.

**Why two nearly-identical-looking test files exist:** `OrderControllerTest`
tests four different querying approaches side by side — method-name query
(`findByStatus`), `@Query` JPQL, `@NamedQuery`, and the concurrent report
endpoint — while `CustomerControllerTest` covers standard CRUD (get all,
get by id, create, search, delete). Comparing them side by side is a good
way to see the *pattern repeat* rather than being four unrelated things to
memorize.

---

## 4. `OrderRepositoryTest.java` — Integration test

**What makes it an integration test:** `@DataJpaTest` boots the real
JPA/Hibernate layer against a real (embedded, in-memory) H2 database.
Nothing is mocked here — deliberately. Since the whole point is to prove
your `@Query`/`@NamedQuery`/method-name queries produce correct SQL
against a real schema, mocking the repository would defeat the purpose.

**Key patterns used:**

- `@DataJpaTest` — a slice test for the persistence layer specifically
  (loads only `@Entity` classes, `Repository` interfaces, and an embedded
  database — not your services or controllers).
- `@Transactional` (inherited automatically from `@DataJpaTest`) — every
  test runs inside its own transaction that gets **rolled back**
  afterward. This is why the same customer/order data can be recreated
  fresh in every test without leftover rows causing conflicts.
- Real repository calls, no `when()`/`verify()` anywhere in this file —
  because there's nothing to stub. You call `orderRepository.findByStatus(...)`
  and get back what a real query against a real (temporary) database
  produced.
- Four query approaches tested back to back: method-name query
  (`findByStatus`), `@Query` JPQL (`fetchOrdersByAmountAndStatus`),
  `@NamedQuery` (`fetchByAmountAndStatus`), and a relationship-integrity
  check (`addOrder_persistsForeignKeyCorrectly`) confirming the foreign
  key actually got set, not left `null`.

**Two things worth double-checking against what we covered earlier in
this conversation, since they apply directly here:**

1. **Generated IDs.** If `Order`/`Customer` use
   `GenerationType.IDENTITY`/`SEQUENCE`, every `new Order(...)` /
   `new Customer(...)` in this file should pass `null` for the id, and
   any assertion that needs a real id should read it back from the saved
   object (`customer.getId()`), not hardcode a literal like `1L`.
2. **Reserved SQL keywords.** If your `Order` entity's table name ever
   gets set to something like `order` or `value` to dodge a keyword
   collision, that's the wrong fix — use a plural, non-reserved name like
   `orders` instead (see the H2 `create table value` syntax error we
   debugged earlier in this conversation for exactly what goes wrong
   otherwise).

---

## Quick reference: the annotations that define each test level

| Annotation | Loads | Speed | Use for |
|---|---|---|---|
| `@ExtendWith(MockitoExtension.class)` | Nothing (plain objects + mocks) | Fastest | Testing one class's business logic in isolation |
| `@WebMvcTest(SomeController.class)` | Web layer only | Fast | Testing routing, JSON shape, status codes |
| `@DataJpaTest` | JPA/Hibernate + embedded DB | Medium | Testing real queries against a real schema |
| `@SpringBootTest` | Everything | Slowest | Testing the whole app wired together for real |

**Rule of thumb for how many of each to write:** lots of unit tests (cheap,
fast, pinpoint failures), a moderate number of slice tests (one per
controller/repository), and a small number of integration tests (just
enough to prove the pieces actually connect correctly).
