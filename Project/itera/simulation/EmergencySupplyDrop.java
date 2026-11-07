
package itera.simulation;

import itera.model.Medicine;
import itera.model.human.Human;
import java.awt.*;

public final class EmergencySupplyDrop {

    private static final int MEDICINE_QUANTITY = 3;
    private static final int HEAL_AMOUNT = 20;

    private final Medicine medicine;
    private final long spawnedAt;
    private final long duration;

    public EmergencySupplyDrop(
            int x,
            int y,
            long spawnedAt,
            long duration) {

        if (x < 0 || y < 0) {
            throw new IllegalArgumentException(
                "Supply drop position cannot be negative"
            );
        }

        if (duration <= 0) {
            throw new IllegalArgumentException(
                "Supply drop duration must be positive"
            );
        }

        this.medicine = new Medicine(
            MEDICINE_QUANTITY,
            HEAL_AMOUNT,
            x,
            y
        );

        this.spawnedAt = spawnedAt;
        this.duration = duration;
    }

    public int getX() {
        return medicine.getX();
    }

    public int getY() {
        return medicine.getY();
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public boolean isActive(long currentTime) {
        return currentTime >= spawnedAt
            && currentTime - spawnedAt < duration
            && !medicine.isCollected();
    }

    /**
     * Allow an eligible survivor to collect the medicine.
     *
     * Existing Human and Medicine behaviour handles
     * inventory management and healing supplies.
     */
    public boolean tryCollect(Human human, long currentTime) {

        if (human == null || !isActive(currentTime)) {
            return false;
        }

        if (!human.canCollect(medicine)) {
            return false;
        }

        if (!medicine.isNear(human)) {
            return false;
        }

        human.interact(medicine);

        return medicine.isCollected();
    }

    /**
     * Draw a highlighted emergency supply crate.
     */
    public void draw(Graphics graphics, long currentTime) {

        if (!isActive(currentTime)) {
            return;
        }

        Graphics2D g = (Graphics2D) graphics.create();

        try {
            int x = getX();
            int y = getY();

            // Golden landing-zone highlight.
            g.setColor(new Color(255, 193, 7, 65));
            g.fillOval(x - 14, y - 14, 46, 46);

            g.setColor(new Color(255, 179, 0));
            g.setStroke(new BasicStroke(2f));
            g.drawOval(x - 9, y - 9, 36, 36);

            // Supply crate.
            g.setColor(new Color(102, 67, 29));
            g.fillRoundRect(x - 3, y - 3, 24, 24, 5, 5);

            g.setColor(new Color(255, 213, 79));
            g.drawRoundRect(x - 3, y - 3, 24, 24, 5, 5);

            // White medical symbol.
            g.setColor(Color.WHITE);
            g.fillRect(x + 6, y + 1, 6, 15);
            g.fillRect(x + 2, y + 5, 14, 6);

            // Label above the drop.
            g.setColor(new Color(150, 90, 0));
            g.setFont(new Font("Arial", Font.BOLD, 11));
            g.drawString("SUPPLY DROP", x - 28, y - 20);

        } finally {
            g.dispose();
        }
    }
}
