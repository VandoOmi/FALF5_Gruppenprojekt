package dataLayer.services;

import javax.sql.DataSource;

import configuration.models.Configuration;
import dataLayer.dataAccessObjects.IDao;

public class DataLayerFactory {

    private Configuration config;

    public IDataLayer createDataLayer(Configuration configuration) {
        return null;
    }

    private IDao<T, ID> createDao(ModelType modelType) {
    }

    private IDao<T, ID> createDbDao(ModelType modelType, DataSource dataSource) {
    }

    private IDao<T, ID> createFileDao(ModelType modelType, DataSource dataSource) {
    }

    private DataSource getDataSource(ModelType modelType) {
    }

    private DbConnection getDbConnection(String dbConnectionType) {
    }

    private FileConnection getFileConnection(ModelType modelType) {
    }
}
