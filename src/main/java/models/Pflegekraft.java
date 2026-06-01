package models;

public class Pflegekraft {

    private long id;
    private String vorname;
    private String nachname;
    private String telefon;

    public Pflegekraft(long id, String vorname, String nachname, String telefon) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.telefon = telefon;
    }

    public long getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
