package game;

import element.creature.Creature;

import t2s.SIVOXDevint;

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
    public static Creature getCreature(String id, SIVOXDevint player) {
        if (!pool.containsKey(id))
            pool.put(id, new Creature(id, player));

        try {
            Creature c = pool.get(id).clone();
            // ne pas oublier le init() sinon risque de bugs
            // la méthode clone des créatures réalise des
            // copies bits-à-bits, donc ne recopie que les
            // pointeurs pour les types non-primitifs
            c.init();
            return c;
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
