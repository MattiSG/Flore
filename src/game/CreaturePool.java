package game;

import element.creature.Creature;

import java.util.Map;
import java.util.HashMap;

public class CreaturePool {
    private static final Map<String,Creature> pool = new HashMap<String,Creature>();
    
    private CreaturePool() {}

    public static Creature getCreature(String id) {
        if (!pool.containsKey(id))
            pool.put(id, new Creature(id));

        try {
            return pool.get(id).clone();
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
