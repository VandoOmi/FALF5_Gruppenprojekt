package dataLayer.services;

import configuration.models.DataSource;

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
import dataLayer.dataAccessObjects.db.services.IPersistensService;
import dataLayer.dataAccessObjects.file.daos.LeistungDaoFile;
import dataLayer.dataAccessObjects.file.daos.PatientDaoFile;
import dataLayer.dataAccessObjects.file.daos.PflegekraftDaoFile;
import models.Leistung;
import models.Patient;
import models.Pflegekraft;

public class DataLayerFactory {

    private Configuration config;

    private DataLayerFactory(Configuration configuration) {
        this.config = configuration;
    }

    public static IDataLayer createDataLayer(Configuration configuration) {
        DataLayerFactory factory = new DataLayerFactory(configuration);

        IDao<Patient, Long> patientDao = factory.createDao(ModelType.PATIENT);
        IDao<Pflegekraft, Long> pflegekraftDao = factory.createDao(ModelType.PFLEGEKRAFT);
        IDao<Leistung, String> leistungDao = factory.createDao(ModelType.LEISTUNG);

        DataLayer dataLayer = new DataLayer();
        dataLayer.setDaoPatient(patientDao);
        dataLayer.setDaoPflegekraft(pflegekraftDao);
        dataLayer.setDaoLeistung(leistungDao);

        return dataLayer;
    }

    private <T, ID> IDao<T, ID> createDao(ModelType modelType) {
        DataSource dataSource = getDataSource(modelType);

        if (dataSource.getSource() == SourceType.FILE) {
            return createFileDao(modelType, dataSource);
        } else {
            return createDbDao(modelType, dataSource);
        }
    }

    private <T, ID> IDao<T, ID> createDbDao(ModelType modelType, DataSource dataSource) {
        DbConnection dbConnection = getDbConnection(dataSource.getType());

        switch (modelType) {
            case PATIENT:
                return new PatientDaoSqlite(dbConnection.getUrl());
            case PFLEGEKRAFT:
                return new PflegekraftDaoSqlite(dbConnection.getUrl());
            case LEISTUNG:
                return new LeistungDaoSqlite(dbConnection.getUrl());
            default:
                throw new ConfigurationException("Unbekannter ModelType: " + modelType);
        }
    }

    private <T, ID> IDao<T, ID> createFileDao(ModelType modelType, DataSource dataSource) {
        FileConnection fileConnection = getFileConnection(dataSource.getType());
        IPersistensService persistenceService = new FilePersistenceServiceCsv(';');

        try {
            Path filePath = Path.of("meineDatei.txt");

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            System.out.println("Dateipfad: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (modelType) {
            case PATIENT:
                return new PatientDaoFile(persistenceService, Patient.class, filePath);
            case PFLEGEKRAFT:
                return new PflegekraftDaoFile(persistenceService, Pflegekraft.class, filePath);
            case LEISTUNG:
                return new LeistungDaoFile(persistenceService, Leistung.class, filePath);
            default:
                throw new ConfigurationException("Unbekannter ModelType: " + modelType);
        }
    }

    private DataSource getDataSource(ModelType modelType) {
        return config.getDataSources().getDataSourceList().stream()
                .filter(ds -> ds.getModel() == modelType)
                .findFirst()
                .orElseThrow(() -> new ConfigurationException(
                        "Keine DataSource für ModelType konfiguriert: " + modelType));
    }

    private DbConnection getDbConnection(ConnectionType connectionType) {
        return config.getConnections().getdbConnections()
                .getDbConnectionList().stream()
                .findFirst()
                .orElseThrow(() -> new ConfigurationException(
                        "Keine DbConnection konfiguriert für: " + connectionType));
    }

    private FileConnection getFileConnection(ConnectionType connectionType) {
        return config.getConnections().getFileConnections()
                .getFileConnectionList().stream()
                .findFirst()
                .orElseThrow(() -> new ConfigurationException(
                        "Keine FileConnection konfiguriert für: " + connectionType));
    }
}