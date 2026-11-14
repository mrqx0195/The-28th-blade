package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import java.util.*;

public final class TideStacks {
    private static final int STACKS_TO_TRIGGER = 3;
    private static final long DECAY_TICKS = 60L;
    private static final int MAX_RECORDED_ATTACKERS = 64;
    
    private int stacks;
    private long expiresAt;
    
    private final Map<UUID, Long> attacks = new LinkedHashMap<>(16, 0.75f, true);
    
    public boolean hit(long now, UUID attacker, long attackId) {
        if (now >= expiresAt) {
            stacks = 0;
            attacks.clear();
        }
        if (Objects.equals(attacks.get(attacker), attackId)) {
            return false;
        }
        attacks.put(attacker, attackId);
        if (attacks.size() > MAX_RECORDED_ATTACKERS) {
            Iterator<UUID> lru = attacks.keySet().iterator();
            lru.next();
            lru.remove();
        }
        expiresAt = now + DECAY_TICKS;
        stacks++;
        if (stacks >= STACKS_TO_TRIGGER) {
            stacks = 0;
            return true;
        }
        return false;
    }
}
