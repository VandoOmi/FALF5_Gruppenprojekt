package dataLayer.dataAccessObjects.file.services;

import dataLayer.exceptions.DaoException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilePersistenceServiceXml<T> implements IFilePersistenceService<T> {

    public FilePersistenceServiceXml() {
    }

    public FilePersistenceServiceXml(XmlWrapper<T> wrapper) {
        Objects.requireNonNull(wrapper, "wrapper must not be null");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> readFile(Class<T> classType, Path filePath) {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(XmlWrapper.class, classType);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XmlWrapper<T> wrapper = (XmlWrapper<T>) unmarshaller.unmarshal(filePath.toFile());
            return wrapper.getItems() != null ? wrapper.getItems() : new ArrayList<>();
        } catch (JAXBException ex) {
            throw new DaoException("Error while reading XML file: " + ex.getMessage());
        }
    }

    @Override
    public void writeFile(Class<T> classType, List<T> listToPersist, Path filePath) {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
        } catch (IOException ex) {
            throw new DaoException("Error while creating directories for XML file: " + ex.getMessage());
        }

        try {
            JAXBContext context = JAXBContext.newInstance(XmlWrapper.class, classType);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            XmlWrapper<T> wrapper = new XmlWrapper<>(listToPersist);
            marshaller.marshal(wrapper, filePath.toFile());
        } catch (JAXBException ex) {
            throw new DaoException("Error while writing XML file: " + ex.getMessage());
        }
    }
}
