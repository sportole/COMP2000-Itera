# COMP2000 Weekly Logbooks

## Week 02 — Git, Version Control & Team Formation

## Team Formation

- Finalised our team and decided on the name **Itera**.
- **Team members:** Ayaan, Daksh, Yuvan, Viveka, and Sushant.
- Set a shared goal of achieving a **High Distinction (HD)** for the overall assignment.
- Agreed that everyone should complete their assigned responsibilities and meet team deadlines, while allowing flexibility for valid circumstances.
- Agreed to communicate regularly and keep everyone updated on progress, particularly when working on shared tasks.

## Git & Version Control

This week we were introduced to **Git and version control** and learned about the different Git working areas:

- Working directory
- Staging area
- Local repository
- Remote repository

We practised moving changes between these areas and learned how commits are used to track changes to the project.

We also experimented with **multi-line commit messages and file attachments** and completed the **EPIC activity**, where Git commits were used as a form of communication between team members.

## Reflection

This week helped me understand how Git can be used to manage collaborative development rather than just storing code. Having commits and a shared repository gives the team a history of changes and makes it easier to keep track of everyone's work.

Our main takeaway was that **consistent communication and version control will be important as the project becomes larger**, especially when multiple team members are modifying the same codebase.

---

## Week 03 — Object Modelling & Project Design

- Our team decided to build a **zombie simulation** for our assignment. We discussed the overall idea of the simulation, including what entities would exist and how they would interact with each other.
- We identified some of the main classes we would likely need, including **Zombie, Human, Environment, and Simulation**. We discussed what responsibility each class should have rather than putting all the functionality into one class.
- We started identifying possible **fields and methods** for each class. For example, `Zombie` and `Human` would need properties and behaviours specific to their roles in the simulation, while `Environment` would be responsible for representing the simulation area.
- Used **draw.io** to create an initial **UML-style class diagram**. This helped us visualise the structure of our program and think about how the different classes would be related before starting implementation.
- We also discussed how the objects would interact during the simulation and which classes should be responsible for particular behaviours.
- Started planning the **user interface**, including how the simulation would be displayed and how the user would interact with it.
- Looked at other teams' designs and compared the different approaches they were taking to the same assignment. I particularly liked the **bee simulation**, as the different objects and behaviours seemed to fit naturally into the simulation.
- Overall, this week helped us move from having a general idea for our project to creating an initial **object-oriented design** that we could use as a foundation for implementation.

---

## Week 04 — Inheritance and Overloading

## Team Project

- Our team continued developing our **zombie simulation**.
- We identified `Character` as a suitable **superclass** for our `Human` and `Zombie` classes because both represent characters in the simulation and share common properties/behaviour.
- Our initial hierarchy was:

```text
Character
├── Human
└── Zombie
```

