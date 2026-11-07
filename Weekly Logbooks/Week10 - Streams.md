
## Topics Covered

- Java Streams API and lambda expressions.
- Stream pipelines and intermediate operations such as `filter()` and `map()`.
- Terminal operations such as `collect()` and `noneMatch()`.
- Refactoring explicit loops into stream pipelines.

## EPIC Activity: Applying Streams to Itera

This week, I refactored parts of our zombie simulation project, Itera, by replacing explicit loops in `World.java` with Java stream pipelines.

- `**isCleared()**`: Replaced a loop that checked whether any living zombies remained with `noneMatch()`. The method now returns `true` when no living zombies exist in the world.

- `**getHumans()**`: Used `filter()` to select human characters and `map()` to convert them into `Human` objects before collecting them into an `ArrayList`.

- `**getZombies()**`: Applied the same approach to filter zombie characters and collect them into an `ArrayList`.


These changes reduced the amount of repetitive code while preserving the existing return types and behaviour. I also learned how method references, such as `Human.class::isInstance` and `Human.class::cast`, can make stream pipelines more concise.

**Commit link:** https://github.com/daksharora-7106/COMP2000-Itera/commit/84cf78bdc49cd07f6a8dcd6a5b0683ce25876131

## Class Activities

Our neighbouring class joined our session because their TA, David, was absent. This meant we worked alongside students from another class during the activities and discussions.

We discussed the advantages and disadvantages of replacing loops with streams, specially about readability, performance, and correctness. Streams can make filtering and checking collections easier to understand, although a traditional loop may sometimes be clearer for more complicated logic.

## Reflection

This week's activity helped me understand how streams can simplify collection processing in an object-oriented project. Instead of manually creating lists and checking every character, I could express the required operations through a pipeline. However, streams are not automatically better than loops; the choice depends on which approach makes the code easier to understand and maintain.