package dataLayer.dataAccessObjects.file.daos;

import dataLayer.dataAccessObjects.file.services.IFilePersistenceService;
import models.Leistung;

import java.nio.file.Path;

public class LeistungDaoFile extends AbstractDaoFile<Leistung, String> {

    public LeistungDaoFile(IFilePersistenceService<Leistung> filePersistenceService, Class<Leistung> objectType, Path filePath) {
        super(filePersistenceService, objectType, filePath);
    }

    @Override
    protected String getIdFromObject(Leistung object) {
        return object.getLkNr();
    }

    @Override
    protected void setIdToObjectToInsert(Leistung objectToInsert) {
    }
}
