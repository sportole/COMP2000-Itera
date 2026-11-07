
package itera.model.zombie;

import itera.model.*;
import itera.model.Character;
import itera.model.human.*;
import itera.model.zombie.targeting.*;

import java.awt.*;
import java.util.*;

public class Zombie extends Character {

    public enum AIBehaviour {
        CHASE,
        FLANK,
        REGROUP
    }

    private AIBehaviour currentBehaviour = AIBehaviour.CHASE;

    private java.util.List<Building> buildings = java.util.List.of();

    protected double detectionRange = 500;

    protected TargetingStrategy targetingStrategy = new NearestTarget();

    protected static final int DAMAGE = 25;
    protected static final double ATTACK_DISTANCE = 20;

    private int blockedTicks = 0;
    private int behaviourTicks = 0;
    private int flankDirection = 1;

    public Zombie(int x, int y) {
        super(100, 1.875, x, y, 18);
    }

    public void receiveGunshot() {
        if (isAlive()) {
            takeDamage(50);
        }
    }

    public void setBuildings(java.util.List<Building> buildings) {
        this.buildings = buildings == null
                ? java.util.List.of()
                : buildings;
    }

    public TargetingStrategy getTargetingStrategy() {
        return targetingStrategy;
    }

    public void setTargetingStrategy(TargetingStrategy strategy) {
        this.targetingStrategy = Objects.requireNonNull(
                strategy,
                "Targeting strategy is required"
        );
    }

    public AIBehaviour getCurrentBehaviour() {
        return currentBehaviour;
    }

    public Human update(
            int worldWidth,
            int worldHeight,
            ArrayList<Human> humans,
            SafePoint safePoint) {

        if (!isAlive()) {
            return null;
        }

        Human target = findTarget(humans);

        if (target == null || target.isSheltered()) {
            currentBehaviour = AIBehaviour.CHASE;
            return null;
        }

        double distance = position.distanceTo(target.getPosition());

        if (distance > detectionRange) {
            return null;
        }

        if (distance <= ATTACK_DISTANCE) {
            boolean attacked = performAttack(target);

            if (attacked && !target.isAlive()) {
                return target;
            }

            return null;
        }

        updateBehaviour(target, humans);

        double previousX = position.getX();
        double previousY = position.getY();

        switch (currentBehaviour) {
            case FLANK:
                flank(target);
                break;

            case REGROUP:
                regroup(target, humans);
                break;

            case CHASE:
            default:
                chase(target);
                break;
        }

        double nextX = position.getX();
        double nextY = position.getY();

        if (isBlocked(nextX, nextY, safePoint)) {

            position.setX(nextX);
            position.setY(previousY);

            if (isBlocked(
                    position.getX(),
                    position.getY(),
                    safePoint)) {

                position.setX(previousX);
                position.setY(nextY);

                if (isBlocked(
                        position.getX(),
                        position.getY(),
                        safePoint)) {

                    position.setX(previousX);
                    position.setY(previousY);
                    blockedTicks++;
                }
            }

        } else {
            blockedTicks = Math.max(0, blockedTicks - 1);
        }

        keepInsideWorld(worldWidth, worldHeight);

        return null;
    }

    private void updateBehaviour(
            Human target,
            ArrayList<Human> humans) {

        behaviourTicks++;

        // Avoid switching strategies every frame.
        if (behaviourTicks < 25) {
            return;
        }

        behaviourTicks = 0;

        int nearbySoldiers = 0;

        for (Human human : humans) {
            if (!human.isAlive() || human.isSheltered()) {
                continue;
            }

            if (human instanceof Soldier
                    && position.distanceTo(
                            human.getPosition()) < 130) {

                nearbySoldiers++;
            }
        }

        if (blockedTicks >= 3) {
            currentBehaviour = AIBehaviour.FLANK;
            flankDirection *= -1;
            blockedTicks = 0;
            return;
        }

        if (nearbySoldiers >= 2) {
            currentBehaviour = AIBehaviour.FLANK;
            return;
        }

        if (nearbySoldiers == 1
                && getHealthRatio() < 0.5) {

            currentBehaviour = AIBehaviour.REGROUP;
            return;
        }

        currentBehaviour = AIBehaviour.CHASE;
    }

