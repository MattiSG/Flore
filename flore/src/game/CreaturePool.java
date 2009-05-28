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
    
    // interdire la création d'une classe pool
    private CreaturePool() {}

    // méthode renvoyant une créature clonée
    //  à partir de la liste existante
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
