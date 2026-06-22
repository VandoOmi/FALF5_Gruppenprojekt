package dataLayer.services;

import dataLayer.dataAccessObjects.IDao;
import models.Leistung;
import models.Patient;
import models.Pflegekraft;

public class DataLayer implements IDataLayer {

    private IDao<Leistung, String> daoLeistung;
    private IDao<Patient, Long> daoPatient;
    private IDao<Pflegekraft, Long> daoPflegekraft;

    @Override
    public IDao<Leistung, String> getDaoLeistung() {
        return daoLeistung;
    }

    @Override
    public IDao<Patient, Long> getDaoPatient() {
        return daoPatient;
    }

    @Override
    public IDao<Pflegekraft, Long> getDaoPflegekraft() {
        return daoPflegekraft;
    }

    protected void setDaoLeistung(IDao<Leistung, String> daoLeistung) {
        this.daoLeistung = daoLeistung;
    }

    protected void setDaoPatient(IDao<Patient, Long> daoPatient) {
        this.daoPatient = daoPatient;
    }

    protected void setDaoPflegekraft(IDao<Pflegekraft, Long> daoPflegekraft) {
        this.daoPflegekraft = daoPflegekraft;
    }
}
