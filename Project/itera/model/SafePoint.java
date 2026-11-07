
package itera.model;

import java.awt.*;
import java.util.*;

public class SafePoint {

    private int defenseLevel;
    private int capacity;

    private int x;
    private int y;

    private int width;
    private int height;

    private int doorY;
    private int doorHeight;

    public SafePoint(int x, int y, int width, int height, int capacity) {

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.capacity = capacity;

        defenseLevel = 100;

        doorHeight = 50;

        doorY = y + height / 2 - doorHeight / 2;
    }

    public void restock() {

        defenseLevel = 100;
    }

    public int getDoorX() {
        return x + width;
    }

    public int getDoorCentreY() {

        return doorY + doorHeight / 2;
    }

    public boolean contains(double objectX, double objectY, int objectSize) {

        double centreX = objectX + objectSize / 2.0;

        double centreY = objectY + objectSize / 2.0;

        return centreX > x
            && centreX < x + width
            && centreY > y
            && centreY < y + height;
    }

    public boolean wouldZombieEnter(double zombieX, double zombieY, int zombieSize) {

        return zombieX + zombieSize > x
            && zombieX < x + width
            && zombieY + zombieSize > y
            && zombieY < y + height;
    }

    public boolean blocksHumanMovement(
            double currentX,
            double currentY,
            double nextX,
            double nextY,
            int humanSize,
            boolean allowedToEnter) {

        boolean intersects =
            nextX + humanSize > x
            && nextX < x + width
            && nextY + humanSize > y
            && nextY < y + height;

        if (!intersects) {
            return false;
        }

        double centreY = nextY + humanSize / 2.0;

        boolean atDoor =
            centreY >= doorY
            && centreY <= doorY + doorHeight;

        boolean approachingFromRight =
            currentX >= x + width
            && nextX < currentX;

        if (allowedToEnter && atDoor && approachingFromRight) {
            return false;
        }

        return true;
    }

    public Vector2D getRandomRestPosition(Random random, int size) {

        int restX =
            x + 25 + random.nextInt(width - size - 50);

        int restY =
            y + 45 + random.nextInt(height - size - 65);

        return new Vector2D(restX, restY);
    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Dark green shelter
        g2.setColor(new Color(65, 90, 70));
        g2.fillRect(x, y, width, height);

        // Inner roof panel
        g2.setColor(new Color(85, 110, 85));
        g2.fillRect(
            x + 12,
            y + 12,
            width - 24,
            height - 24
        );

        // Roof details
        g2.setColor(new Color(40, 65, 45));
        g2.fillRect(x + 25, y + 65, 35, 25);
        g2.fillRect(x + 120, y + 65, 35, 25);

        // Shelter symbol
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.drawString("+", x + 80, y + 115);

        // Original border with doorway gap
        g2.setColor(new Color(25, 45, 30));
        g2.setStroke(new BasicStroke(4));

        g2.drawLine(x, y, x + width, y);
        g2.drawLine(x, y, x, y + height);
        g2.drawLine(x, y + height, x + width, y + height);

        g2.drawLine(x + width, y, x + width, doorY);

        g2.drawLine(
            x + width,
            doorY + doorHeight,
            x + width,
            y + height
        );

        // Highlight the entrance without closing the gap
        g2.setColor(new Color(225, 190, 95));
        g2.setStroke(new BasicStroke(3));

        g2.drawLine(
            x + width,
            doorY + 5,
            x + width,
            doorY + doorHeight - 5
        );

        // Building name
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 19));
        g2.drawString("SHELTER", x + 43, y + 35);

        g2.dispose();
    }
}
