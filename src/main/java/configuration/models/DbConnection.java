package configuration.models;

public class DbConnection {
    private DbConnectionType type;
    private String url;


    public DbConnectionType getType() {
        return type ;
    }

    public void setType(DbConnectionType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