- The idea was to move common fields and methods into `Character` instead of duplicating them in both `Human` and `Zombie`.
- `Human` and `Zombie` can then specialise their behaviour where necessary using **method overriding (`@Override`)**.
- This helped us understand the **IS-A relationship**: a Human IS-A Character, and a Zombie IS-A Character.
- We also discussed **HAS-A relationships**, and implemented it in our program (Character's position HAS-A Vector2D)

## UML Design

We used **lucid chart** to update our UML class diagram and represent the relationships between our classes.

One thing we were still unsure about was how to correctly represent **IS-A vs HAS-A relationships using UML arrows**.

Our understanding at this point was:

```text
Human ───────▷ Character
Zombie ──────▷ Character
```

The inheritance relationship represents **IS-A**.

For example:

> A Zombie IS-A Character.

Whereas if another class contains a `Character` object, that would represent a **HAS-A** relationship.

We wanted to clarify the exact UML notation and arrow types before finalising the diagram.

![[zombie simulation UML.png]]
*fig: UML ver 1.*

## Concepts Learned

This week helped me understand the difference between:

- **Inheritance (`extends`)**: creating a subclass from an existing class.
- **Interfaces (`implements`)**: defining a contract that a class must follow.
- **Overriding**: a subclass providing its own implementation of an inherited method.
- **Overloading**: using the same method name with different parameters.
- **Polymorphism**: allowing different subclasses to be treated as their common superclass.

## Reflection

The main thing I learned this week was that inheritance should be used when there is a genuine **IS-A relationship**, rather than just because two classes have similar code.

For our project, `Character` made sense as a superclass because both `Human` and `Zombie` are types of characters and can share common attributes and behaviour.

We were still unsure about some of our UML relationships, particularly distinguishing **inheritance (IS-A)** from **composition/association (HAS-A)**. This is something we wanted to clarify before continuing to expand our class structure.

---

## Week 05 — Generics

## Generics

- This week we learned about **Java Generics**, more so about how generic type information is handled at compile time.
- We looked at **type erasure** during the TRAT where Java removes most generic type information during compilation. This means that `Container<String>` and `Container<Integer>` are essentially treated as the same generic class at runtime.
- We also covered **raw types**, **heap pollution**, and **unchecked warnings**, and how these can allow code that appears type-safe to cause runtime errors.

## EPIC Activity

- We were given a generic `Container<T>` class that stores items in a `List<T>`, along with a `Main` class that correctly used `Container<String>`.
- We experimented with ways to break the type safety of the program **without using explicit casts**.
- We found that using a **raw `Container` type** can bypass the compiler's generic type checking.
- This allowed us to insert a different type of object into a container that was originally being used as a `Container<String>`.
- When the value was later retrieved as a `String`, the program could produce a **runtime `ClassCastException`**.
- This demonstrated that generics mainly provide **compile-time type safety** and that using raw types can undermine that safety.

## Project Application

- We also considered where generics could be useful in our **zombie simulation**.
- A generic such as `ArrayList<Resource>` could allow us to store resources in the inventory of human class.
- This could make our code safer by allowing the compiler to detect incorrect types before the program runs.

## Reflection

The EPIC activity was useful because we deliberately broke a program that initially looked type-safe and could see how raw types and unchecked operations allowed this to happen.

---

## Week 06 — Exceptions

## Exceptions & Call Stack

- This week we focused on **exceptions in Java** and how they interact with the **call stack**.
- We looked at how an exception is thrown in one method and can **propagate up through multiple method calls** until it is caught.
- We learned that when an exception propagates through a method, the rest of that method does not execute normally, meaning any statements after the method call are skipped.
- We also covered `try`, `catch`, `throw`, `throws`, and `finally`, particularly how `finally` can execute regardless of whether an exception occurs.

## EPIC Activity

- We worked with the `BarcodePuzzle` program, which had the call chain:

```text
star() → pipe() → caret() → chuck_a_fit()
```

- `chuck_a_fit()` was where we added the exceptions, while the other methods printed their symbols before and after calling the next method.
- We experimented with different **catch locations** to produce the required barcode outputs.
- Catching the exception in `pipe()` means `pipe()` can handle it and the remaining statements in `star()` can still execute.
- If the exception instead reaches `star()`, the exit statements in both `caret()` and `pipe()` are skipped because those methods did not catch the exception.
- We also looked at `finally` and how it can be used to ensure certain code executes even when an exception occurs.

## Project Application

We discussed how exceptions could apply to our **zombie simulation**.

Some possible situations where exceptions could be useful include:

- A zombie or human trying to move outside the environment.
- Trying to access an entity that no longer exists.
- An invalid position being provided.
- Invalid simulation configuration.

We could potentially create custom exceptions such as:

```text
InvalidPositionException
EntityNotFoundException
```

This would allow errors specific to our simulation to be handled more clearly.

## Reflection

The barcode activity was useful because we could directly see the effect of moving the `catch` to different levels of the call chain. It also brought up some ideas on how I could use exceptions in our group project.

---

## Week 07 — Presentation

## Presentation 1, Group A: Sydney Transport Simulation

Group A presented a Sydney transport simulation involving trains, cargo and a network.

- `Vehicle`class stores information such as speed and status.
- `Itinerary` stores the train's route and current position.
- They used **linear interpolation** to calculate a vehicle's position between nodes.
- `Network` consists of nodes and pathways, similar to vertices and edges in a graph.
- Used `A*` for finding routes through the network.
- Cargo was separated into passenger and freight types.
- Used **localisation/i18n** to support different languages.
- UI could display information about a train when hovering over it.

**Takeaway:** As a project gets larger, organising classes into appropriate packages becomes important. I would also consider separating simulation settings/configuration from the main simulation logic.

## Presentation 2, Group B: Bee Simulation

Group B presented a 3D bee simulation.

- Main `Bee` class handles bee behaviour.
- Worker bees collect nectar and return it to the hive.
- A bee can collect up to 15 nectar before returning.
- They were considering making worker bees abstract.

**Takeaway:** This made me think about whether inheritance is actually necessary or whether behaviours could be separated using composition.

## Reflection

The presentations helped me understand that OOP is also about making the code easy to extend and maintain.

The **Strategy Pattern** is useful when behaviour needs to be interchangeable, while the **Observer Pattern** is useful when one object needs to notify multiple other objects.

I also want to review my own inheritance structure and see whether **composition** would make some parts of my code easier to modify.

### What I would tell my Week 3 self

Think about how the design will handle **future changes**, not just the current requirements. If adding a new feature requires modifying lots of existing code, the design probably needs improvement.

---

## Week 08 — Patterns 2

## Design Pattern: State

- This week we were given **source code from different Java projects** and had to identify where design patterns were being used or where they could be applied.
- Our team chose the **State Pattern** as the focus of our discussion.
- We discussed how the State Pattern could be used instead of having an **enum representing the game state with a large collection of `if`/`else` statements**.
- The main idea was that a `GameState` class/interface can encapsulate the behaviour of each state separately, rather than having all state-specific logic inside one class.

## Debate

Our debate topic was essentially **whether changing from an enum-based state system to a GameState-based design would make the program better**.

We were assigned the **for** side, while the other team argued against it. We used a dice roll from our TA to decide which team would take each side.

The opposing team argued that an enum could be more efficient because the game could potentially become an **AAA-level game**, where CPU efficiency would be important. They also argued that keeping the state logic together in one place would make it easier to debug because developers could simply search through the `if`/`else` statements.

Our main arguments were:

- **Encapsulation:** each game state can contain its own behaviour instead of putting everything into one large class.
- **Isolation:** changing one state would be less likely to affect unrelated states.
- **Maintainability:** as more states are added, a large `if`/`else` structure can become difficult to manage.
- **Extensibility:** new states can be added without heavily modifying existing state logic.
- **Readability:** each state has a clear responsibility, making the code easier to understand.

We argued that CPU efficiency should not automatically be prioritised over good software design, especially when the performance difference is unlikely to be significant for this type of state management. We also argued that having everything in one block may make searching easier, but that does not necessarily make the overall design easier to maintain.

## Debate Result

The opposing team was already struggling to make a strong argument for the enum approach, so after the main debate we decided to have a second dice roll to determine the winner.

We rolled a **D20**:

- **Our team:** 20
- **Other team:** 7

We won the debate and then somehow won the dice roll by getting the maximum possible number.

## Reflection

The main thing I took away from this activity was that design patterns are not just about making code work; they are about choosing a structure that makes the code easier to **maintain, extend and understand**.
