package configuration.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileConnections {
    private ArrayList<FileConnection> fielConnectionList;

    public ArrayList<FileConnection> getFileConnectionList() {
        return fielConnectionList;
    }

    public Map createFileConnectionMap() {
        HashMap connectionMap = new HashMap<ModelType, FileConnection>();
        for (FileConnection connection : fielConnectionList) {
            connectionMap.put(connection.getModel(), connection);
        }
        return connectionMap;
    }
}
