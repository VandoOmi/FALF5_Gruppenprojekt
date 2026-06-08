package dataLayer.dataAccessObjects.db.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConnectionManager {

    private String className;
    private String connectionString;
    private Connection existingConnection;

    public ConnectionManager(String className, String connectionString) {
        this.className = className;
        this.connectionString = connectionString;
    }

    public String getClassName() {
        return className;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public Connection getExistingConnection() {
        return existingConnection;
    }

    public Connection getNewConnection() {
        try {
            existingConnection = DriverManager.getConnection(connectionString);
            return existingConnection;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not establish database connection.", exception);
        }
    }
}