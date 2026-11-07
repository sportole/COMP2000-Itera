
package itera.model.spells;

import itera.model.human.Human;
import java.util.ArrayList;

public class PoisonCloudSpell {

    public static final int RADIUS = 95;

    private static final int DAMAGE_PER_TICK = 2;

    private final int x;
    private final int y;

    public PoisonCloudSpell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ArrayList<Human> damage(ArrayList<Human> humans) {

        ArrayList<Human> killed = new ArrayList<>();

        for (Human human : humans) {

            // Ignore dead humans and sheltered humans
            if (!human.isAlive() || human.isSheltered()) {
                continue;
            }

            // Calculate distance from poison cloud
            double dx = human.getX()
                    + human.getSize() / 2.0 - x;

            double dy = human.getY()
                    + human.getSize() / 2.0 - y;

            double distance = Math.sqrt(
                    dx * dx + dy * dy
            );

            // Damage humans inside the poison cloud
            if (distance <= RADIUS) {

                human.takeDamage(DAMAGE_PER_TICK);

                // Track humans killed by poison
                if (!human.isAlive()) {
                    killed.add(human);
                }
            }
        }

        return killed;
    }
}
