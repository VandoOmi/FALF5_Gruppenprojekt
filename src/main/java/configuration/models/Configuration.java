package configuration.models;

public class Configuration {
    private Connections connections;
    private DataSources dataSources;

    public Connections getConnections(){
        return connections;
    }

    public void setConnections(Connections connections){
        this.connections = connections;
    }

    public DataSources getDataSources(){
        return dataSources;
    }

    public void setDataSources(DataSources dataSources){
        this.dataSources = dataSources;
    }
}
