package models;

public class Leistung {

    private String lkNr;
    private String bezeichnung;
    private String beschreibung;

    Leistung(String lkNr, String bezeichnung, String beschreibung) {
        this.lkNr = lkNr;
        this.bezeichnung = bezeichnung;
        this.beschreibung = beschreibung;
    }

    public String getLkNr() {
        return lkNr;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setLkNr(String lkNr) {
        this.lkNr = lkNr;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
    
}
