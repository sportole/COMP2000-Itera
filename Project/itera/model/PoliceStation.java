
package itera.model;

import java.awt.*;

public class PoliceStation extends Building {

    public PoliceStation(int x, int y) {

        super(10, x, y, 180, 180, "DEFENCE POST");

        // Original weapon stock
        for (int i = 0; i < 6; i++) {
            stock.add(new Weapon(
                5, 50, 5,
                x + 40 + (i % 3) * 35,
                y + 75 + (i / 3) * 40
            ));
        }
    }

    /**
     * Removes a weapon from the police station's stock.
     *
     * @return the next weapon, or null when none is available
     */
    public Weapon getWeapon() {

        Resource resource = loot();

        if (resource instanceof Weapon weapon) {
            return weapon;
        }

        return null;
    }

    @Override
    protected void drawBuildingBody(Graphics g) {

        // Dark blue-grey building
        g.setColor(new Color(65, 80, 100));
        g.fillRect(x, y, width, height);

        // Inner roof
        g.setColor(new Color(90, 105, 125));
        g.fillRect(x + 12, y + 12, width - 24, height - 24);

        // Reinforced roof panels
        g.setColor(new Color(40, 50, 65));
        g.fillRect(x + 25, y + 65, 35, 25);
        g.fillRect(x + 120, y + 65, 35, 25);

        // Security stripe
        g.setColor(new Color(190, 160, 65));
        g.fillRect(x + 20, y + 105, width - 40, 6);

        // Central reinforced hatch
        g.setColor(new Color(35, 45, 60));
        g.fillRect(x + 72, y + 120, 36, 35);

        // Hatch border
        g.setColor(new Color(165, 175, 185));
        g.drawRect(x + 72, y + 120, 36, 35);

        // Hatch handle
        g.setColor(new Color(220, 220, 220));
        g.fillOval(x + 88, y + 135, 5, 5);
    }
}
