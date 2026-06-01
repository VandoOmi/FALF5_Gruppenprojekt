package models;

import java.time.LocalDate;

public class Patient {

    private long id;
    private String vorname;
    private String nachname;
    private LocalDate geburtsdatum;
    private int pflegegrad;
    private String zimmer;
    private double vermoegen;

    Patient(long id, String vorname, String nachname, LocalDate geburtsdatum, int pflegegrad, String zimmer, double vermoegen) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.pflegegrad = pflegegrad;
        this.zimmer = zimmer;
        this.vermoegen = vermoegen;
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

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public int getPflegegrad() {
        return pflegegrad;
    }

    public String getZimmer() {
        return zimmer;
    }

    public double getVermoegen() {
        return vermoegen;
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

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public void setPflegegrad(int pflegegrad) {
        this.pflegegrad = pflegegrad;
    }

    public void setZimmer(String zimmer) {
        this.zimmer = zimmer;
    }

    public void setVermoegen(double vermoegen) {
        this.vermoegen = vermoegen;
    }
}
