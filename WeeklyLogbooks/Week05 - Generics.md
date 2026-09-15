

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