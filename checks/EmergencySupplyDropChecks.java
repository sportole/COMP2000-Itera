package itera.checks;

import itera.model.SafePoint;
import itera.model.human.Civilian;
import itera.model.human.Medic;
import itera.simulation.EmergencySupplyDrop;
import itera.simulation.World;
import itera.simulation.SimulationSettings;
import itera.ui.Main;
import java.awt.Point;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/** Isolated checks for timed temporary medical drops and medic pickup behaviour. */
public final class EmergencySupplyDropChecks {
    private static int checks;

    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
        checks++;
    }

    private static void rejects(Class<? extends Throwable> error, Runnable action) {
        try {
            action.run();
        } catch (Throwable result) {
            check(error.isInstance(result), "Expected " + error.getSimpleName());
            return;
        }
        throw new AssertionError("Expected " + error.getSimpleName());
    }

    public static void main(String[] args) throws Exception {
        World world = new World();
        long start = world.getTime();
        check(world.getActiveEmergencySupplyDrops().isEmpty(), "No initial drops");

        EmergencySupplyDrop first = world.spawnEmergencySupplyDrop(350, 350, 25_000L);
        check(first.isActive(start), "Fresh drop is active");
        check(first.getMedicine().getQuantity() == 3, "Drop contains three medicine uses");
        check(world.getActiveEmergencySupplyDrops().size() == 1,
            "World exposes an active drop");
        rejects(UnsupportedOperationException.class,
            () -> world.getActiveEmergencySupplyDrops().clear());

        Civilian civilian = new Civilian(350, 350);
        check(!first.tryCollect(civilian, start), "Civilians cannot collect medicine");
        check(!first.tryCollect(null, start), "Null survivor cannot collect");

        Medic distant = new Medic(250, 350);
        world.addCharacter(distant);
        check(!first.tryCollect(distant, start), "Too-distant medic cannot collect");

        distant.setEmergencySupplyDrops(world.getActiveEmergencySupplyDrops());
        distant.setEnvironment(List.of(), List.of(distant));
        double before = distant.getPosition().distanceTo(
            new itera.model.Vector2D(first.getX(), first.getY()));
        distant.update(1200, 800, new ArrayList<>(),
            new SafePoint(0, 0, 180, 180, 10));
        double after = distant.getPosition().distanceTo(
            new itera.model.Vector2D(first.getX(), first.getY()));
        check(after < before, "Unarmed medic heads toward active supplies");

        Medic collector = new Medic(350, 350);
        world.addCharacter(collector);
        world.update();
        check(collector.getMedKits() == 3, "Medic collects all medicine uses");
        check(first.getMedicine().isCollected(), "Collected drop is marked collected");
        check(world.getActiveEmergencySupplyDrops().isEmpty(),
            "Collected supply no longer appears on map");
        check(!first.tryCollect(distant, start), "Drop cannot be collected twice");

        EmergencySupplyDrop later = world.spawnEmergencySupplyDrop(500, 500, 5_000L);
        check(later.isActive(world.getTime()), "Second drop starts active");
        world.advanceTime(5_000);
        world.update();
        check(!later.isActive(world.getTime()), "Expiry uses simulation time");
        check(world.getActiveEmergencySupplyDrops().isEmpty(), "Expired drop is removed");

        rejects(IllegalArgumentException.class,
            () -> new EmergencySupplyDrop(-1, 5, start, 100));
        rejects(IllegalArgumentException.class,
            () -> new EmergencySupplyDrop(1, 5, start, 0));
        rejects(NullPointerException.class, () -> distant.setEmergencySupplyDrops(null));

        // Check the actual Main spawning path without starting the Swing timer.
        SwingUtilities.invokeAndWait(() -> {
            try {
                Main simulation = new Main(new SimulationSettings(20, 10));
                simulation.setSize(1200, 800);
                Field worldField = Main.class.getDeclaredField("world");
                worldField.setAccessible(true);
                World gameWorld = (World) worldField.get(simulation);
                Field dueField = Main.class.getDeclaredField("nextSupplyDropTime");
                dueField.setAccessible(true);
                dueField.setLong(simulation, gameWorld.getTime());
                Method spawn = Main.class.getDeclaredMethod("updateTimedSupplyDrops");
                spawn.setAccessible(true);
                spawn.invoke(simulation);
                check(gameWorld.getActiveEmergencySupplyDrops().size() == 1,
                    "Main spawns a drop once the timer is due");
                EmergencySupplyDrop crate = gameWorld.getActiveEmergencySupplyDrops().get(0);
                check(crate.getX() >= 65 && crate.getY() >= 65,
                    "Supply drop respects the battlefield margin");
                check(!gameWorld.getActiveEmergencySupplyDrops().isEmpty(),
                    "Spawned crate can be rendered and shown on radar");
                spawn.invoke(simulation);
                check(gameWorld.getActiveEmergencySupplyDrops().size() == 1,
                    "Spawn timer prevents duplicate immediate drops");
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });

        System.out.println("Passed " + checks + " emergency supply checks.");
    }
}
