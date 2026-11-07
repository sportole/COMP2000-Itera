
package itera.model;

import java.awt.*;

public class Hospital extends Building {

    public Hospital(int x, int y) {

        super(10, x, y, 180, 180, "MEDICAL BAY");

        // Original medicine stock
        for (int i = 0; i < 6; i++) {
            stock.add(new Medicine(
                3, 20,
                x + 40 + (i % 3) * 35,
                y + 75 + (i / 3) * 40
            ));
        }
    }

    public void treat(Character character) {

        character.health += 20;

        if (character.health > 100) {
            character.health = 100;
        }
    }

    @Override
    protected void drawBuildingBody(Graphics g) {

        // Light grey medical building
        g.setColor(new Color(170, 185, 185));
        g.fillRect(x, y, width, height);

        // Inner roof panel
        g.setColor(new Color(205, 215, 210));
        g.fillRect(x + 12, y + 12, width - 24, height - 24);

        // Dark windows
        g.setColor(new Color(55, 75, 85));
        g.fillRect(x + 25, y + 65, 35, 22);
        g.fillRect(x + 120, y + 65, 35, 22);

        // Red medical cross
        g.setColor(new Color(190, 35, 45));
        g.fillRect(x + 84, y + 90, 14, 48);
        g.fillRect(x + 67, y + 107, 48, 14);
    }
}
