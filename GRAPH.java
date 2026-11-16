import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Beschreiben Sie hier die Klasse Graph.
 * 
 * @author Anja Stube
 * @version 2023-04-24
*/
public class GRAPH
{
    private KNOTEN[] knotennamen;
    private int[][] adja;    
    private int aktAnzahl, maxAnzahl;

    boolean[] besucht;     //Feld, um zu markieren, ob der Knoten schon besucht wurde
    int[] warteschlange;   //abzuarbeitende Warteschlange
    int[] reihenfolge; //Feld für die Ausgabe der Reihenfolge
    
    
     public GRAPH(int anzKnoten)
    {
        adja = new int[anzKnoten][anzKnoten];
        knotennamen = new KNOTEN[anzKnoten];
        maxAnzahl = anzKnoten;
        aktAnzahl = 0;
        besucht = new boolean[maxAnzahl];
        reihenfolge = new int[maxAnzahl];
        warteschlange = new int[maxAnzahl];
    }

    public void knotenHinzufuegen(KNOTEN k) {
        if(aktAnzahl < maxAnzahl) {
            knotennamen[aktAnzahl] = k;
            aktAnzahl++;
        } else {
            System.out.println("Ein Einfügen ist leider nicht möglich.");
        }
    }
     
    public void kanteHinzufuegen(int start, int ende, int distanz) {
        if (start < 0 || start >= maxAnzahl || ende < 0 || ende >= maxAnzahl) {
            System.out.println("Ein Einfügen ist leider nicht möglich.");
        } else {
            adja[start][ende]=distanz;
            adja[ende][start]=distanz;
        }        
    }
    
    private void warteschlangeVorbereiten() {
    //Alle Plätze der Warteschlange werden mit -1 initalisiert, um zu kennzeichnen, dass sie nicht benutzt werden.
        for(int i=0; i<warteschlange.length; i++) {
            warteschlange[i] = -1;
        }
    }
    
    public void breitensuche(String start, String ende) {
        System.out.println(breitensucheText(start, ende));
    }

    public String breitensucheText(String start, String ende) {
        int startNummer = knotenNummerGeben(start);
        int zielNummer = knotenNummerGeben(ende);
        if (startNummer == -1 || zielNummer == -1) {
            return "Start- oder Zielbahnhof ist unbekannt.";
        }

        warteschlangeVorbereiten();
        Arrays.fill(besucht, false);
        int[] vorgaenger = new int[aktAnzahl];
        Arrays.fill(vorgaenger, -1);

        int kopf = 0;
        int warteschlangenEnde = 0;
        int anzahlInReihenfolge = 0;
        besucht[startNummer] = true;
        warteschlange[warteschlangenEnde++] = startNummer;

        while (kopf < warteschlangenEnde) {
            int aktuell = warteschlange[kopf++];
            reihenfolge[anzahlInReihenfolge++] = aktuell;
            if (aktuell == zielNummer) {
                break;
            }

            for (int nachbar = 0; nachbar < aktAnzahl; nachbar++) {
                if (adja[aktuell][nachbar] > 0 && !besucht[nachbar]) {
                    // Beim Einreihen markieren, damit jeder Knoten nur einmal ansteht.
                    besucht[nachbar] = true;
                    vorgaenger[nachbar] = aktuell;
                    warteschlange[warteschlangenEnde++] = nachbar;
                }
            }
        }

        StringJoiner durchlauf = new StringJoiner("  –  ");
        for (int i = 0; i < anzahlInReihenfolge; i++) {
            durchlauf.add(knotennamen[reihenfolge[i]].bezeichnerGeben());
        }
        String ergebnis = "Besuchsreihenfolge der Breitensuche\n" + durchlauf;
        if (!besucht[zielNummer]) {
            return ergebnis + "\n\nKeine Verbindung gefunden.";
        }

        // Vom Ziel über die Vorgänger zum Start zurückgehen.
        String strecke = knotennamen[zielNummer].bezeichnerGeben();
        int anzahlAbschnitte = 0;
        for (int knoten = vorgaenger[zielNummer]; knoten != -1; knoten = vorgaenger[knoten]) {
            strecke = knotennamen[knoten].bezeichnerGeben() + " → " + strecke;
            anzahlAbschnitte++;
        }
        return ergebnis + "\n\nRoute mit den wenigsten Streckenabschnitten\n" + strecke
                + "\nStreckenabschnitte: " + anzahlAbschnitte;
    }

    public void adjaAusgeben() {
        System.out.println(adjaText());
    }

    public String adjaText() {
        StringBuilder matrix = new StringBuilder("\t");
        for (int i = 0; i < aktAnzahl; i++) {
            matrix.append(knotennamen[i].kuerzelGeben()).append('\t');
        }
        matrix.append('\n');
        for (int i = 0; i < aktAnzahl; i++) {
            matrix.append(knotennamen[i].kuerzelGeben()).append('\t');
            for (int j = 0; j < aktAnzahl; j++) {
                matrix.append(adja[i][j]).append('\t');
            }
            matrix.append('\n');
        }
        return matrix.toString();
    }

    public String[] bahnhofsnamenGeben() {
        String[] namen = new String[aktAnzahl];
        for (int i = 0; i < aktAnzahl; i++) {
            namen[i] = knotennamen[i].bezeichnerGeben();
        }
        return namen;
    }

    public int knotenNummerGeben(String wert) {
        if (wert != null) {
            for (int i = 0; i < aktAnzahl; i++) {
                if (knotennamen[i].bezeichnerIstGleich(wert)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void alleKantenEntfernen() {
        for(int i=0; i<maxAnzahl;i++) {
            for(int j=0; j<maxAnzahl;j++) {
                adja[i][j]=-1;
            }
        }
    }
       
}
