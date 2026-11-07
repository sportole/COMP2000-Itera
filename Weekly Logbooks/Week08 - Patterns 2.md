
## Design Pattern:  State

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

The debate also made me think about the difference between **theoretical performance** and practical software design. An enum may be slightly simpler, but that does not automatically make it the better choice if the state logic becomes large and difficult to maintain.