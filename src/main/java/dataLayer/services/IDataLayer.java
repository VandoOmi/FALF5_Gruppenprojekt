package dataLayer.services;

import dataLayer.dataAccessObjects.IDao;
import models.Leistung;
import models.Patient;
import models.Pflegekraft;

public interface IDataLayer {

    public IDao<Leistung, String> getDaoLeistung();

    public IDao<Patient, Long> getDaoPatient();

    public IDao<Pflegekraft, Long> getDaoPflegekraft();
}
