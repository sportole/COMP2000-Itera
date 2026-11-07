
package itera.model.spells;

import itera.model.human.Human;
import java.util.ArrayList;

public class HealingCircleSpell {

    public static final int RADIUS = 105;

    private static final int HEAL_AMOUNT = 25;

    private final int x;
    private final int y;

    public HealingCircleSpell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int cast(ArrayList<Human> humans) {

        int healed = 0;

        for (Human human : humans) {

            // Ignore dead or sheltered humans
            if (!human.isAlive() || human.isSheltered()) {
                continue;
            }

            // Only heal injured humans
            if (human.getHealth() >= 100) {
                continue;
            }

            // Calculate distance from healing circle
            double dx = human.getX()
                    + human.getSize() / 2.0 - x;

            double dy = human.getY()
                    + human.getSize() / 2.0 - y;

            double distance = Math.sqrt(
                    dx * dx + dy * dy
            );

            // Heal humans inside the circle
            if (distance <= RADIUS) {

                human.restoreSpellHealth(HEAL_AMOUNT);

                healed++;
            }
        }

        return healed;
    }
}
