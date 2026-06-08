package dataLayer.dataAccessObjects.db.services;

public class ConnectionManagerSqlite extends ConnectionManager {

    private boolean classLoaded;

    public ConnectionManagerSqlite(String url) {
        super("org.sqlite.JDBC", "jdbc:sqlite:" + url);

        try {
            Class.forName(getClassName());
            this.classLoaded = true;
        } catch (ClassNotFoundException exception) {
            this.classLoaded = false;
            throw new RuntimeException("SQLite driver could not be loaded.", exception);
        }
    }
}
