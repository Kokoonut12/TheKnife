package theknife;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

/**

 * Classe Menu

 * 

 * Gestisce l'interfaccia principale dell'applicazione TheKnife.

 * Permette di:

 *  - Cercare ristoranti

 *  - Registrarsi come utente

 *  - Effettuare login

 *  - Visualizzare recensioni

 *  - Gestire preferiti e recensioni per i clienti

 *  - Gestire ristoranti e rispondere alle recensioni per i ristoratori

 */

public class Menu {
    private Scanner scanner = new Scanner(System.in);//input della tastiera
    private List<Ristorante> restaurants;//lista di tutti i ristorante
    private List<Utente> utilisateurs; // Necessario per il metodo utilisateursClients()
    
    /**

         * Costruttore Menu

         * Carica i ristoranti e gli utenti dai file CSV

         */
    
    public Menu() {
        restaurants = GestioneDati.caricaRistorante("data/ristoranti.csv");
        // Caricamento dei dati degli utenti dal file csv
        utilisateurs = GestioneDati.caricaUtente("data/utenti.csv");
    }
    /**

         * Menu principale

         * Mostra le opzioni iniziali e gestisce le scelte dell'utente

         */

    public void visualizzareMenuPrincipale() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n=== Benvenuto/a in TheKnife ===");
            System.out.println("1. Ricerca ristoranti");
            System.out.println("2. Login");
            System.out.println("3. Registrazione");
            System.out.println("4. Mostra recensioni");
            System.out.println("5. ESCI");
            System.out.print("Inserisci la tua scelta: ");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1": ricercaRistorante(); break;
                case "2": login(); break;
                case "3": registrazione(); break;
                case "4": visualizzareRecensione(); break;
                case "5":
                    System.out.println("Arrivederci!");
                    continuer = false;
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }
    
    /**

         * Visualizza tutte le recensioni dei ristoranti

         * Le recensioni vengono lette da avis.csv e anonimizate

         */

    private void visualizzareRecensione() {
        System.out.println("=== Recensioni dei ristoranti ===");

        Map<String, List<String>> avisParResto = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/avis.csv"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length >= 4) {
                    String utilisateur = parts[0];
                    String restoNom = parts[1];
                    String etoiles = parts[2];
                    String texte = parts[3];

                    // Anonimizzazione: al posto del nome dell'utente, viene mostrato "anonimo"
                    String avisTexte = etoiles + "★ - " + texte + " (anonyme)";

                    avisParResto
                        .computeIfAbsent(restoNom, k -> new ArrayList<>())
                        .add(avisTexte);
                }
            }
        } catch (Exception e) {
            System.out.println("Errore lettura avis.csv: " + e.getMessage());
        }

        if (avisParResto.isEmpty()) {
            System.out.println("Nessuna recensione disponibile.");
        } else {
            for (Map.Entry<String, List<String>> entry : avisParResto.entrySet()) {
                System.out.println(entry.getKey()); // Nom du restaurant
                for (String avis : entry.getValue()) {
                    System.out.println("Recensione: " + avis);
                }
            }
        }
    }


	// === 1. Ricerca (visitatori) ===
    private void ricercaRistorante() {
        System.out.print("Inserisci una città: ");
        String ville = scanner.nextLine();

        List<Ristorante> resultats = Ricerca.ricercaCitta(restaurants, ville);

        if (resultats.isEmpty()) {
            System.out.println("Nessun ristorante trovato a " + ville);
        } else {
            System.out.println("Ristoranti trovati a " + ville + ":");
            for (Ristorante r : resultats) {
                System.out.println(r);
                for (Recensione avis : r.getRecensione()) {
                    System.out.println(" - " + avis);
                }
            }
        }
    }

    // === 2. Login ===
    private void login() {
        System.out.println("\n=== Login ===");
        System.out.print("Nome utente: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String passwordHash = String.valueOf(password.hashCode());

        List<Utente> utilisateurs = GestioneDati.caricaUtente("data/utenti.csv");
        Utente utilisateurConnecte = null;

        for (Utente u : utilisateurs) {
            if (u.getUsername().equals(username) && u.passwordHash.equals(passwordHash)) {
                utilisateurConnecte = u;
                break;
            }
        }

        if (utilisateurConnecte == null) {
            System.out.println("Credenziali errate.");
            return;
        }

        System.out.println("Accesso riuscito! Benvenuto/a " + utilisateurConnecte.username + ".");

        if (utilisateurConnecte instanceof Cliente) {
            menuCliente((Cliente) utilisateurConnecte);
        } else if (utilisateurConnecte instanceof Ristoratore) {
            // Cast
            Ristoratore risto = (Ristoratore) utilisateurConnecte;
            // Carica i suoi ristoranti prima di mostrare il menu
            caricaRistoranteDelProprietario(risto);
            // Avvia il sottomenu
            menuRistoratore(risto);
        }
    }
    
    /**

         * Carica tutti i ristoranti di un ristoratore

         */
    
    private void caricaRistoranteDelProprietario(Ristoratore risto) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/restaurants_proprietaires.csv"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length == 2) {
                    String username = parts[0];
                    String restoNom = parts[1];
                    if (username.equalsIgnoreCase(risto.getUsername())) {
                        // Ricerca del ristorante all'interno dell'elenco completo
                        for (Ristorante r : restaurants) {
                            if (r.getNome().equalsIgnoreCase(restoNom)) {
                                risto.aggiungereRistorante(r);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nel caricamento dei ristoranti del proprietario: " + e.getMessage());
        }
    }


    // === 3. Registrazione ===
    private void registrazione() {
        System.out.println("\n=== Registrazione ===");

        System.out.print("Nome: ");
        String prenom = scanner.nextLine();
        System.out.print("Cognome: ");
        String nom = scanner.nextLine();
        System.out.print("Nome utente: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String passwordHash = String.valueOf(password.hashCode());
        System.out.print("Residenza (città): ");
        String domicile = scanner.nextLine();
        System.out.print("Ruolo (cliente/ristoratore): ");
        String role = scanner.nextLine().toLowerCase();

        String ligne = prenom + ";" + nom + ";" + username + ";" + passwordHash + ";;" + domicile + ";" + role;

        try (FileWriter fw = new FileWriter("data/utenti.csv", true)) {
            fw.write(ligne + "\n");
            System.out.println("Utente registrato con successo!");
        } catch (Exception e) {
            System.out.println("Errore durante la registrazione: " + e.getMessage());
        }
    }

 // === Sottomenu' cliente ===
    
    // === Sotto-menu dei cliente ===
    /**
     * Menu per i clienti, per gestire preferiti e recensioni.
     *
     * @param client il cliente loggato
     */

    
    private void menuCliente(Cliente client) {
        // Carica i ristoranti preferiti e le recensioni dell'utente
        GestioneDati.caricaPreferiti(client, restaurants);
        GestioneDati.caricaRecensione(client, restaurants);

        boolean continuer = true;
        while (continuer) {
            System.out.println("\n=== Menu Cliente ===");
            System.out.println("1. Visualizza preferiti");
            System.out.println("2. Aggiungi un ristorante ai preferiti");
            System.out.println("3. Rimuovi un ristorante dai preferiti");
            System.out.println("4. Aggiungi una recensione");
            System.out.println("5. Visualizza le mie recensioni");
            System.out.println("6. Elimina una recensione");
            System.out.println("7. Modifica una recensione");
            System.out.println("8. Ricerca Ristorante");
            System.out.println("9. Logout");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1": client.visuallizzarePreferiti(); break;

                case "2":
                    System.out.print("Nome del ristorante: ");
                    String fav = scanner.nextLine();
                    for (Ristorante r : restaurants) {
                        if (r.getNome().equalsIgnoreCase(fav)) {
                            if (!client.getPreferiti().contains(r)) {
                                client.aggiungerePreferiti(r);
                                GestioneDati.salvaPreferiti(client.getUsername(), r);
                                System.out.println("Aggiunto ai preferiti!");
                            } else {
                                System.out.println("Già presente nei tuoi preferiti.");
                            }
                            break;
                        }
                    }
                    break;

                case "3": // Rimuove un ristorante dalla lista dei preferiti del cliente
                    if (client.getPreferiti().isEmpty()) {
                        System.out.println("Non hai nessun preferito.");
                        break;
                    }

                    System.out.print("Nome del ristorante da rimuovere dai preferiti: ");
                    String favSup = scanner.nextLine();

                    boolean supprime = false;
                    for (Ristorante r : new ArrayList<>(client.getPreferiti())) {
                        if (r.getNome().equalsIgnoreCase(favSup)) {
                            client.eliminaPreferiti(r);
                            supprime = true;
                            break;
                        }
                    }

                    if (supprime) {
                        System.out.println("✅ " + favSup + " rimosso dai preferiti!");
                        // Il file è stato aggiornato correttamente dopo la rimozione
                        GestioneDati.aggiornamentoPreferitiCSV(Collections.singletonList(client));
                    } else {
                        System.out.println("Questo ristorante non è nei tuoi preferiti.");
                    }
                    break;

                case "4": // Aggiunge una nuova recensione per un ristorante
                    System.out.print("Nome del ristorante: ");
                    String restoNom = scanner.nextLine();
                    Ristorante cible = trovaRistorantePerNome(restoNom);

                    if (cible == null) {
                        System.out.println("Ristorante non trovato.");
                        break;
                    }
                    if (clientPossedeAvisSurNom(client, restoNom)) {
                        System.out.println("Hai già lasciato una recensione per questo ristorante. Usa '7. Modifica'.");
                        break;
                    }

                    System.out.print("Stelle (1-5): ");
                    int stars = Integer.parseInt(scanner.nextLine());
                    System.out.print("Commento: ");
                    String texte = scanner.nextLine();

                    boolean cree = client.lasciareRecensione(cible, stars, texte);
                    if (cree) {
                        // Salvataggio effettuato SOLO quando si aggiunge un nuovo elemento
                        GestioneDati.salvaRecensione(client.getUsername(), cible, stars, texte);
                        System.out.println("Recensione aggiunta!");
                    }
                    break;

                case "5": client.visualizzareMieRecensione(); break;

                case "6": // Rimuove una recensione lasciata dal cliente
                    client.visualizzareMieRecensione();
                    if (client.getMieRecensione().isEmpty()) break;
                    System.out.print("Nome del ristorante da eliminare: ");
                    String suppNom = scanner.nextLine();
                    client.eliminareRecensione(suppNom);
                    // réécriture complète du fichier (pas de sauvegarde append)
                    GestioneDati.aggiornamentoRecensioneCSV(utilisateursClients());
                    break;

                case "7": // Modifica di una recensione esistente da parte del cliente
                    client.visualizzareMieRecensione();
                    if (client.getMieRecensione().isEmpty()) break;

                    System.out.print("Nome del ristorante da modificare: ");
                    String modNom = scanner.nextLine();
                    System.out.print("Nuove stelle (1-5): ");
                    int newStars = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nuovo commento: ");
                    String newTxt = scanner.nextLine();

                    if (client.modificareRecensione(modNom, newStars, newTxt)) {
                        GestioneDati.aggiornamentoRecensioneCSV(Collections.singletonList(client));
                        System.out.println("Recensione modificata per " + modNom);
                    } else {
                        System.out.println("Nessuna recensione trovata per questo ristorante.");
                    }
                    break;




                case "8": 
                	System.out.print("Inserisci una città: ");
                    String ville = scanner.nextLine();

                    List<Ristorante> resultats = Ricerca.ricercaCitta(restaurants, ville);

                    if (resultats.isEmpty()) {
                        System.out.println("Nessun ristorante trovato a " + ville);
                    } else {
                        System.out.println("Ristoranti trovati a " + ville + ":");
                        for (Ristorante r : resultats) {
                            System.out.println(r);
                            for (Recensione avis : r.getRecensione()) {
                                System.out.println(" - " + avis);
                            }
                        }
                    }break;
               
                case"9":continuer = false; break;

                default: System.out.println("Scelta non valida.");
            }
        }
    }
    private Ristorante trovaRistorantePerNome(String nom) {
        for (Ristorante r : restaurants) {
            if (r.getNome().equalsIgnoreCase(nom)) return r;
        }
        return null;
    }

    // Sicurezza nel caso in cui Map<Ristorante,Recensione> non corrisponda tramite equals/hashCode
    private boolean clientPossedeAvisSurNom(Cliente client, String nomResto) {
        for (Ristorante r : client.getMieRecensione().keySet()) {
            if (r.getNome().equalsIgnoreCase(nomResto)) return true;
        }
        return false;
    }


    // Recupera solo i clienti tra tutti gli utenti
    private List<Cliente> utilisateursClients() {
        List<Cliente> clients = new ArrayList<>();
        for (Utente u : utilisateurs) {
            if (u instanceof Cliente) {
                clients.add((Cliente) u);
            }
        }
        return clients;
    }


	// === Sottomenu' ristoratore ===
    private void menuRistoratore(Ristoratore risto) {
    	 /**
         * Menu per i ristoratori, per gestire i propri ristoranti e rispondere alle recensioni.
         *
         * @param risto il ristoratore loggato
         */

        boolean continuer = true;
        while (continuer) {
            System.out.println("\n=== Menu Ristoratore ===");
            System.out.println("1. Aggiungi un ristorante");
            System.out.println("2. Visualizza i miei ristoranti e recensioni");
            System.out.println("3. Rispondi a una recensione");
            System.out.println("4. Logout");
            String choix = scanner.nextLine();

            switch (choix) {
            case "1":
                System.out.println("=== Aggiungi un ristorante ===");
                System.out.print("Nome del ristorante: ");
                String nom = scanner.nextLine();

                System.out.print("Paese: ");
                String pays = scanner.nextLine();

                System.out.print("Città: ");
                String ville = scanner.nextLine();

                System.out.print("Indirizzo: ");
                String adresse = scanner.nextLine();

                System.out.print("Latitudine: ");
                double latitude = Double.parseDouble(scanner.nextLine());

                System.out.print("Longitudine: ");
                double longitude = Double.parseDouble(scanner.nextLine());

                System.out.print("Prezzo medio (€): ");
                double prixMoyen = Double.parseDouble(scanner.nextLine());

                System.out.print("Consegna disponibile? (si/no): : ");
                boolean livraison = scanner.nextLine().equalsIgnoreCase("oui");

                System.out.print("Prenotazione online? (si/no): ");
                boolean reservation = scanner.nextLine().equalsIgnoreCase("oui");

                System.out.print("Tipo di cucina: ");
                String typeCuisine = scanner.nextLine();

                Ristorante r = new Ristorante(nom, pays, ville, adresse, latitude, longitude,
                                              prixMoyen, livraison, reservation, typeCuisine);

                risto.aggiungereRistorante(r);
                restaurants.add(r);
                // Salvataggio del ristorante in ristoranti.csv
                try (FileWriter fw = new FileWriter("data/ristoranti.csv", true)) {
                    String ligne = nom + ";" + pays + ";" + ville + ";" + adresse + ";" +
                                   latitude + ";" + longitude + ";" + prixMoyen + ";" +
                                   (livraison ? "si" : "no") + ";" +
                                   (reservation ? "si" : "no") + ";" + typeCuisine;
                    fw.write(ligne + "\n");
                    System.out. println("Ristorante salvato in ristoranti.csv!");
                } catch (Exception e) {
                    System.out.println("Errore durante il salvataggio: " + e.getMessage());
                }

                System.out.println("Ristorante aggiunto con successo!");
                // Salvataggio del collegamento ristoratore-ristorante nel file restaurants_proprietaires.csv
                try (FileWriter fw = new FileWriter("data/restaurants_proprietaires.csv", true)) {
                    fw.write(risto.getUsername() + ";" + nom + "\n");
                } catch (Exception e) {
                    System.out.println("Errore nel salvataggio del proprietario: " + e.getMessage());
                }
                break;

                case "2": risto.visualizzareMieiRistorante(); // Mostra i ristoranti del ristoratore
                    break; 

                case "3":
                    System.out.print("Nome del ristorante: ");
                    String restoNom = scanner.nextLine();

                    Ristorante cible = trovaRistorantePerNome(restoNom);
                    if (cible == null || !risto.getMesRestaurants().contains(cible)) {
                        System.out.println("Questo ristorante non esiste o non ti appartiene.");
                        break;
                    }

                    List<Recensione> avis = cible.getRecensione();
                    if (avis.isEmpty()) {
                        System.out.println("Nessuna recensione trovata per questo ristorante.");
                        break;
                    }

                    for (int i = 0; i < avis.size(); i++) {
                        Recensione rec = avis.get(i);
                        System.out.println(i + ". " + rec);
                        if (rec.getRisposta() != null) {
                            System.out.println("Risposta già data: " + rec.getRisposta());
                        }
                    }

                    System.out.print("Numero della recensione: ");
                    int index = Integer.parseInt(scanner.nextLine());

                    if (index < 0 || index >= avis.size()) {
                        System.out.println("Indice non valido.");
                        break;
                    }

                    Recensione avisChoisi = avis.get(index);
                    if (avisChoisi.getRisposta() != null) {
                        System.out.println("Hai già risposto a questa recensione.");
                        break;
                    }

                    System.out.print("Risposta: ");
                    String rep = scanner.nextLine();
                    avisChoisi.setRisposta(rep);
                    System.out.println("Risposta registrata!");
                    break;
                case "4": continuer = false; break;
            }
        }
    }
}
