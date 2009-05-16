package game;

import element.creature.Creature;

import java.util.Map;
import java.util.HashMap;

/*
 * Classe permettant d'éviter le parsing des descriptions
 *  des insectes à chaque nouvelle création.
 *
 */
public class CreaturePool {
    // liste des créatures déjà parsées
    private static final Map<String,Creature> pool = new HashMap<String,Creature>();
    
    private CreaturePool() {}

    public static Creature getCreature(String id) {
        if (!pool.containsKey(id))
            pool.put(id, new Creature(id));

        try {
            Creature c = pool.get(id).clone();
            c.init();
            return c;
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
