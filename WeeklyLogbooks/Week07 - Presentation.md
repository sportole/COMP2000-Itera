
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