package theknife;

import java.io.*;
import java.util.*;
/**

 * La classe {@code GestioneDati} gestisce la persistenza dei dati

 * (ristoranti, utenti, preferiti e recensioni) tramite file CSV.

 * <p>

 * Tutti i metodi sono statici e permettono di:

 * <ul>

 *     <li>Caricare i dati da file CSV</li>

 *     <li>Salvare nuovi dati (recensioni, preferiti, ecc.)</li>

 *     <li>Aggiornare i file esistenti</li>

 * </ul>

 */

public class GestioneDati {

    // --- Ristoranti ---
 /**

	     * Carica i ristoranti da un file CSV.

	     *

	     * @param chemin percorso del file CSV

	     * @return lista dei ristoranti caricati

	     */
    public static List<Ristorante> caricaRistorante(String chemin) {
        List<Ristorante> liste = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            boolean premiere = true;
            while ((ligne = br.readLine()) != null) {
                if (premiere) { premiere = false; continue; } // Ignora intestazione
                String[] c = ligne.split(";");
                if (c.length == 10) {
                    Ristorante r = new Ristorante(
                        c[0], c[1], c[2], c[3],
                        Double.parseDouble(c[4]),
                        Double.parseDouble(c[5]),
                        Double.parseDouble(c[6]),
                        c[7].equalsIgnoreCase("si"),
                        c[8].equalsIgnoreCase("si"),
                        c[9]
                    );
                    liste.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nel caricamento dei ristoranti: " + e.getMessage());
        }
        return liste;
    }
    // Carica utente
	/**

	     * Carica gli utenti da un file CSV.

	     *

	     * @param chemin percorso del file CSV

	     * @return lista degli utenti caricati

	     */
    public static List<Utente> caricaUtente(String chemin) {
        List<Utente> utente = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] champs = ligne.split(";");
                if (champs.length >= 7) {
                    String nome = champs[0];
                    String cognome = champs[1];
                    String username = champs[2];
                    String passwordHash = champs[3];
                    String domicilio = champs[5];
                    String ruolo = champs[6].toLowerCase();

                    Utente u;
                    if (ruolo.equals("cliente")) {
                        u = new Cliente(nome, cognome, username, passwordHash, domicilio);
                    } else {
                        u = new Ristoratore(nome, cognome, username, passwordHash, domicilio);
                    }
                    utente.add(u);
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nel caricamento degli utenti: " + e.getMessage());
        }
        return utente;
    }

    // --- Preferiti ---
    /**

         * Salva un nuovo ristorante preferito per un cliente.

         *

         * @param username nome utente del cliente

         * @param resto    ristorante preferito

         */
    
    public static void salvaPreferiti(String username, Ristorante resto) {
        try (FileWriter fw = new FileWriter("data/favoris.csv", true)) {
            fw.write(username + ";" + resto.getNome() + "\n");
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio dei preferiti: " + e.getMessage());
        }
    }
    /**

         * Carica i preferiti di un cliente dal file CSV.

         *

         * @param client      cliente di riferimento

         * @param restaurants lista dei ristoranti disponibili

         */

    public static void caricaPreferiti(Cliente cliente, List<Ristorante> ristorante) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/favoris.csv"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] c = ligne.split(";");
                if (c.length == 2 && c[0].equals(cliente.getUsername())) {
                    for (Ristorante r : ristorante) {
                        if (r.getNome().equalsIgnoreCase(c[1])) {
                            cliente.aggiungerePreferiti(r);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // File inesistente o vuoto = nessun errore
        }
    }

    // --- Recensioni ---
    /**

         * Salva una nuova recensione in CSV.

         *

         * @param username nome utente del cliente

         * @param resto    ristorante recensito

         * @param etoiles  numero di stelle

         * @param texte    testo della recensione

         */
    public static void salvaRecensione(String username, Ristorante resto, int stelle, String testo) {
        try (FileWriter fw = new FileWriter("data/avis.csv", true)) {
            fw.write(username + ";" + resto.getNome() + ";" + stelle + ";" + testo.replace(";", ",") + "\n");
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio della recensione: " + e.getMessage());
        }
    }
    /**

         * Carica tutte le recensioni di un cliente dal file CSV.

         *

         * @param client      cliente di riferimento

         * @param restaurants lista dei ristoranti disponibili

         */

    public static void caricaRecensione(Cliente cliente, List<Ristorante> ristorante) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/avis.csv"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] c = ligne.split(";");
                if (c.length >= 4 && c[0].equals(cliente.getUsername())) {
                    String restoNom = c[1];
                    int etoiles = Integer.parseInt(c[2]);
                    String texte = c[3];

                    for (Ristorante r : ristorante) {
                        if (r.getNome().equalsIgnoreCase(restoNom)) {
                            Recensione rec = new Recensione(cliente, r, etoiles, texte);
                            r.aggiungereRecensione(rec);
                            cliente.getMieRecensione().put(r, rec);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // File inesistente o vuoto = nessun errore
        }
    }

	// Sovrascrive completamente il file recensioni.csv con le recensioni attuali dei clienti
    /**

         * Sovrascrive completamente il file avis.csv con le recensioni attuali dei clienti.

         *

         * @param clients lista di clienti

         */
    public static void aggiornamentoRecensioneCSV(List<Cliente> clients) {
        try (FileWriter fw = new FileWriter("data/avis.csv", false)) { // false = sovrascrive tutto
            for (Cliente c : clients) {
                for (Ristorante r : c.getMieRecensione().keySet()) {
                    Recensione rec = c.getMieRecensione().get(r);
                    fw.write(c.getUsername() + ";" 
                            + r.getNome() + ";" 
                            + rec.getStelle() + ";" 
                            + rec.getTesto().replace(";", ",") 
                            + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nell'aggiornamento di avis.csv: " + e.getMessage());
        }
    }
    // Sovrascrive completamente il file favoriti.csv con i preferiti attuali dei clienti
    /**

         * Sovrascrive completamente il file favoris.csv con i preferiti attuali dei clienti.

         *

         * @param clients lista di clienti

         */
    public static void aggiornamentoPreferitiCSV(List<Cliente> clients) {
        try (FileWriter fw = new FileWriter("data/favoris.csv", false)) { 
            for (Cliente c : clients) {
                for (Ristorante r : c.getPreferiti()) {
                    fw.write(c.getUsername() + ";" + r.getNome() + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nell'aggiornamento di favoris.csv: " + e.getMessage());
        }
    }
}