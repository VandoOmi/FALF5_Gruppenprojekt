package dataLayer.services;

import configuration.exception.ConfigurationException;
import configuration.models.Configuration;
import configuration.service.*;

public class DataLayerManager {

    private static DataLayerManager instance;
    private IDataLayer dataLayer;

    private DataLayerManager() {
        try {
            Configuration config = ConnfigurationPersistenceService.getInstance().getConfiguration();
            this.dataLayer = DataLayerFactory.createDataLayer(config);
        } catch (ConfigurationException e) {
            throw new RuntimeException("DataLayer konnte nicht initialisiert werden", e);
        }
    }

    public static synchronized DataLayerManager getInstance() {
        if (instance == null) {
            instance = new DataLayerManager();
        }
        return instance;
    }

    public IDataLayer getDataLayer() {
        return dataLayer;
    }
}
