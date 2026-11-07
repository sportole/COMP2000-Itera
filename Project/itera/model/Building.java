
package itera.model;

import itera.model.human.*;
import java.awt.*;
import java.util.*;

public abstract class Building {

    protected int capacity;

    protected int x;
    protected int y;

    protected int width;
    protected int height;

    protected String name;

    protected ArrayList<Resource> stock = new ArrayList<>();

    public Building(int capacity, int x, int y, int width, int height, String name) {

        this.capacity = capacity;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.name = name;
    }

    public boolean isSecure() {
        return true;
    }

    public boolean contains(int objectX, int objectY) {

        return objectX >= x
            && objectX <= x + width
            && objectY >= y
            && objectY <= y + height;
    }

    /**
     * Removes the next resource from the building's stock.
     *
     * @return the next resource, or null when stock is empty
     */
    public Resource loot() {

        for (int i = 0; i < stock.size(); i++) {

            if (!stock.get(i).isCollected()) {
                return stock.remove(i);
            }
        }

        return null;
    }

    /**
     * Gives one stocked resource to a human when available.
     *
     * @param human the human receiving the resource
     */
    public void interact(Human human) {

        if (!canEnter(human) || !isInside(human)) {
            return;
        }

        for (Resource resource : stock) {

            if (!resource.isCollected()
                    && resource.isNear(human)
                    && human.canCollect(resource)) {

                human.interact(resource);
                break;
            }
        }
    }

    public boolean canEnter(Human human) {

        return human.isAlive()
            && (!(this instanceof PoliceStation)
                || human instanceof Soldier)
            && (!(this instanceof Hospital)
                || human instanceof Medic);
    }

    public java.util.List<Resource> getResources() {

        return java.util.Collections.unmodifiableList(stock);
    }

    public boolean isInside(Character character) {

        return character.getX() >= x + 4
            && character.getY() >= y + 4
            && character.getX() + character.getSize() <= x + width - 4
            && character.getY() + character.getSize() <= y + height - 4;
    }

    public boolean overlaps(double px, double py, int size) {

        return px + size > x
            && px < x + width
            && py + size > y
            && py < y + height;
    }

    public int getDoorX() {

        return x == 0 ? x + width : x;
    }

    public int getDoorY() {

        return y + height / 2;
    }

    public boolean blocksMovement(
            double px,
            double py,
            int size,
            boolean allowed) {

        if (!overlaps(px, py, size)) {
            return false;
        }

        if (!allowed) {
            return true;
        }

        // Solid top, bottom and back walls.
        // The entrance remains 50 pixels wide.
        if (py < y + 4 || py + size > y + height - 4) {
            return true;
        }

        if (x == 0
                ? px < x + 4
                : px + size > x + width - 4) {
            return true;
        }

        boolean crossesDoorWall =
            x == 0
                ? px + size > x + width - 4
                : px < x + 4;

        return crossesDoorWall
            && (py < getDoorY() - 25
                || py + size > getDoorY() + 25);
    }

    public Vector2D destination(Human human, Resource resource) {

        int side = x == 0 ? 1 : -1;

        double outsideX =
            getDoorX()
            + (side > 0 ? 24 : -24 - human.getSize());

        double doorY =
            getDoorY() - human.getSize() / 2.0;

        if (isInside(human)) {

            if (resource != null) {
                return new Vector2D(
                    resource.getX(),
                    resource.getY()
                );
            }

            // Align with the door before exiting.
            if (Math.abs(human.getY() - doorY) > 1) {
                return new Vector2D(
                    human.getPosition().getX(),
                    doorY
                );
            }

            return new Vector2D(outsideX, doorY);
        }

        // Align outside the building before entering.
        if (Math.abs(human.getY() - doorY) > 2) {

            if (side > 0
                    ? human.getX() < outsideX - 2
                    : human.getX() > outsideX + 2) {

                return new Vector2D(
                    outsideX,
                    human.getY()
                );
            }

            return new Vector2D(outsideX, doorY);
        }

        if (resource == null) {
            return new Vector2D(outsideX, doorY);
        }

        return new Vector2D(
            getDoorX() - side * 30 - human.getSize() / 2.0,
            doorY
        );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Subtle shadow
        g2.setColor(new Color(0, 0, 0, 65));
        g2.fillRect(x + 5, y + 5, width, height);

        // Draw the building's own roof and decorations
        drawBuildingBody(g2);

        // Dark outer walls
        g2.setColor(new Color(25, 30, 30));
        g2.setStroke(new BasicStroke(4));

        // Top wall
        g2.drawLine(x, y, x + width, y);

        // Bottom wall
        g2.drawLine(x, y + height, x + width, y + height);

        // Back wall
        int backX = x == 0 ? x : x + width;
        g2.drawLine(backX, y, backX, y + height);

        // Front wall with the original doorway gap
        int doorX = getDoorX();
        int doorCentreY = getDoorY();

        g2.drawLine(
            doorX, y,
            doorX, doorCentreY - 25
        );

        g2.drawLine(
            doorX, doorCentreY + 25,
            doorX, y + height
        );

        // Yellow doorway markers
        g2.setColor(new Color(220, 175, 70));
        g2.setStroke(new BasicStroke(3));

        g2.drawLine(
            doorX, doorCentreY - 25,
            doorX, doorCentreY - 15
        );

        g2.drawLine(
            doorX, doorCentreY + 15,
            doorX, doorCentreY + 25
        );

        // Dark label background
        g2.setColor(new Color(20, 25, 25, 210));
        g2.fillRoundRect(
            x + 8,
            y + 7,
            width - 16,
            43,
            8,
            8
        );

        // Building name
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString(name, x + 16, y + 24);

        // Remaining stock count
        long remainingStock = stock.stream()
            .filter(r -> !r.isCollected())
            .count();

        g2.setColor(new Color(220, 220, 220));
        g2.setFont(new Font("Arial", Font.PLAIN, 11));

        g2.drawString(
            "Stock: " + remainingStock,
            x + 16,
            y + 41
        );

        g2.dispose();
    }

    protected abstract void drawBuildingBody(Graphics g);
}
