package itera.simulation;

import itera.model.human.Human;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Immutable values collected on the calling thread, with no live entity references.
 * Capture must not overlap a world update: World stores characters in a mutable
 * ArrayList, so concurrent additions/removals can cause inconsistent results or
 * ConcurrentModificationException; health reads can also observe mixed tick state.
 * The parallel reduction below only sees captured values.
 */
public final class PopulationSnapshot {
    public record RoleSummary(long count, long wounded, double averageHealth) { }

    private record HumanStats(String role, int health) { }

    private static final class RoleAccumulator {
        private long count;
        private long wounded;
        private long totalHealth;

        private void add(HumanStats human) {
            count++;
            if (human.health() < 100) wounded++;
            totalHealth += human.health();
        }

        private RoleAccumulator combine(RoleAccumulator other) {
            count += other.count;
            wounded += other.wounded;
            totalHealth += other.totalHealth;
            return this;
        }

        private RoleSummary toSummary() {
            return new RoleSummary(count, wounded, (double) totalHealth / count);
        }
    }

    private final Map<String, RoleSummary> roles;

    public PopulationSnapshot(List<Human> humans) {
        List<HumanStats> captured = humans.stream()
            .filter(Human::isAlive)
            .map(human -> new HumanStats(human.getClass().getSimpleName(), human.getHealth()))
            .toList();

        Map<String, RoleSummary> collected = captured.parallelStream()
            .collect(Collectors.groupingBy(HumanStats::role,
                TreeMap::new,
                Collector.of(RoleAccumulator::new, RoleAccumulator::add,
                    RoleAccumulator::combine, RoleAccumulator::toSummary)));
        roles = Collections.unmodifiableMap(collected);
    }

    public Map<String, RoleSummary> getRoles() { return roles; }
}
