Yes. We'll treat this as **one React learning project**, not just a frontend implementation exercise.

I checked your uploaded OpenAPI document and will use its documented endpoints as the source of truth. It contains **10 operations across authentication, classes, bookings, favorites, and dashboard**.

# FitClub React Frontend — Roadmap

## Phase 0 — React foundation

**Goal:** Understand the React basics before connecting the backend.

| Step | React concept         | Component / work                      |
| ---- | --------------------- | ------------------------------------- |
| 0.1  | JSX                   | `App.jsx`                             |
| 0.2  | Components            | `Navbar`, `ClassCard`                 |
| 0.3  | Props                 | Pass class information to `ClassCard` |
| 0.4  | `.map()`              | Render multiple classes               |
| 0.5  | `useState`            | Store UI/API data                     |
| 0.6  | Events                | Buttons and form submission           |
| 0.7  | Conditional rendering | Loading, errors, logged-in state      |

---

# Phase 1 — Application structure

We'll create the frontend structure gradually:

```text
src/
│
├── components/
│   ├── Navbar.jsx
│   ├── ClassCard.jsx
│   ├── TrainerList.jsx
│   ├── Schedule.jsx
│   ├── Loading.jsx
│   └── ErrorMessage.jsx
│
├── pages/
│   ├── Home.jsx
│   ├── Login.jsx
│   ├── Register.jsx
│   ├── Classes.jsx
│   ├── MyBookings.jsx
│   └── Dashboard.jsx
│
├── services/
│   └── api.js
│
├── App.jsx
├── App.css
├── index.css
└── main.jsx
```

We **will not create all of these at once**. We'll create each when we learn why it is needed.

---

# Phase 2 — First real backend feature

Your first real feature will be:

```text
GET /api/classes
```

Your Swagger documentation says this returns a JSON array containing:

```text
id
className
description
capacity
confirmedBookingCount
favorited
schedule
trainers
```

with schedule and trainer information nested inside the response.

We'll learn:

```text
fetch()
  ↓
HTTP GET
  ↓
Spring Boot
  ↓
JSON
  ↓
useState()
  ↓
.map()
  ↓
ClassCard
  ↓
Browser
```

This will be our **first complete React → Spring Boot feature**.

---

# Phase 3 — Authentication

Your API uses **session-based authentication**, not JWT.

The documented flow is:

```text
Register
   ↓
POST /api/auth/register
   ↓
Login
   ↓
POST /api/auth/login
   ↓
JSESSIONID session cookie
   ↓
Protected endpoints
```

Your Swagger explicitly documents the login endpoint as creating a session and setting `JSESSIONID`.

### Components

```text
pages/
├── Login.jsx
└── Register.jsx
```

### Endpoints

| Endpoint                  | Frontend feature       |
| ------------------------- | ---------------------- |
| `POST /api/auth/register` | Registration form      |
| `POST /api/auth/login`    | Login form             |
| `GET /api/auth/me`        | Determine current user |
| `POST /api/auth/logout`   | Logout                 |

The `/api/auth/me` endpoint returns the current user's ID and requires the active session.

Logout invalidates that session.

---

# Phase 4 — Classes page

We'll turn `/api/classes` into an actual FitClub page.

```text
Classes.jsx
│
├── ClassCard
│   ├── className
│   ├── description
│   ├── capacity
│   ├── booking count
│   ├── Schedule
│   └── Trainers
│
├── Book button
└── Favorite button
```

This phase teaches:

* props
* arrays
* `.map()`
* nested objects
* nested arrays
* conditional rendering
* event handlers

---

# Phase 5 — Booking

Endpoint:

```text
POST /api/classes/{classId}/book
```

Your Swagger documents this as a session-protected operation with no request body.

We'll add:

```text
ClassCard
    │
    └── [Book Class]
             ↓
       POST /api/classes/{id}/book
```

We'll also learn how React handles:

```text
button click
     ↓
API request
     ↓
success / error
     ↓
update UI
```

---

# Phase 6 — Favorites

Two endpoints:

```text
POST /api/classes/{classId}/favorite
DELETE /api/classes/{classId}/favorite
```

These are both documented in your API.

The UI will eventually have something like:

```text
Morning Yoga       ♡
```

and after favoriting:

```text
Morning Yoga       ♥
```

We'll use the `favorited` property already returned by `/api/classes` to reflect the current user's state.

---

# Phase 7 — My Bookings

Endpoint:

```text
GET /api/bookings/me
```

Your API returns:

```text
id
className
bookingDate
status
```

for the authenticated user's bookings.

We'll create:

