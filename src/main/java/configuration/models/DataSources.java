package configuration.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
