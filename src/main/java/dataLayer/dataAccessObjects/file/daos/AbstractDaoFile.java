package dataLayer.dataAccessObjects.file.daos;

import dataLayer.dataAccessObjects.file.services.IFilePersistenceService;
import dataLayer.exceptions.DaoException;

import java.nio.file.Path;
import java.util.List;

public abstract class AbstractDaoFile<T, ID> {

    private IFilePersistenceService<T> filePersistenceService;
    private Class<T> objectType;
    private Path filePath;
    protected List<T> cachedObjectList;

    public AbstractDaoFile(IFilePersistenceService<T> filePersistenceService, Class<T> objectType, Path filePath) {
        this.filePersistenceService = filePersistenceService;
        this.objectType = objectType;
        this.filePath = filePath;
    }

    public T create(T objectToInsert) {
        List<T> objectList = loadObjectList();
        setIdToObjectToInsert(objectToInsert);
        objectList.add(objectToInsert);
        saveObjectList(objectList);
        return objectToInsert;
    }

    public T read(ID id) {
        List<T> objectList = loadObjectList();
        for (T object : objectList) {
            if (hasMatchingId(object, id)) {
                return object;
            }
        }
        throw new DaoException("Object with id " + id + " not found.");
    }

    public List<T> read() {
        return loadObjectList();
    }

    public void update(T objectToUpdate) {
        List<T> objectList = loadObjectList();
        ID id = getIdFromObject(objectToUpdate);
        for (int i = 0; i < objectList.size(); i++) {
            if (hasMatchingId(objectList.get(i), id)) {
                objectList.set(i, objectToUpdate);
                saveObjectList(objectList);
                return;
            }
        }
        throw new DaoException("Object with id " + id + " not found.");
    }

    public void delete(ID id) {
        List<T> objectList = loadObjectList();
        for (int i = 0; i < objectList.size(); i++) {
            if (hasMatchingId(objectList.get(i), id)) {
                objectList.remove(i);
                saveObjectList(objectList);
                return;
            }
        }
        throw new DaoException("Object with id " + id + " not found.");
    }

    protected abstract ID getIdFromObject(T object);

    protected abstract void setIdToObjectToInsert(T objectToInsert);

    private boolean hasMatchingId(T object, ID id) {
        return getIdFromObject(object).equals(id);
    }

    private List<T> loadObjectList() {
        cachedObjectList = filePersistenceService.readFile(objectType, filePath);
        return cachedObjectList;
    }

    private void saveObjectList(List<T> objectList) {
        filePersistenceService.writeFile(objectType, objectList, filePath);
        cachedObjectList = objectList;
    }
}