```text
pages/
└── MyBookings.jsx
```

UI:

```text
My Bookings
────────────────────────────
Morning Yoga
Booked: ...
Status: CONFIRMED

HIIT Blast
Booked: ...
Status: CONFIRMED
```

This phase reinforces:

* `useState`
* `useEffect`
* `fetch`
* `.map()`
* conditional rendering

---

# Phase 8 — Dashboard

Your API also documents:

```text
GET /dashboards
```

with a successful response documented as:

```text
dashboard
```

and no request body.

We'll therefore create:

```text
pages/
└── Dashboard.jsx
```

and connect that endpoint.

**Important:** I won't invent dashboard fields that aren't documented in your Swagger.

---

# Phase 9 — React application navigation

Once the individual features work, we'll introduce routing.

```text
React Router
     │
     ├── /
     │    Home
     │
     ├── /login
     │    Login
     │
     ├── /register
     │    Register
     │
     ├── /classes
     │    Classes
     │
     ├── /bookings
     │    My Bookings
     │
     └── /dashboard
          Dashboard
```

This is when the frontend starts behaving like an actual application instead of a collection of React exercises.

---

# Phase 10 — API service layer

Instead of eventually having this everywhere:

```javascript
fetch("http://localhost:8080/api/classes")
```

we'll introduce:

```text
services/
└── api.js
```

and organize API communication.

Conceptually:

```text
React Components
       │
       ▼
    api.js
       │
       ▼
Spring Boot REST API
```

This will also give you a practical reason to learn JavaScript concepts such as:

* functions
* objects
* promises
* `async/await`
* error handling
* modules
* `fetch`

---

# Complete endpoint checklist

This is important because you specifically said **no endpoint should be missed**.

|  # | Backend endpoint                         | React feature        | Planned |
| -: | ---------------------------------------- | -------------------- | :-----: |
|  1 | `POST /api/auth/register`                | Register             |    ✅    |
|  2 | `POST /api/auth/login`                   | Login                |    ✅    |
|  3 | `GET /api/auth/me`                       | Current user/session |    ✅    |
|  4 | `POST /api/auth/logout`                  | Logout               |    ✅    |
|  5 | `GET /api/classes`                       | Classes              |    ✅    |
|  6 | `POST /api/classes/{classId}/book`       | Book class           |    ✅    |
|  7 | `GET /api/bookings/me`                   | My bookings          |    ✅    |
|  8 | `POST /api/classes/{classId}/favorite`   | Favorite             |    ✅    |
|  9 | `DELETE /api/classes/{classId}/favorite` | Unfavorite           |    ✅    |
| 10 | `GET /dashboards`                        | Dashboard            |    ✅    |

So **all 10 documented operations are accounted for**.

---

# How we'll actually learn

I don't want to give you the finished frontend code.

For every stage, we'll follow:

```text
1. Concept
      ↓
2. Small example
      ↓
3. You understand what it does
      ↓
4. Apply it to FitClub
      ↓
5. Test in browser
      ↓
6. Connect it to backend
      ↓
7. Move to next concept
```

And because you're learning JavaScript at the same time, I'll explicitly point out when something is **JavaScript** versus **React**.

For example:

```jsx
classes.map(classItem => ...)
```

`map()` → **JavaScript**

while:

```jsx
useState(...)
```

→ **React**

and:

```jsx
<ClassCard />
```

→ **React component/JSX usage**

That distinction will be important for you.

---

# 🚀 Let's start — Step 1

You already created the Vite project.

Now let's make your **first real component**, but don't connect the API yet.

Create:

```text
src/components/ClassCard.jsx
```

Put only this in it:

```jsx
function ClassCard() {
    return (
        <div>
            <h2>Morning Yoga</h2>
            <p>Gentle flow to start your day</p>
        </div>
    );
}

export default ClassCard;
```

Then modify `App.jsx`:

```jsx
import ClassCard from "./components/ClassCard";

function App() {
    return (
        <div>
            <h1>FitClub</h1>

            <ClassCard />
        </div>
    );
}

export default App;
```

Run:

```powershell
npm run dev
```

You should see:

```text
FitClub

Morning Yoga
Gentle flow to start your day
```

**Stop at this point.**

Once this works, our next lesson is **Props**. We'll take the `"Morning Yoga"` hardcoded inside `ClassCard` and learn how to pass it from `App` — which is the first step toward eventually passing your **real `/api/classes` data** into the component.
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
TO-DO LIST...

Absolutely. Use this as the **master TODO checklist** for the FitClub React frontend. We'll mark items as completed as we progress.

