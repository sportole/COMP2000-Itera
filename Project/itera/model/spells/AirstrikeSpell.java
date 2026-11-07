
package itera.model.spells;

import itera.model.zombie.Zombie;
import java.util.ArrayList;

public class AirstrikeSpell {

    private static final int DAMAGE = 60;
    private static final double RADIUS = 100;

    private final int x;
    private final int y;

    public AirstrikeSpell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int explode(ArrayList<Zombie> zombies) {

        int zombiesHit = 0;

        for (Zombie zombie : zombies) {

            if (!zombie.isAlive()) {
                continue;
            }

            double dx = zombie.getX()
                    + zombie.getSize() / 2.0 - x;

            double dy = zombie.getY()
                    + zombie.getSize() / 2.0 - y;

            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= RADIUS) {
                zombie.takeDamage(DAMAGE);
                zombiesHit++;
            }
        }

        return zombiesHit;
    }
}
