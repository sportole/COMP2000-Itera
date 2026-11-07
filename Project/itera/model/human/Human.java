package itera.model.human;

import itera.model.*;
import itera.model.Character;
import itera.model.zombie.*;
import java.awt.*;
import java.util.*;

public class Human extends Character {

    protected java.util.List<Building> buildings = java.util.List.of();
    protected java.util.List<Human> companions = java.util.List.of();
    private Building visitingBuilding;

    public void setEnvironment(java.util.List<Building> buildings, java.util.List<Human> companions) {
        this.buildings = buildings;
        this.companions = companions;
    }

    public boolean isSheltered() {
        return insideSafePoint || buildings.stream().anyMatch(b -> b.isInside(this));
    }

    public boolean canCollect(Resource resource) {
        return isAlive() && !resource.isCollected()
            && (!(resource instanceof Weapon) || this instanceof Soldier soldier && !soldier.isArmed())
            && (!(resource instanceof Medicine) || this instanceof Medic medic && medic.getMedKits() == 0)
            && (!(resource instanceof Food) || health < 100);
    }

    protected boolean clearShot(Character target) {
        for (Building building : buildings) {
            if (new java.awt.geom.Rectangle2D.Double(building.getX(), building.getY(),
                    building.getWidth(), building.getHeight()).intersectsLine(
                    getX() + size / 2.0, getY() + size / 2.0,
                    target.getX() + target.getSize() / 2.0, target.getY() + target.getSize() / 2.0)) return false;
        }
        return true;
    }

    private Resource wantedResource(Building building) {
        for (Resource resource : building.getResources())
            if (canCollect(resource)) return resource;
        return null;
    }

    protected boolean seekResources() {
        for (Building building : buildings) {
            if (building.canEnter(this) && building.overlaps(getX(), getY(), size)) {
                visitingBuilding = building;
                break;
            }
        }

        if (visitingBuilding == null) {
            for (Building building : buildings) {
                if (building.canEnter(this) && wantedResource(building) != null) {
                    visitingBuilding = building;
                    break;
                }
            }
        }

        if (visitingBuilding == null) return false;

        Resource resource = wantedResource(visitingBuilding);
        if (resource == null && !visitingBuilding.overlaps(getX(), getY(), size)) {
            visitingBuilding = null;
            return false;
        }

        Vector2D destination = visitingBuilding.destination(this, resource);
        double vx = destination.getX() - position.getX();
        double vy = destination.getY() - position.getY();
        double length = Math.hypot(vx, vy);

        dx = length == 0 ? 0 : vx / length * Math.min(speed, length);
        dy = length == 0 ? 0 : vy / length * Math.min(speed, length);
        return true;
    }

    protected boolean pursueTarget(ArrayList<Zombie> zombies) {
        return false;
    }

    protected void moveTowards(Vector2D destination) {
        double vx = destination.getX() - position.getX();
        double vy = destination.getY() - position.getY();
        double length = Math.hypot(vx, vy);

        dx = length == 0 ? 0 : vx / length * Math.min(speed, length);
        dy = length == 0 ? 0 : vy / length * Math.min(speed, length);
    }

    protected int stamina = 100;

    protected static final int MAX_STAMINA = 100;
    protected static final int STAMINA_DRAIN = 1;
    protected static final int STAMINA_RECOVERY = 1;

    protected ArrayList<Resource> inventory = new ArrayList<>();

    protected static final double NORMAL_SPEED = 3.0;
    protected static final double DETECTION_RANGE = 300;

    protected boolean insideSafePoint = false;

    protected long lastDamageTime = 0;
    protected long lastHealTime = 0;

    protected static final long DAMAGE_COOLDOWN = 600;
    protected static final long HEAL_INTERVAL = 3000;

    protected Random random = new Random();

    protected double dx;
    protected double dy;

    @SuppressWarnings("this-escape")
    public Human(int x, int y) {
        super(100, NORMAL_SPEED, x, y, 15);
        initialize();
    }

    public void initialize() {
        chooseRandomDirection();
    }

    public void useItem(Resource item) {
        if (inventory.contains(item)) {
            item.use(this);
        }
    }

    public void addResource(Resource resource) {
        if (!canCollect(resource)) return;

        inventory.removeIf(r -> r.getQuantity() <= 0);
        Resource carried = resource.inventoryCopy();
        carried.setTimeSource(timeSource);
        inventory.add(carried);

        if (carried instanceof Food) {
            carried.use(this);
        }
    }