    private double getHealthRatio() {
        return Math.max(
                0.0,
                Math.min(
                        1.0,
                        health / (double) getMaxHealth()
                )
        );
    }

    private void flank(Human target) {

        double dx = target.getX() - position.getX();
        double dy = target.getY() - position.getY();

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            return;
        }

        double forwardX = dx / distance;
        double forwardY = dy / distance;

        double sideX = -forwardY * flankDirection;
        double sideY = forwardX * flankDirection;

        double flankStrength = distance > 65 ? 0.7 : 0.2;

        moveDirection(
                forwardX + sideX * flankStrength,
                forwardY + sideY * flankStrength
        );
    }

    private void regroup(
            Human target,
            ArrayList<Human> humans) {

        // Without a shared zombie list, regrouping uses
        // a defensive movement away from the target.
        // World.java integration will enable real grouping.

        double dx = position.getX() - target.getX();
        double dy = position.getY() - target.getY();

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            return;
        }

        moveDirection(
                dx / distance,
                dy / distance
        );
    }

    private void moveDirection(double dx, double dy) {

        double length = Math.sqrt(dx * dx + dy * dy);

        if (length <= 0) {
            return;
        }

        position.add(
                dx / length * speed,
                dy / length * speed
        );
    }

    protected Human findTarget(ArrayList<Human> humans) {
        return targetingStrategy
                .chooseTarget(this, humans)
                .orElse(null);
    }

    protected boolean performAttack(Human target) {
        return target.receiveZombieHit(DAMAGE);
    }

    public void attack(Character target) {
        target.takeDamage(DAMAGE);
    }

    public void chase(Character target) {

        double directionX = target.getX() - position.getX();
        double directionY = target.getY() - position.getY();

        double distance = Math.sqrt(
                directionX * directionX
                        + directionY * directionY
        );

        if (distance > 0) {
            position.add(
                    directionX / distance * speed,
                    directionY / distance * speed
            );
        }
    }

    private boolean isBlocked(
            double x,
            double y,
            SafePoint safePoint) {

        if (safePoint != null
                && safePoint.wouldZombieEnter(x, y, size)) {
            return true;
        }

        for (Building building : buildings) {
            if (building.overlaps(x, y, size)) {
                return true;
            }
        }

        return false;
    }

    protected void keepInsideWorld(
            int width,
            int height) {

        if (position.getX() < 0) {
            position.setX(0);
        }

        if (position.getX() > width - size) {
            position.setX(width - size);
        }

        if (position.getY() < 0) {
            position.setY(0);
        }

        if (position.getY() > height - size) {
            position.setY(height - size);
        }
    }

 @Override
public void draw(Graphics g) {
    g.setColor(Color.RED);
    g.fillOval(getX(), getY(), size, size);

    drawTypeLabel(g, "Z");
}

    public int getMaxHealth() {
        return 100;
    }

    
protected void drawHealthBar(Graphics g) {

    int width = Math.max(24, size);
    int barX = getX() + (size - width) / 2;
    int barY = getY() - 9;

    int remaining = (int) Math.round(
        width * Math.max(
            0,
            Math.min(1.0, health / (double) getMaxHealth())
        )
    );

    // Dark background for missing health
    g.setColor(Color.DARK_GRAY);
    g.fillRect(barX, barY, width, 4);

    // RED health for zombies
    g.setColor(Color.RED);
    g.fillRect(barX, barY, remaining, 4);
}


        
    protected void drawTypeLabel(
            Graphics g,
            String label) {

        drawHealthBar(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font(
                "Arial",
                Font.BOLD,
                10
        ));

        g.drawString(
                label,
                getX() - 5,
                getY() + size + 12
        );
    }
}