# FitClub React Frontend — TODO

## 🟢 Phase 0 — Project Setup

* [x] Create React + Vite project
* [x] Configure React + JavaScript
* [x] Run `npm install`
* [x] Run `npm run dev`
* [x] Verify React app opens at `localhost:5173`
* [ ] Understand basic Vite project structure
* [ ] Clean default Vite UI

---

## 🟢 Phase 1 — React Fundamentals

### Components + JSX

* [ ] Create first `App` component
* [ ] Understand JSX
* [ ] Create `ClassCard` component
* [ ] Render `ClassCard` from `App`
* [ ] Understand component reuse

### Props

* [ ] Understand why props are needed
* [ ] Pass `className` to `ClassCard`
* [ ] Pass `description` to `ClassCard`
* [ ] Pass multiple props
* [ ] Display props inside JSX

### JavaScript + React

* [ ] Use JavaScript objects as component data
* [ ] Use arrays in React
* [ ] Use `.map()` to render multiple components
* [ ] Understand `key` when rendering lists

---

# 🟡 Phase 2 — First Real FitClub UI

* [ ] Create `ClassCard`
* [ ] Create class list
* [ ] Display class name
* [ ] Display description
* [ ] Display capacity
* [ ] Display confirmed booking count
* [ ] Display schedule
* [ ] Display trainers
* [ ] Display favorite status

Target:

```text
Classes
────────────────────────────

Morning Yoga
Gentle flow to start your day

Capacity: 15
Bookings: 1

MONDAY
07:00 - 08:00

Trainer: Asha Patel
Specialization: Yoga & Flexibility

♡ Favorite
[ Book Class ]
```

At this stage the data can still be **hardcoded**.

---

# 🟡 Phase 3 — React State

* [ ] Understand `useState`
* [ ] Create state
* [ ] Update state
* [ ] Understand re-rendering
* [ ] Store class data in state
* [ ] Change UI based on state
* [ ] Handle button click events

---

# 🟡 Phase 4 — First Backend Connection

### Learn JavaScript `fetch()`

* [ ] Understand HTTP request from React
* [ ] Understand `fetch()`
* [ ] Understand Promise returned by `fetch()`
* [ ] Learn `.then()`
* [ ] Learn `async/await`
* [ ] Handle `response.json()`

### First real endpoint

* [ ] Call `GET /api/classes`
* [ ] Receive JSON from Spring Boot
* [ ] Store response using `useState`
* [ ] Render returned classes using `.map()`
* [ ] Remove hardcoded class data

**🎯 Milestone #1**

> React successfully displays real fitness classes from your Spring Boot API.

Your API documents `/api/classes` as a session-protected endpoint returning the class information we will display.

---

# 🟡 Phase 5 — API States

* [ ] Understand loading state
* [ ] Create `Loading` component
* [ ] Display loading message
* [ ] Understand API errors
* [ ] Create `ErrorMessage` component
* [ ] Display API error
* [ ] Handle empty class list

---

# 🟠 Phase 6 — Authentication

## Register

* [ ] Create `Register.jsx`
* [ ] Create username input
* [ ] Create email input
* [ ] Create password input
* [ ] Handle form submission
* [ ] Send `POST /api/auth/register`
* [ ] Handle successful registration
* [ ] Handle `409 Conflict`
* [ ] Handle validation errors

Your documented registration endpoint accepts username, email and password.

## Login

* [ ] Create `Login.jsx`
* [ ] Create username input
* [ ] Create password input
* [ ] Submit login
* [ ] Send `POST /api/auth/login`
* [ ] Understand session-based authentication
* [ ] Understand `JSESSIONID`
* [ ] Configure frontend requests to work with the session cookie

Your API documents `POST /api/auth/login` as creating the session and setting the `JSESSIONID` cookie.

## Current User

* [ ] Call `GET /api/auth/me`
* [ ] Store current user ID
* [ ] Understand authenticated vs unauthenticated UI

## Logout

* [ ] Create logout button
* [ ] Call `POST /api/auth/logout`
* [ ] Clear frontend authentication state
* [ ] Return user to login/home

---

# 🟠 Phase 7 — Protected Classes Features

## Book Class

* [ ] Add Book button to `ClassCard`
* [ ] Get `classId`
* [ ] Handle button click
* [ ] Send `POST /api/classes/{classId}/book`
* [ ] Handle successful booking
* [ ] Handle `401`
* [ ] Handle `409`
* [ ] Update UI after booking

Your endpoint requires an active session and has documented success/conflict/unauthorized/not-found cases.

---

# 🟠 Phase 8 — Favorites

