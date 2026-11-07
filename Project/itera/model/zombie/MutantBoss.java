
package itera.model.zombie;

import itera.model.*;
import itera.model.human.*;
import java.awt.*;
import java.util.ArrayList;

public class MutantBoss extends Zombie {

    private static final int BOSS_HEALTH = 270;
    private static final int BOSS_DAMAGE = 50;
    private static final double BOSS_SPEED = 2.75;
    private static final int BOSS_SIZE = 40;
    private static final double BOSS_DETECTION_RANGE = 700;

    // Ground Smash settings
    private static final int SMASH_DAMAGE = 20;
    private static final double SMASH_RADIUS = 100;
    private static final long SMASH_COOLDOWN = 8000;
    private static final long SMASH_WARNING = 2000;

    private long lastSmashTime;
    private long warningStartTime;
    private boolean smashWarningActive = false;
    private boolean smashClockStarted = false;

    // Zombie Summoning
    private boolean hasSummoned = false;
    private boolean summonRequested = false;

    public MutantBoss(int x, int y) {
        super(x, y);

        this.health = BOSS_HEALTH;
        this.speed = BOSS_SPEED;
        this.size = BOSS_SIZE;
        this.detectionRange = BOSS_DETECTION_RANGE;
    }

    @Override
    protected boolean performAttack(Human target) {
        return target.receiveZombieHit(BOSS_DAMAGE);
    }

    /**
     * Called once per simulation tick.
     * Controls Ground Smash and Zombie Summoning.
     */
    public void updateSpecialAbilities(
            ArrayList<Human> humans,
            long currentTime) {

        if (!isAlive()) {
            return;
        }

        // Summon zombies once at 50% health.
        if (!hasSummoned && health <= BOSS_HEALTH / 2) {
            hasSummoned = true;
            summonRequested = true;
        }

        // Start the cooldown when the boss first appears.
        if (!smashClockStarted) {
            lastSmashTime = currentTime;
            smashClockStarted = true;
        }

        // Start warning after 8 seconds.
        if (!smashWarningActive
                && currentTime - lastSmashTime >= SMASH_COOLDOWN) {

            smashWarningActive = true;
            warningStartTime = currentTime;
        }

        // Execute Ground Smash after the warning.
        if (smashWarningActive
                && currentTime - warningStartTime >= SMASH_WARNING) {

            performGroundSmash(humans);

            smashWarningActive = false;
            lastSmashTime = currentTime;
        }
    }

    /**
     * Damage every nearby living human.
     */
    private void performGroundSmash(ArrayList<Human> humans) {

        double bossCenterX = getX() + size / 2.0;
        double bossCenterY = getY() + size / 2.0;

        for (Human human : humans) {

            if (!human.isAlive()) {
                continue;
            }

            double humanCenterX =
                human.getX() + human.getSize() / 2.0;

            double humanCenterY =
                human.getY() + human.getSize() / 2.0;

            double distance = Math.hypot(
                bossCenterX - humanCenterX,
                bossCenterY - humanCenterY
            );

            if (distance <= SMASH_RADIUS) {
                human.receiveZombieHit(SMASH_DAMAGE);
            }
        }
    }

    /**
     * World checks this to spawn reinforcements.
     * Returns true only once.
     */
    public boolean consumeSummonRequest() {

        if (!summonRequested) {
            return false;
        }

        summonRequested = false;
        return true;
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        int centerX = getX() + size / 2;
        int centerY = getY() + size / 2;

        // Show the Ground Smash warning circle.
        if (smashWarningActive) {

            g2.setColor(new Color(255, 100, 0, 65));

            g2.fillOval(
                (int) (centerX - SMASH_RADIUS),
                (int) (centerY - SMASH_RADIUS),
                (int) (SMASH_RADIUS * 2),
                (int) (SMASH_RADIUS * 2)
            );

            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(3));

            g2.drawOval(
                (int) (centerX - SMASH_RADIUS),
                (int) (centerY - SMASH_RADIUS),
                (int) (SMASH_RADIUS * 2),
                (int) (SMASH_RADIUS * 2)
            );
        }

        // Boss always remains purple.
        g2.setColor(new Color(74, 20, 140));
        g2.fillOval(getX(), getY(), size, size);

        drawHealthBar(g2);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));

        g2.drawString(
            smashWarningActive ? "BOSS - SMASH!" : "BOSS",
            getX(),
            getY() + size + 15
        );

        g2.dispose();
    }

    public int getBossHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return BOSS_HEALTH;
    }

    public int getMaxBossHealth() {
        return BOSS_HEALTH;
    }
}
