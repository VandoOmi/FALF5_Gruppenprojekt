package configuration.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
public class DbConnections {
    private List<DbConnection> dbConnectionList;

    public List<DbConnection> getDbConnectionList() {
        return dbConnectionList;
    }

    public Map<DbConnectionType, DbConnection> createDbConnectionMap(){
        HashMap dbConnectionMap = new HashMap<DbConnectionType, DbConnection>();
        for (DbConnection dbConnection : dbConnectionList){
            dbConnectionMap.put(dbConnection.getType(), dbConnection);
        }
        return dbConnectionMap;
    }
}

