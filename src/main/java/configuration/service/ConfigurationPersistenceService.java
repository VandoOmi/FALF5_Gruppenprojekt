package configuration.service;

import configuration.models.Configuration;
import configuration.exception.ConfigurationException;

public class ConfigurationPersistenceService {
    private static ConfigurationPersistenceService instance;
    private String filePath;
    private Configuration configuration;

    private ConfigurationPersistenceService() {

    }

    public static ConfigurationPersistenceService getInstance(){
        return instance;
    }

    public Configuration getConfiguration() throws ConfigurationException{
        return configuration;
    }

    private Configuration readFile() throws ConfigurationException{
        return null;
    }
    private void writeFile(Configuration configuration) throws ConfigurationException{

    }
}
