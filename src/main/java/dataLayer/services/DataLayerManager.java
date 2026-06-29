package dataLayer.services;

import configuration.models.ModelType;
import configuration.models.DataSource;
import configuration.models.Configuration;
import configuration.service.ConnfigurationPersistenceService;

public class DataLayerManager {

    private static DataLayerManager instance;

    private final Configuration config;
    private IDataLayer dataLayer;

    private DataLayerManager(Configuration config) {
        this.config = config;
    }

    public static synchronized DataLayerManager getInstance() {
        if (instance == null) {
            ConnfigurationPersistenceService cps = ConfigurationPersistenceService.getInstance();
            instance = new DataLayerManager(cps.getConfiguration());
        }
        return instance;
    }

    public IDataLayer getDataLayer() {
        if (dataLayer == null) {
            dataLayer = DataLayerFactory.createDataLayer(config);
        }
        return dataLayer;
    }
}