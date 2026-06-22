package configuration.models;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileConnection {
    private ModelType model;
    private List<File> fileList;

    public ModelType getModel() {
        return model;
    }

    public void setModel(ModelType model) {
        this.model = model;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public Map<FileType, File> createFileMap() {
        HashMap fileMap = new HashMap<FileType, File>();
        for (File file : fileList) {
            fileMap.put(file.getFileType(), file);
        }
        return fileMap;
    }
}