    public void interact(Object object) {
        if (object instanceof Resource resource && canCollect(resource)) {
            addResource(resource);
            resource.collect();
        } else if (object instanceof Building building) {
            building.interact(this);
        }
    }

    public void update(
            int worldWidth,
            int worldHeight,
            ArrayList<Zombie> zombies,
            SafePoint safePoint) {

        if (insideSafePoint) {
            dx = 0;
            dy = 0;

            healInsideSafePoint();
            recoverStamina();

            if (health >= 80) {
                insideSafePoint = false;
                position.setX(safePoint.getDoorX() + 20);
                position.setY(safePoint.getDoorCentreY());
                chooseRandomDirection();
            }
            return;
        }

        if ((isSheltered() || health > 20) && seekResources()) {
            recoverStamina();

        } else if (health <= 20) {
            moveTowardsSafePoint(safePoint);

            double distance = position.distanceTo(
                new Vector2D(safePoint.getDoorX(), safePoint.getDoorCentreY())
            );

            if (distance <= 25) {
                insideSafePoint = true;

                Vector2D restPosition =
                    safePoint.getRandomRestPosition(random, size);

                position.setX(restPosition.getX());
                position.setY(restPosition.getY());

                dx = 0;
                dy = 0;
                lastHealTime = now();
                return;
            }

        } else if (pursueTarget(zombies)) {
            recoverStamina();

        } else {
            Zombie nearestZombie = findNearestZombie(zombies);

            if (nearestZombie != null) {
                double distance =
                    position.distanceTo(nearestZombie.getPosition());

                if (distance <= DETECTION_RANGE) {
                    fleeFromMultipleZombies(
                        zombies, worldWidth, worldHeight
                    );
                    drainStamina();
                } else {
                    roam();
                    recoverStamina();
                }

            } else {
                roam();
                recoverStamina();
            }
        }

        double nextX = position.getX() + dx;
        double nextY = position.getY() + dy;

        boolean buildingBlocked = buildings.stream().anyMatch(
            b -> b.blocksMovement(nextX, nextY, size, b.canEnter(this))
        );

        if (buildingBlocked || safePoint.blocksHumanMovement(
                position.getX(),
                position.getY(),
                nextX,
                nextY,
                size,
                health <= 20)) {

            chooseRandomDirection();

        } else {
            position.add(dx, dy);
        }

        keepInsideWorld(worldWidth, worldHeight);
    }

    protected Zombie findNearestZombie(ArrayList<Zombie> zombies) {
        Zombie nearest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Zombie zombie : zombies) {
            if (!zombie.isAlive()) {
                continue;
            }

            double distance =
                position.distanceTo(zombie.getPosition());

            if (distance < closestDistance) {
                closestDistance = distance;
                nearest = zombie;
            }
        }

