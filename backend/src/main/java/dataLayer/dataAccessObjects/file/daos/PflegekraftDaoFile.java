package dataLayer.dataAccessObjects.file.daos;

import dataLayer.dataAccessObjects.file.services.IFilePersistenceService;
import models.Pflegekraft;

import java.nio.file.Path;

public class PflegekraftDaoFile extends AbstractDaoFile<Pflegekraft, Long> {

    public PflegekraftDaoFile(IFilePersistenceService<Pflegekraft> filePersistenceService, Class<Pflegekraft> objectType, Path filePath) {
        super(filePersistenceService, objectType, filePath);
    }

    @Override
    protected Long getIdFromObject(Pflegekraft object) {
        return object.getId();
    }

    @Override
    protected void setIdToObjectToInsert(Pflegekraft objectToInsert) {
        long maxId = 0;
        if (cachedObjectList != null) {
            for (Pflegekraft pflegekraft : cachedObjectList) {
                if (pflegekraft.getId() > maxId) {
                    maxId = pflegekraft.getId();
                }
            }
        }
        objectToInsert.setId(maxId + 1);
    }
}
