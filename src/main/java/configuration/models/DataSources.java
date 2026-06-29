package configuration.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataSources {
    private List<DataSource> dataSourceList;

    public List<DataSource> getDataSourceList() {
        return dataSourceList;
    }

    public Map<ModelType, DataSource> createDataSourceMap() {
        HashMap dataSourceMap = new HashMap<ModelType, DataSource>();
        for (DataSource dataSource : dataSourceList) {
            dataSourceMap.put(dataSource.getType(), dataSource);
        }
        return dataSourceMap;
    }
}
