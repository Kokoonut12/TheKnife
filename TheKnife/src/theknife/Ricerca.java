package theknife;

import java.util.*;
/**

 * Classe di utilità che fornisce metodi per effettuare ricerche sui ristoranti.

 */

public class Ricerca {
/**

	     * Cerca tutti i ristoranti che si trovano in una determinata città.

	     *

	     * @param restaurants Lista di tutti i ristoranti disponibili

	     * @param citta La città da cercare (case-insensitive)

	     * @return Lista di ristoranti che si trovano nella città indicata

	     */
	public static List<Ristorante> ricercaCitta(List<Ristorante> ristorante, String citta) {
	    List<Ristorante> resultats = new ArrayList<>();
	    String ricerca = citta.trim().toLowerCase(); // Normalizzazione dell'input dell'utente
	    // Filtra i ristoranti in base alla città (confronto case-insensitive)

	    for (Ristorante r : ristorante) {
	        if (r.getCitta().trim().toLowerCase().equals(ricerca)) {
	            resultats.add(r);
	        }
	    }
	    return resultats;
	}

}

