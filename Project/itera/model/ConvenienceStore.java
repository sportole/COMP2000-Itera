
package itera.model;

import java.awt.*;

public class ConvenienceStore extends Building {

    public ConvenienceStore(int x, int y) {

        super(15, x, y, 180, 180, "SUPPLY HUB");

        // Original food stock
        for (int i = 0; i < 9; i++) {
            stock.add(new Food(
                1, 20,
                x + 40 + (i % 3) * 35,
                y + 65 + (i / 3) * 30
            ));
        }
    }

    /**
     * Removes food from the store's stock.
     *
     * @return the next food item, or null when none is available
     */
    public Food getFood() {

        Resource resource = loot();

        if (resource instanceof Food food) {
            return food;
        }

        return null;
    }

    @Override
    protected void drawBuildingBody(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Sandy-brown building
        g2.setColor(new Color(125, 100, 70));
        g2.fillRect(x, y, width, height);

        // Main roof
        g2.setColor(new Color(165, 135, 95));
        g2.fillRect(x + 12, y + 12, width - 24, height - 24);

        // Roof border
        g2.setColor(new Color(85, 65, 45));
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(x + 12, y + 12, width - 24, height - 24);

        // Storage crate 1
        g2.setColor(new Color(95, 65, 40));
        g2.fillRect(x + 25, y + 70, 35, 35);

        g2.setColor(new Color(205, 165, 105));
        g2.drawRect(x + 25, y + 70, 35, 35);
        g2.drawLine(x + 25, y + 70, x + 60, y + 105);
        g2.drawLine(x + 60, y + 70, x + 25, y + 105);

        // Storage crate 2
        g2.setColor(new Color(95, 65, 40));
        g2.fillRect(x + 120, y + 70, 35, 35);

        g2.setColor(new Color(205, 165, 105));
        g2.drawRect(x + 120, y + 70, 35, 35);
        g2.drawLine(x + 120, y + 70, x + 155, y + 105);
        g2.drawLine(x + 155, y + 70, x + 120, y + 105);

        // Loading area
        g2.setColor(new Color(70, 60, 50));
        g2.fillRect(x + 60, y + 125, 60, 28);

        // Loading area stripes
        g2.setColor(new Color(215, 175, 75));
        g2.fillRect(x + 65, y + 130, 10, 5);
        g2.fillRect(x + 85, y + 130, 10, 5);
        g2.fillRect(x + 105, y + 130, 10, 5);

        g2.dispose();
    }
}
