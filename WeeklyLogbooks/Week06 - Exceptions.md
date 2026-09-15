
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