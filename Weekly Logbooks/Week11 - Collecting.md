
## Topics Covered

- Using Java Collectors to collect stream elements into lists, sets, and maps.
- Grouping elements with `Collectors.groupingBy()`.
- Calculating summary statistics using `summarizingInt()` and related collectors.
- Creating custom collectors with `Collector.of()`.
- Combining downstream collectors to produce meaningful summaries.

## EPIC Activity: Population Snapshots in Itera

This week, I worked on `PopulationSnapshot.java` for our zombie simulation project. The goal was to collect meaningful population statistics by grouping living humans according to their roles.

I used `Collectors.groupingBy()` to group living humans by their class names, such as their specific human roles. Each group produces a `RoleSummary` containing three metrics:

- **Population count:** the number of living humans in that role.
- **Wounded count:** the number of humans whose health is below 100. 
- **Average health:** the average health of living humans in that role.

I also implemented a custom `RoleAccumulator` with `Collector.of()`. It maintains the count, wounded count, and total health as elements are processed, then converts these values into a `RoleSummary`. Its `combine()` method merges partial accumulations, while `toSummary()` calculates the average health.

This activity demonstrated how collectors can transform a collection of entities into structured statistical data. The snapshot stores the results in an unmodifiable map keeping the collected summaries separate from the live simulation entities.

The changes were committed to the shared repository in commit `24832df`. The existing checks passed, and the UI and displayed summary remained unchanged.

**End result**
![[Screenshot 2026-10-19 at 2.52.23 pm.png]]

## Reflection

This week's activity helped me understand how collectors can do more than simply create lists or maps. By combining grouping, filtering, and aggregation, we can produce useful statistics about the simulation without manually managing separate collections. Building a custom accumulator also showed me how to collect several related metrics together.

One important consideration is handling empty groups when calculating averages. In this implementation, the accumulator's `toSummary()` method divides total health by count, so it assumes that the accumulator contains at least one human. This is safe when summaries are produced only for non-empty groups, but the assumption should be considered if the collector is reused elsewhere.