## Favorite

* [ ] Add favorite button
* [ ] Send `POST /api/classes/{classId}/favorite`
* [ ] Update `favorited` state
* [ ] Display favorite state

## Unfavorite

* [ ] Send `DELETE /api/classes/{classId}/favorite`
* [ ] Update `favorited` state
* [ ] Handle `401`
* [ ] Handle `404`

These two operations are documented as the inverse favorite/unfavorite operations.

---

# 🟠 Phase 9 — My Bookings

* [ ] Create `MyBookings.jsx`
* [ ] Understand `useEffect`
* [ ] Call `GET /api/bookings/me`
* [ ] Store bookings in state
* [ ] Display booking list
* [ ] Display class name
* [ ] Display booking date
* [ ] Display booking status
* [ ] Handle loading
* [ ] Handle errors
* [ ] Handle empty bookings

Your API documents this endpoint as returning the authenticated user's bookings.

**🎯 Milestone #2**

> Logged-in user can see their FitClub bookings.

---

# 🟠 Phase 10 — Dashboard

* [ ] Create `Dashboard.jsx`
* [ ] Call `GET /dashboards`
* [ ] Display the documented dashboard response
* [ ] Handle loading
* [ ] Handle errors

We will **not invent dashboard fields**, because your Swagger currently documents the successful response as `dashboard` rather than specifying a structured dashboard model.

---

# 🔵 Phase 11 — React Routing

Learn React Router:

* [ ] Understand client-side routing
* [ ] Install React Router
* [ ] Create routes
* [ ] `/`
* [ ] `/login`
* [ ] `/register`
* [ ] `/classes`
* [ ] `/bookings`
* [ ] `/dashboard`
* [ ] Add navigation
* [ ] Add logout navigation

---

# 🔵 Phase 12 — Reusable Components

Create/reuse:

* [ ] `Navbar`
* [ ] `ClassCard`
* [ ] `TrainerList`
* [ ] `Schedule`
* [ ] `Loading`
* [ ] `ErrorMessage`
* [ ] `Button`
* [ ] Form components where useful

Goal:

```text
Pages
  ↓
Reusable Components
  ↓
UI
```

---

# 🔵 Phase 13 — API Service Layer

Create:

```text
src/
└── services/
    └── api.js
```

* [ ] Understand why API calls should be separated
* [ ] Create API base URL
* [ ] Create registration function
* [ ] Create login function
* [ ] Create current-user function
* [ ] Create logout function
* [ ] Create get-classes function
* [ ] Create book-class function
* [ ] Create get-bookings function
* [ ] Create favorite-class function
* [ ] Create unfavorite-class function
* [ ] Create dashboard function

---

# 🔵 Phase 14 — Final API Coverage Check

Before calling the frontend complete:

| Endpoint                                 | Status |
| ---------------------------------------- | :----: |
| `POST /api/auth/register`                |    ⬜   |
| `POST /api/auth/login`                   |    ⬜   |
| `GET /api/auth/me`                       |    ⬜   |
| `POST /api/auth/logout`                  |    ⬜   |
| `GET /api/classes`                       |    ⬜   |
| `POST /api/classes/{classId}/book`       |    ⬜   |
| `GET /api/bookings/me`                   |    ⬜   |
| `POST /api/classes/{classId}/favorite`   |    ⬜   |
| `DELETE /api/classes/{classId}/favorite` |    ⬜   |
| `GET /dashboards`                        |    ⬜   |

**10 / 10 endpoints must be checked before we consider the frontend complete.**

---

# 🏁 Final Project

When everything is complete, the architecture should look roughly like:

```text
Spring-14-fit-club-frontend
│
└── src
    │
    ├── components
    │   ├── Navbar
    │   ├── ClassCard
    │   ├── Schedule
    │   ├── TrainerList
    │   ├── Loading
    │   └── ErrorMessage
    │
    ├── pages
    │   ├── Home
    │   ├── Login
    │   ├── Register
    │   ├── Classes
    │   ├── MyBookings
    │   └── Dashboard
    │
    ├── services
    │   └── api.js
    │
    ├── App.jsx
    ├── App.css
    ├── index.css
    └── main.jsx
```

### Current progress

**Phase 0:** Project created ✅
**Next:** **Phase 1 — React Fundamentals → Components + JSX → Props**

So our immediate TODO is simply:

```text
[ ] Create ClassCard.jsx
[ ] Render ClassCard from App.jsx
[ ] Verify it appears in browser
[ ] Learn Props
```

We'll mark each item as we complete it rather than jumping ahead.


