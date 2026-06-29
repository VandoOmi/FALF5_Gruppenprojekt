package configuration.service;

import configuration.exception.ConfigurationException;
import configuration.models.Configuration;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class ConfigurationPersistenceService {
    private static ConfigurationPersistenceService instance;
    private String filePath;
    private Configuration configuration;

    private ConfigurationPersistenceService() {
        this.filePath = "configuration.xml";
    }

    public static ConfigurationPersistenceService getInstance() {
        if (instance == null) {
            instance = new ConfigurationPersistenceService();
        }
        return instance;
    }

    public Configuration getConfiguration() throws ConfigurationException {
        if (configuration == null) {
            configuration = readFile();
        }
        return configuration;
    }

    private Configuration readFile() throws ConfigurationException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ConfigurationException("Configuration file not found: " + filePath);
        }
        try {
            JAXBContext context = JAXBContext.newInstance(Configuration.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Configuration) unmarshaller.unmarshal(file);
        } catch (JAXBException ex) {
            throw new ConfigurationException("Error reading configuration file: " + ex.getMessage());
        }
    }

    private void writeFile(Configuration configuration) throws ConfigurationException {
        try {
            JAXBContext context = JAXBContext.newInstance(Configuration.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(configuration, new File(filePath));
        } catch (JAXBException ex) {
            throw new ConfigurationException("Error writing configuration file: " + ex.getMessage());
        }
    }
}
