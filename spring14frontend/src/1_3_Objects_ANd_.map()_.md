Exactly. 👍 From now on, we'll follow this approach:

> **Build the React components while learning, using the actual response structure from your API documentation as our source of truth. Temporary data is allowed only when needed for learning, and it will always be marked clearly.**

Since you've added the extra props, let's move to the next React concept.

## Step 4 — JavaScript Objects and `.map()`

Before we connect the real API, we need to understand **how React will eventually receive the `/api/classes` response**.

Your API returns an **array of class objects**:

```text
GET /api/classes
       ↓
[
   class object,
   class object,
   class object,
   ...
]
```

Each class object contains the fields documented by your API:

```text
Class
├── id
├── className
├── description
├── capacity
├── confirmedBookingCount
├── favorited
├── schedule
└── trainers
```

The important React/JavaScript problem is:

> **How do we take an array containing multiple classes and create one `<ClassCard />` for each class?**

That's where `.map()` comes in.

### First, let's practice with the API-shaped data

In `App.jsx`, temporarily create an array:

```jsx
// TEMPORARY PRACTICE DATA — shaped from GET /api/classes
const classes = [
    {
        id: 1,
        className: "Morning Yoga",
        description: "Gentle flow to start your day",
        capacity: 20,
        confirmedBookingCount: 12
    },
    {
        id: 2,
        className: "Strength Training",
        description: "Build strength and improve fitness",
        capacity: 15,
        confirmedBookingCount: 8
    }
];
```

Notice that I'm **not claiming these are your backend's actual returned values**. The important part here is learning the array/object structure using the fields from your documented endpoint.

Then:

```jsx
classes.map((fitnessClass) => {
    return <ClassCard
        className={fitnessClass.className}
        description={fitnessClass.description}
        capacity={fitnessClass.capacity}
        confirmedBookingCount={fitnessClass.confirmedBookingCount}
    />;
})
```

The mental flow is:

```text
classes
  │
  ├── class object #1 ──→ ClassCard
  │
  ├── class object #2 ──→ ClassCard
  │
  └── class object #3 ──→ ClassCard
```

And **later**, the temporary `classes` array will disappear:

```text
TEMPORARY ARRAY
      ↓
removed
      ↓
GET /api/classes
      ↓
real response
      ↓
classes state
      ↓
.map()
      ↓
ClassCard
```

### One important thing

Don't implement this yet unless you want to follow the step.

First, understand this one idea:

> **`.map()` takes each object from the API response array and lets us create a React component from that object.**

Once that makes sense, we'll implement it in `App.jsx`, and then we'll introduce the `key` prop—which is an important React rule when rendering lists.






