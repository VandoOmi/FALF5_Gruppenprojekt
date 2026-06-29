package configuration.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
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
