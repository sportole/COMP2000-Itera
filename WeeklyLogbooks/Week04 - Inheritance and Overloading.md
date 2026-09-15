
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