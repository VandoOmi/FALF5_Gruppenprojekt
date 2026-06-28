package dataLayer.services;

import javax.sql.DataSource;

import configuration.models.Configuration;
import configuration.models.FileConnection;
import configuration.models.ModelType;
import dataLayer.dataAccessObjects.IDao;

public class DataLayerFactory {

    private static Configuration config;

    public static IDataLayer createDataLayer(Configuration configuration) {
        return null;
    }

    private static <T, ID> IDao<T, ID> createDao(ModelType modelType) {
    }

    private static <T, ID> IDao<T, ID> createDbDao(ModelType modelType, DataSource dataSource) {
    }

    private static <T, ID> IDao<T, ID> createFileDao(ModelType modelType, DataSource dataSource) {
    }

    private static DataSource getDataSource(ModelType modelType) {
    }

    private static DbConnection getDbConnection(String dbConnectionType) {
    }

    private static FileConnection getFileConnection(ModelType modelType) {
    }
}
