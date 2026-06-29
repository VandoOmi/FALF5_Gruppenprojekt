package configuration.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
public class FileConnections {
    private ArrayList<FileConnection> fileConnectionList;

    public ArrayList<FileConnection> getFileConnectionList() {
        return fileConnectionList;
    }

    public Map createFileConnectionMap() {
        HashMap connectionMap = new HashMap<ModelType, FileConnection>();
        for (FileConnection connection : fileConnectionList) {
            connectionMap.put(connection.getModel(), connection);
        }
        return connectionMap;
    }
}
