# Spring Boot Data JPA

=> Can JDBC API send objects to a Database?
Answer: No.
* then how?

* You have to do the following:
    1. call getters to retrieve the data from an object.
    2. construct SQL and set the values to it.
    3. Now execute the SQL using JDBC API.


=> Can JDBC API retrieve objects from the database?
Answer: No.
* then how?

* You have to do the following:
    1. execute the sql query and retrieve the row into a ResultSet.
    2. Manually write the code for converting  the row into a Java object.

**** JDBC API cannot send/retrieve the Java objects directly from a Database. *********

=> So, a new technology called ORM has been provided to send/retrieve the
Java Objects from a Database.

=> ORM technology handles all the internal mismatches between
Object oriented model and Relational model.

- ex1: In Java, we use equals() method to compare the two objects.
  But in Database, the rows are compared with Primary key. So, ORM will handle this mismatch.

- ex2: In Java, we use HAS-A relation to provide the relationship between two objects.
  But in Database, the foreign key is used to provide the relationship between two tables. So, ORM will handle this mismatch.

- The vendors have created multiple tools/frameworks based on ORM technology,
  like Hibernate, MyBatis, TopLink, etc.

- The most popular ORM framework is Hibernate.

## Then what is JPA? (Java Persistence API)

- Different ORM frameworks have followed their own way to implement ORM.
  It means, each framework has developed their own API, and their own annotations, and configurations.

- As a developer, if i am switching from one framework to another, then again
  it will be like a new framework to learn.

- So, SUN Microsystems, have provided a solution that, it has provided
  a common standard specification for ORM. This common specification is called
  Java Persistence API(JPA).

- The ORM frameworks like Hibernate, EclipseLink, OpenJPA, etc.. they have provided
  the concrete implementaton for JPA specification. So, we call them as
  JPA vendors/JPA providers.

- JPA is a specification, it doesn't contain actual code to perform the
  database operations on its own. It is just a detailed blueprint.

- Hibernate is an implementation of JPA specification. It provides the actual code and
  functionality to follow the rules defined by JPA.

- The ORM tools that provides concrete implementation for the JPA specification are
  called JPA providers.

**********************************************************************************************
Basics :-> 
![DataJpa_Basic_concept_Transactions_and_Callbacks_(Spring 13).png](DataJpa_Basic_concept_Transactions_and_Callbacks_%28Spring%2013%29.png)
***********************************************************************************************

# 🔴 1. `findById()` vs `getReferenceById()`

---

## 🔥 Mental Model

```text
findById()           → "Bring Full Data Now"
getReferenceById()   → "Give me Placeholder, I’ll use later"
```

---

## ⚠️ Edge Cases (Real Projects)

### ❌ Case 1: LazyInitializationException

```text
getReferenceById() → returns proxy
Session closed → accessing field
→ 💥 LazyInitializationException
```

👉 Happens in:

* Service → returns entity
* Controller → tries to access field

---

### ❌ Case 2: Entity Not Found (Delayed Crash)

```text
getReferenceById(id)
(no DB hit yet)

Later → access field
→ 💥 EntityNotFoundException
```

👉 Problem:

* Error happens **late**
* Hard to debug

---

### ❌ Case 3: Logging / Debugging Trap

```text
System.out.println(entity)
```

👉 Triggers DB fetch unintentionally

---

## ✅ When to Use

| Use Case                                   | Method             |
| ------------------------------------------ | ------------------ |
| Need data immediately                      | findById()         |
| Only need reference (FK, delete, relation) | getReferenceById() |

---

# 🔴 2. `deleteAll()` vs `deleteAllInBatch()`

---

## 🔥 Mental Model

```text
deleteAll()         → "Load → Process → Delete One by One"
deleteAllInBatch()  → "Direct SQL → Boom Delete"
```

---

## ⚠️ Edge Cases

### ❌ Case 1: Missing Callbacks

```text
deleteAllInBatch()
→ @PreRemove NOT called
→ @PostRemove NOT called
```

👉 Risk:

* Audit logs missing
* Business rules skipped

---

### ❌ Case 2: Cascade Not Triggered

```text
deleteAllInBatch()
→ No cascade
→ Child records remain
→ 💥 Orphan data
```

---

### ❌ Case 3: Cache Inconsistency

```text
Persistence Context ≠ DB
```

👉 After batch delete:

* Cache still thinks data exists

---

### ❌ Case 4: Foreign Key Constraint Failure

```text
Direct SQL delete
→ Violates FK
→ 💥 Exception
```

---

## ✅ When to Use

| Use Case                 | Method             |
| ------------------------ | ------------------ |
| Need lifecycle events    | deleteAll()        |
| Bulk delete, performance | deleteAllInBatch() |

---

# 🔴 3. `save()` vs `saveAndFlush()`

---

## 🔥 Mental Model

```text
save()           → "Save Now, Push Later"
saveAndFlush()   → "Save + Push Immediately"
```

---

## ⚠️ Edge Cases

### ❌ Case 1: Data Not Visible Immediately

```text
save()
→ DB not updated yet
→ Next query may NOT see data
```

---

### ❌ Case 2: Constraint Violation Delay

```text
save()
→ Error comes at commit
→ Hard to trace
```

---

### ❌ Case 3: Performance Issue

```text
saveAndFlush()
→ Forces DB sync every time
→ 💥 Slower in loops
```

---

### ❌ Case 4: Unexpected Flush

```text
saveAndFlush()
→ Triggers flush of ALL pending changes
```

👉 Not just one entity!

---

## ✅ When to Use

| Use Case               | Method         |
| ---------------------- | -------------- |
| Normal operations      | save()         |
| Need immediate DB sync | saveAndFlush() |

---

# 🧠 FINAL COMBINED MENTAL MODEL

```text
findById()          → Safe, Immediate Data
getReferenceById()  → Lazy, Risky if misused

deleteAll()         → Safe, Slow, Full lifecycle
deleteAllInBatch()  → Fast, No lifecycle, Risky

save()              → Deferred execution
saveAndFlush()      → Immediate DB sync
```

---

# 🔥 One-Line Revision

```text
Most bugs in real projects come from:
→ Lazy loading misuse
→ Skipping lifecycle callbacks
→ Forcing flush at wrong time
```


# Spring Boot JPA properties

These properties control how Spring Boot + JPA + Hibernate talk to the database.

1. `spring.jpa.show-sql` ----> tells the Spring to print the SQL query on the console.
2. `spring.jpa.properties.hibernate.format_sql` -----> tells the Spring to print the SQL in pretty format, for better readability.
3. `spring.jpa.hibernate.ddl-auto` -----> controls the table creation and updation behaviour.
4. `spring.jpa.database-platform` ------> tells Hibernate about which Database dialect to use.

The possible values for `spring.jpa.hibernate.ddl-auto` property are,
1. none
2. validate
3. update
4. create
5. create-drop

- none -- do nothing. Used in Production.
- validate – checks entity and table match.
- update – create/alter tables. Used in development.
- create -- drop and create tables. Used in Testing
- create-drop --- creates tables at startup, drops at shutdown. Used in Junit.

## what MySQLDialect class will do?

==> org.hibernate.dialect.MySQLDialect tells Hibernate to translate
generic Java objects into SQL syntax and data types specific to
the MySQL database.






