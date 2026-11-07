## Overview

Week 13 was the final student showcase, where students presented their simulations, explained their design decisions, and reflected on what they had learned throughout the semester.

Although ten students had registered to present, only a few attended to present their work. I had not registered because the presentation slots were full, but I presented my team's Itera zombie simulation anyway when time remained. I wanted to present the project and explain the work I had contributed throughout the semester.

## My Presentation

I presented the Itera zombie simulation, explaining its overall design and some of the technical improvements made during development. I discussed my contributions to the population statistics system, including the use of Java Streams, custom collectors, and parallel processing.

I also explained how the system captures human statistics before processing them in parallel, helping avoid unsafe access to the simulation's mutable world state.

Presenting the project gave me an opportunity to explain not just what the code does, but why certain implementation decisions were made.

## Notes from Another Team's Presentation

Only one other team presented: a train network simulation.

The team explained that each member had implemented parallel processing in their respective parts of the code. They reported that this was intended to improve processing performance as they increased the size of their simulated network from six to twenty.

Their approach demonstrated how parallelism could be applied across different components of a simulation rather than being limited to a single calculation.

However, increasing the network size and introducing parallel processing does not automatically establish a performance improvement. A benchmark comparing execution times before and after the changes would be needed to verify the benefit.

## Design Idea I Would Like to Explore

One technique I would like to explore further is applying parallel processing to more computationally expensive parts of a simulation, provided those operations can be separated safely.

In Itera, I introduced parallelism into population statistics rather than the simulation's core behaviour. The train network team's approach made me consider whether other independent calculations in our simulation could also benefit from parallel execution.

I would first identify suitable operations, then benchmark them before and after parallelisation to determine whether the change actually improves performance.

## Final Reflection

Throughout the semester, I developed a better understanding of object-oriented programming, design patterns, Java Streams, collectors, and parallel processing through the development of Itera.

One aspect I am proud of is contributing to the population statistics system. I moved from using built-in collectors to implementing a custom accumulator, then extended the implementation to support parallel processing while accounting for the simulation's mutable state.

The final showcase also gave me an opportunity to explain my technical decisions and learn from another team's approach to improving simulation performance.

If I were to continue developing Itera, I would investigate performance bottlenecks more systematically, use benchmarks to evaluate optimisation decisions, and explore whether other independent calculations could be parallelised safely.

Overall, this project helped me understand that good software engineering involves more than implementing features. It also requires careful design, testing, maintainability, and evidence that proposed optimisations provide meaningful benefits.