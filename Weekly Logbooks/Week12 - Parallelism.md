
## Overview

This week focused on concurrency and parallelism in Java, including parallel streams, race conditions, thread safety, and synchronisation. The practical activity involved identifying an opportunity to process data in parallel within the Itera zombie simulation.

My contribution focused on parallelising population statistics in `PopulationSnapshot.java` while ensuring that parallel processing did not access the simulation's mutable entities directly.

## EPIC Activity: Parallel Population Statistics

### Problem Identified

- Population statistics required processing living humans to calculate the number of characters, wounded characters, and average health for each role.
- Processing a large population sequentially could limit performance as the simulation grows.
- The `World` stores characters in a mutable `ArrayList`, and character health can change during simulation updates.
- Accessing or modifying this shared data concurrently could produce inconsistent results or cause a `ConcurrentModificationException`.

### Implementation

- **Captured an immutable representation of the data:** Introduced a `HumanStats` record containing each living human's role and health. The constructor captures these values before parallel aggregation, separating the calculation from the live simulation entities.

- **Introduced parallel processing:** Replaced the sequential stream used for aggregation with `captured.parallelStream()`, allowing Java to divide the statistical calculation into smaller tasks.

- **Used a custom collector:** Reused the `RoleAccumulator` from Week 11 to maintain the count, wounded count, and total health for each role.

- **Implemented reduction and combination:** Used the accumulator's `add()` method to process individual records and `combine()` to merge partial results produced during parallel processing.

- **Preserved the existing output:** Continued producing `RoleSummary` objects containing the character count, wounded count, and average health for each role.

- **Documented concurrency limitations:** Added documentation explaining that snapshot capture must not overlap with a world update because the underlying world and entities are mutable.

### Files Modified

| File                      | Changes                                                                                                                                                  |
| ------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `PopulationSnapshot.java` | Added the `HumanStats` record, captured living humans' role and health, and performed aggregation using `parallelStream()`.                              |
| `World.java`              | Added `populationSnapshot()` documentation explaining the requirement to capture statistics on the simulation thread without concurrent world mutations. |
| `Week811Checks.java`      | Added tests for parallel aggregation using a population of 1,000 humans.                                                                                 |


## Key Concepts Learned

- **Parallel streams:** `parallelStream()` allows Java to divide stream processing across multiple tasks, potentially using multiple threads.
- **Race conditions:** Occur when concurrent operations access shared data and the result depends on their timing or execution order.
- **Immutable snapshots:** Capturing role and health values into records separates the statistical calculation from mutable simulation entities.
- **Reduction:** Partial results can be combined into a final result. The custom collector's `combine()` method merges partial population statistics.
- **Thread safety:** Parallel tasks must not modify shared mutable accumulators without appropriate coordination. The custom collector allows Java to manage accumulation and combination through its supplied functions.
- **Synchronisation:** Concurrent access to mutable world data must be prevented or coordinated when capturing a snapshot.

## Reflection

This activity demonstrated that parallelising a calculation requires more than replacing `stream()` with `parallelStream()`. The data being processed must also be safe to access concurrently.

Capturing each living human's role and health into a `HumanStats` record allowed the aggregation stage to operate on a separate collection of values rather than repeatedly accessing live simulation entities. The custom collector could then calculate statistics in parallel and combine partial results.

An important limitation is that the initial capture still requires protection from concurrent world updates. Moving the aggregation to a parallel stream does not automatically make the original `World` or its entities thread-safe. The implementation documents this constraint, although the calling code must still ensure that capture and world updates do not overlap.

The change establishes a potential performance improvement for larger populations, but the tests validate correctness rather than demonstrate an actual speedup. Benchmarking sequential and parallel implementations would be necessary to determine whether parallel processing is worthwhile for the simulation's typical population sizes.