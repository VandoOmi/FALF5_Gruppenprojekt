package configuration.models;

public class Connections {
    private DbConnections dbConnections;
    private FileConnections fileConnections;


    public DbConnections getdbConnections() {
        return dbConnections;
    }

    public void setDbConnections(DbConnections dbConnections) {
        this.dbConnections = dbConnections;
    }

    public FileConnections getFileConnections() {
        return fileConnections;
    }

    public void setFileConnections(FileConnections fileConnections) {
        this.fileConnections = fileConnections;
    }
}