package configuration.models;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