        return nearest;
    }

    // Original escape method retained for compatibility.
    protected void fleeFrom(
            Zombie zombie,
            int worldWidth,
            int worldHeight) {

        double directionX = position.getX() - zombie.getX();
        double directionY = position.getY() - zombie.getY();

        double distance = Math.hypot(directionX, directionY);

        if (distance > 0) {
            directionX /= distance;
            directionY /= distance;
        }

        int margin = 60;

        if (position.getX() < margin) {
            directionX += 1.8;
        }

        if (position.getX() > worldWidth - size - margin) {
            directionX -= 1.8;
        }

        if (position.getY() < margin) {
            directionY += 1.8;
        }

        if (position.getY() > worldHeight - size - margin) {
            directionY -= 1.8;
        }

        double length = Math.hypot(directionX, directionY);

        if (length > 0) {
            double fleeSpeed =
                stamina <= 0 ? speed * 0.5 : speed;

            dx = directionX / length * fleeSpeed;
            dy = directionY / length * fleeSpeed;
        }
    }

    // Smart Escape AI: considers all nearby zombies.
    protected void fleeFromMultipleZombies(
            ArrayList<Zombie> zombies,
            int worldWidth,
            int worldHeight) {

        double escapeX = 0;
        double escapeY = 0;

        for (Zombie zombie : zombies) {
            if (!zombie.isAlive()) {
                continue;
            }

            double differenceX =
                position.getX() - zombie.getX();

            double differenceY =
                position.getY() - zombie.getY();

            double distance =
                Math.hypot(differenceX, differenceY);

            if (distance > 0 && distance <= DETECTION_RANGE) {

                // Closer zombies have greater influence.
                double weight = 1.0 / (distance * distance);

                escapeX += differenceX / distance * weight;
                escapeY += differenceY / distance * weight;
            }
        }

        // Encourage humans to stay away from world edges.
        int margin = 60;

        if (position.getX() < margin) {
            escapeX += 0.01;
        }

        if (position.getX() > worldWidth - size - margin) {
            escapeX -= 0.01;
        }

        if (position.getY() < margin) {
            escapeY += 0.01;
        }

        if (position.getY() > worldHeight - size - margin) {
            escapeY -= 0.01;
        }

        double length = Math.hypot(escapeX, escapeY);

        if (length > 0) {
            double fleeSpeed =
                stamina <= 0 ? speed * 0.5 : speed;

            dx = escapeX / length * fleeSpeed;
            dy = escapeY / length * fleeSpeed;
        } else {
            chooseRandomDirection();
        }
    }

    protected void drainStamina() {
        stamina -= STAMINA_DRAIN;

        if (stamina < 0) {
            stamina = 0;
        }
    }

    protected void recoverStamina() {
        stamina += STAMINA_RECOVERY;

        if (stamina > MAX_STAMINA) {
            stamina = MAX_STAMINA;
        }
    }

    public int getStamina() {
        return stamina;
    }

    public void restoreStamina(int amount) {
        stamina = Math.min(MAX_STAMINA, stamina + amount);
    }

    protected void moveTowardsSafePoint(SafePoint safePoint) {
        double directionX =
            safePoint.getDoorX() - position.getX();

        double directionY =
            safePoint.getDoorCentreY() - position.getY();

        double distance =
            Math.hypot(directionX, directionY);

        if (distance > 0) {
            dx = directionX / distance * speed;
            dy = directionY / distance * speed;
        }
    }

    protected void roam() {
        if (random.nextInt(100) < 2) {
            chooseRandomDirection();
        }
    }

    protected void chooseRandomDirection() {
        double angle = random.nextDouble() * Math.PI * 2;

        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
    }

    protected void keepInsideWorld(int width, int height) {
        if (position.getX() < 0) {
            position.setX(0);
            dx = Math.abs(dx);
        }

        if (position.getX() > width - size) {
            position.setX(width - size);
            dx = -Math.abs(dx);
        }

        if (position.getY() < 0) {
            position.setY(0);
            dy = Math.abs(dy);
        }

        if (position.getY() > height - size) {
            position.setY(height - size);
            dy = -Math.abs(dy);
        }
    }

    /**
     * Applies zombie damage when this human is outside the safe point
     * and the damage cooldown has finished.
     */
    public boolean receiveZombieHit(int damage) {
        if (isSheltered()) {
            return false;
        }

        long currentTime = now();

        if (currentTime - lastDamageTime < DAMAGE_COOLDOWN) {
            return false;
        }

        takeDamage(damage);
        lastDamageTime = currentTime;

        return true;
    }

    protected void healInsideSafePoint() {
        long currentTime = now();

        if (currentTime - lastHealTime >= HEAL_INTERVAL) {
            health += 10;

            if (health > 100) {
                health = 100;
            }

            lastHealTime = currentTime;
        }
    }

    public boolean isInSafePoint() {
        return insideSafePoint;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 137, 123));
        g.fillOval(getX(), getY(), size, size);
        drawHealthBar(g);
    }

    protected void drawHealthBar(Graphics g) {
        int width = 24;
        int height = 4;

        int x = getX() - 4;
        int y = getY() - 8;

        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);

        int remaining = (int) (width * health / 100.0);

        g.setColor(Color.GREEN);
        g.fillRect(x, y, remaining, height);
    }

    protected void drawTypeLabel(Graphics g, String label) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(label, getX() - 5, getY() + size + 12);
    }
    
    /**
     * Restore health when an automatic Healing Circle is activated.
     * Health cannot exceed the maximum of 100.
     */
    public void restoreSpellHealth(int amount) {
        if (!isAlive() || amount <= 0) {
            return;
        }

        health = Math.min(100, health + amount);
    }

}