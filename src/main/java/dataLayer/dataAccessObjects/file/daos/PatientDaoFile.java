package dataLayer.dataAccessObjects.file.daos;

import dataLayer.dataAccessObjects.file.services.IFilePersistenceService;
import models.Patient;

import java.nio.file.Path;

public class PatientDaoFile extends AbstractDaoFile<Patient, Long> {

    public PatientDaoFile(IFilePersistenceService<Patient> filePersistenceService, Class<Patient> objectType, Path filePath) {
        super(filePersistenceService, objectType, filePath);
    }

    @Override
    protected Long getIdFromObject(Patient object) {
        return object.getId();
    }

    @Override
    protected void setIdToObjectToInsert(Patient objectToInsert) {
        long maxId = 0;
        if (cachedObjectList != null) {
            for (Patient patient : cachedObjectList) {
                if (patient.getId() > maxId) {
                    maxId = patient.getId();
                }
            }
        }
        objectToInsert.setId(maxId + 1);
    }
}