--------------------------------------------------------------------------------------
/*
Great. ✅ Let's continue from Step 4 — .map().
One important React concept comes immediately with .map():
When React creates multiple components from an array, each component needs a unique key.
Our flow is:
```
classes array
     │
     │ .map()
     ▼
 ┌───────────────┐
 │ class object 1│ ──→ <ClassCard />
 ├───────────────┤
 │ class object 2│ ──→ <ClassCard />
 ├───────────────┤
 │ class object 3│ ──→ <ClassCard />
 └───────────────┘
```
React needs to distinguish those generated components.
So we write:
```
<ClassCard key={fitnessClass.id} />
```
Your actual /api/classes response contains an id for each class.
So eventually:
```
key={fitnessClass.id}
```
will use the real API class ID.
Update your App.jsx like this:
```
import ClassCard from "./components/ClassCard";

// TEMPORARY PRACTICE DATA — shaped from GET /api/classes
const classes = [
    {
        id: 1,
        className: "Morning Yoga",
        description: "Gentle flow to start your day",
        capacity: 20,
        confirmedBookingCount: 12
    },
    {
        id: 2,
        className: "Strength Training",
        description: "Build strength and improve fitness",
        capacity: 15,
        confirmedBookingCount: 8
    }
];

function App() {
    return (
        <div>
            <h1>FitClub</h1>

            {classes.map((fitnessClass) => {
                return (
                    <ClassCard
                        key={fitnessClass.id}
                        className={fitnessClass.className}
                        description={fitnessClass.description}
                        capacity={fitnessClass.capacity}
                        confirmedBookingCount={fitnessClass.confirmedBookingCount}
                    />
                );
            })}
        </div>
    );
}

export default App;
```
Previously we manually wrote:
```
<ClassCard
    className="Morning Yoga"
    ...
/>

<ClassCard
    className="Strength Training"
    ...
/>
```
Now we have:
```
classes.map((fitnessClass) => {
    return <ClassCard ... />;
})
```
So the array determines how many ClassCards are created.
```
2 objects in array
       ↓
     .map()
       ↓
2 ClassCards
```
And later, with the real API:
```
GET /api/classes
       ↓
real JSON array
       ↓
classes state
       ↓
.map()
       ↓
one ClassCard per API class
```
.map() = "For every object in this array, create something."
key = "Give React a unique identity for each item in this list."
Don't use:
```
key={Math.random()}
```
and don't use the array index as our first choice when the API already gives us a unique id.
We'll eventually use:
```
key={fitnessClass.id}
```
from the real /api/classes response.
Once you see the two cards rendering through .map(), tell me completed. Then we'll move to Step 6 — useState, which is where our class array starts becoming actual React state.


-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------


Excellent. ✅ **Step 5 — `.map()` + `key` is completed.**

Now we move to an important React concept:

# Step 6 — `useState`

So far, our classes are just a normal JavaScript array:

```jsx
const classes = [...]
```

But eventually this array will come from:

```text
GET /api/classes
       ↓
HTTP response
       ↓
React
       ↓
classes
```

React needs a way to **store that data and update the UI when the data changes**.

That's what `useState` is for.

---

## 6.1 What is state?

A simple mental model:

> **State is data that React remembers and watches for changes.**

For example:

```jsx
const [classes, setClasses] = useState([]);
```

This gives us **two things**:

```text
classes
   ↑
current value


setClasses(...)
   ↑
function used to update the value
```

And the initial value is:

```jsx
[]
```

because initially we don't have the API response yet.

---

# 6.2 Import `useState`

At the top of `App.jsx`:

```jsx
import { useState } from "react";
import ClassCard from "./components/ClassCard";
```

Then:

```jsx
function App() {

    const [classes, setClasses] = useState([]);

    ...
}
```

The important syntax is:

```jsx
const [classes, setClasses] = useState([]);
```

Think:

```text
useState([])
      │
      ├── classes
      │     → current state value
      │
      └── setClasses
            → changes the state
```

---

# 6.3 Move our temporary data into state

For now, we're **still not calling the API**.

So we'll initialize the state with our temporary practice data.

```jsx
import { useState } from "react";
import ClassCard from "./components/ClassCard";

function App() {

    // TEMPORARY PRACTICE DATA — not from API
    const [classes, setClasses] = useState([
        {
            id: 1,
            className: "Morning Yoga",
            description: "Gentle flow to start your day",
            capacity: 20,
            confirmedBookingCount: 12
        },
        {
            id: 2,
            className: "Strength Training",
            description: "Build strength and improve fitness",
            capacity: 15,
            confirmedBookingCount: 8
        }
    ]);

    return (
        <div>
            <h1>FitClub</h1>

            {classes.map((fitnessClass) => {
                return (
                    <ClassCard
                        key={fitnessClass.id}
                        className={fitnessClass.className}
                        description={fitnessClass.description}
                        capacity={fitnessClass.capacity}
                        confirmedBookingCount={
                            fitnessClass.confirmedBookingCount
                        }
                    />
                );
            })}
        </div>
    );
}

export default App;
```

### One thing you may notice

We aren't using `setClasses` yet.

That's **okay**.

We're first learning what state is. In the next step, we'll use `setClasses` when the API gives us the actual `/api/classes` response.

---

## The important difference

### Before

```text
Normal JavaScript variable
        ↓
const classes = [...]
```

### Now

```text
React State
        ↓
const [classes, setClasses] = useState([...])
```

Eventually:

```text
Spring Boot
    │
    │ GET /api/classes
    ▼
API response
    │
    ▼
setClasses(responseData)
    │
    ▼
classes
    │
    ▼
.map()
    │
    ▼
<ClassCard />
```

This is the foundation for our real API integration.

### 🧠 Mental model

> **`useState` gives React a piece of data that can change and cause the component to render again with the new data.**

For now, **don't worry about `useEffect` yet**. We'll introduce it when we actually make the API request.

Implement this version and make sure the two cards still appear. Then tell me **completed**.


-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
Good. ✅ **Step 6 — `useState` is now our foundation.**

Now we need to learn the React concept that lets us perform something **when the component appears**.

# Step 7 — `useEffect`

This is important because our eventual flow will be:

```text
React component loads
        ↓
useEffect()
        ↓
Call Spring Boot API
        ↓
GET /api/classes
        ↓
Receive JSON
        ↓
setClasses(response)
        ↓
React re-renders
        ↓
classes.map(...)
        ↓
ClassCard
```

### First: an important API detail

Your `GET /api/classes` endpoint is **session-protected** according to your API documentation. student-c3f-0a9-spring14-1.0.0-…

So we should **not pretend that we can currently call it anonymously and get class data**.

We'll learn `useEffect` first, and when we connect to this endpoint, we'll make sure the required session authentication is handled.

---

## 7.1 Import `useEffect`

Change:

```jsx
import { useState } from "react";
```

to:

```jsx
import { useState, useEffect } from "react";
```

Now your component can use both hooks.

---

## 7.2 Add a `useEffect`

Inside `App()`:

```jsx
useEffect(() => {
    console.log("App component loaded");
}, []);
```

So your structure becomes:

```jsx
function App() {

    const [classes, setClasses] = useState([
        // TEMPORARY PRACTICE DATA — not from API
        // ...
    ]);

    useEffect(() => {
        console.log("App component loaded");
    }, []);

    return (
        <div>
            <h1>FitClub</h1>

            {/* class rendering */}
        </div>
    );
}
```

Open the browser's **Developer Tools → Console**.

You should see:

```text
App component loaded
```

---

## What does `useEffect()` mean?

For now, understand it as:

> **`useEffect` allows us to run code as a side effect of a component rendering/lifecycle event.**

The second argument:

```jsx
[]
```

is important.

```jsx
useEffect(() => {
    console.log("App component loaded");
}, []);
```

For our current learning case, the empty dependency array means:

```text
Component renders
       ↓
useEffect runs
```

We'll learn dependencies properly later.

---

# Why do we need this for the API?

We don't want this:

```jsx
function App() {

    // somehow call API directly during rendering ❌

    return (...);
}
```

Instead, we'll eventually have:

```jsx
useEffect(() => {

    // call Spring Boot API here

}, []);
```

Then:

```text
App rendered
     ↓
useEffect
     ↓
fetch("/api/classes")
     ↓
Spring Boot
     ↓
JSON response
     ↓
setClasses(...)
     ↓
React renders again
```

### 🧠 Mental model

> **`useState` → stores changing data.**

> **`useEffect` → performs side-effect work such as API calls.**

For this step, **do not add `fetch()` yet**.

Just add the `useEffect`, open the console, and verify:

```text
App component loaded
```

Then tell me **completed**.


Generate an image for this concepts above .....   Use creativity and include the code and do not miss what is the flow and what is doing what and why ....


*/