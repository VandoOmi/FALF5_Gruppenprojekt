package dataLayer.services;

import configuration.models.DataSource;
import configuration.models.DataSources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import configuration.exception.ConfigurationException;
import configuration.models.Configuration;
import configuration.models.ConnectionType;
import configuration.models.DbConnection;
import configuration.models.FileConnection;
import configuration.models.ModelType;
import configuration.models.SourceType;
import dataLayer.dataAccessObjects.IDao;
import dataLayer.dataAccessObjects.db.daos.LeistungDaoSqlite;
import dataLayer.dataAccessObjects.db.daos.PatientDaoSqlite;
import dataLayer.dataAccessObjects.db.daos.PflegekraftDaoSqlite;
import dataLayer.dataAccessObjects.db.services.ConnectionManager;
import dataLayer.dataAccessObjects.db.services.ConnectionManagerSqlite;
import dataLayer.dataAccessObjects.file.daos.LeistungDaoFile;
import dataLayer.dataAccessObjects.file.daos.PatientDaoFile;
import dataLayer.dataAccessObjects.file.daos.PflegekraftDaoFile;
import dataLayer.dataAccessObjects.file.services.FilePersistenceServiceCsv;
import dataLayer.dataAccessObjects.file.services.FilePersistenceServiceXml;
import dataLayer.dataAccessObjects.file.services.XmlWrapper;
import models.Leistung;
import models.Patient;
import models.Pflegekraft;

public class DataLayerFactory {

    private DataLayerFactory() {
    }

    public static IDataLayer createDataLayer(Configuration configuration) {
        DataLayer dataLayer = new DataLayer();

        DataSource dsLeistung = getDataSource(configuration, ModelType.LEISTUNG);
        DataSource dsPatient = getDataSource(configuration, ModelType.PATIENT);
        DataSource dsPflegekraft = getDataSource(configuration, ModelType.PFLEGEKRAFT);

        dataLayer.setDaoLeistung(createDao(ModelType.LEISTUNG, dsLeistung));
        dataLayer.setDaoPatient(createDao(ModelType.PATIENT, dsPatient));
        dataLayer.setDaoPflegekraft(createDao(ModelType.PFLEGEKRAFT, dsPflegekraft));

        return dataLayer;
    }

    private static <T, ID> IDao<T, ID> createDao(ModelType modelType, DataSource dataSource) {
        if (dataSource == null) {
            throw new ConfigurationException("Kein DataSource für ModelType: " + modelType);
        }

        switch (dataSource.getSource()) {
            case DB:
                return createDbDao(modelType, dataSource);
            case FILE:
                return createFileDao(modelType, dataSource);
            default:
                throw new ConfigurationException("Unbekannter SourceType: " + dataSource.getSource());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, ID> IDao<T, ID> createDbDao(ModelType modelType, DataSource dataSource) {
        ConnectionType ct = dataSource.getType();
        String connectionString = extractConnectionString(dataSource);

        ConnectionManager connectionManager = new ConnectionManagerSqlite(connectionString);

        switch (ct) {
            case SQLITE:
                switch (modelType) {
                    case LEISTUNG:
                        return (IDao<T, ID>) new LeistungDaoSqlite((String) connectionString);
                    case PATIENT:
                        return (IDao<T, ID>) new PatientDaoSqlite((String) connectionString);
                    case PFLEGEKRAFT:
                        return (IDao<T, ID>) new PflegekraftDaoSqlite((String) connectionString);
                    default:
                        throw new ConfigurationException("Unbekannter ModelType für DB: " + modelType);
                }
            default:
                throw new ConfigurationException("Für DB wird nur SQLITE erwartet, bekommen: " + ct);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, ID> IDao<T, ID> createFileDao(ModelType modelType, DataSource dataSource) {
        ConnectionType ct = dataSource.getType();
        String filePathString = extractFilePathString(dataSource);
        Path filePath = Path.of(filePathString);

        switch (ct) {
            case CSV:
                switch (modelType) {
                    case LEISTUNG:
                        return (IDao<T, ID>) new LeistungDaoFile(
                                new FilePersistenceServiceCsv<>(','),
                                (Class<Leistung>) (Class<?>) Leistung.class,
                                filePath);
                    case PATIENT:
                        return (IDao<T, ID>) new PatientDaoFile(
                                new FilePersistenceServiceCsv<>(','),
                                (Class<Patient>) (Class<?>) Patient.class,
                                filePath);
                    case PFLEGEKRAFT:
                        return (IDao<T, ID>) new PflegekraftDaoFile(
                                new FilePersistenceServiceCsv<>(','),
                                (Class<Pflegekraft>) (Class<?>) Pflegekraft.class,
                                filePath);
                    default:
                        throw new ConfigurationException("Unbekannter ModelType für FILE CSV: " + modelType);
                }

            case XML:
                switch (modelType) {
                    case LEISTUNG:
                        return (IDao<T, ID>) new LeistungDaoFile(
                                new FilePersistenceServiceXml<>(new XmlWrapper<Leistung>()),
                                (Class<Leistung>) (Class<?>) Leistung.class,
                                filePath);
                    case PATIENT:
                        return (IDao<T, ID>) new PatientDaoFile(
                                new FilePersistenceServiceXml<>(new XmlWrapper<Patient>()),
                                (Class<Patient>) (Class<?>) Patient.class,
                                filePath);
                    case PFLEGEKRAFT:
                        return (IDao<T, ID>) new PflegekraftDaoFile(
                                new FilePersistenceServiceXml<>(new XmlWrapper<Pflegekraft>()),
                                (Class<Pflegekraft>) (Class<?>) Pflegekraft.class,
                                filePath);
                    default:
                        throw new ConfigurationException("Unbekannter ModelType für FILE XML: " + modelType);
                }

            default:
                throw new ConfigurationException("Für File wird nur XML oder CSV erwartet, bekommen: " + ct);
        }
    }

    private static DataSource getDataSource(Configuration config, ModelType modelType) {
        return config.getDataSources().getDataSourceList().getFirst();
    }

    private static String extractConnectionString(DataSource ds) {
        return ds.getType().name();
    }

    private static String extractFilePathString(DataSource ds) {
        return ds.getType().name();
    }
}