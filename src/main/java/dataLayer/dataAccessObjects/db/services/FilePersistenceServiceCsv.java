package dataLayer.dataAccessObjects.db.services;

import dataLayer.exceptions.DaoException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilePersistenceServiceCsv<T> implements IFilePersistenceService<T> {

    private char separator;

    public FilePersistenceServiceCsv(char separator) {
        this.separator = separator;
    }

    @Override
    public List<T> readFile(Class<T> classType, Path filePath) {
        List<T> result = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return result;
        }

        String[] columnNames = getCsvColumnNames(classType);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] values = line.split(String.valueOf(separator), -1);
                    T entity = createInstance(classType, columnNames, values);
                    result.add(entity);
                }
            }
        } catch (IOException ex) {
            throw new DaoException("Error while reading CSV file: " + ex.getMessage());
        }

        return result;
    }

    @Override
    public void writeFile(Class<T> classType, List<T> listToPersist, Path filePath) {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
        } catch (IOException ex) {
            throw new DaoException("Error while creating directories for CSV file: " + ex.getMessage());
        }

        String[] columnNames = getCsvColumnNames(classType);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(String.join(String.valueOf(separator), columnNames));
            writer.newLine();

            for (T entity : listToPersist) {
                writer.write(toCsvLine(entity, columnNames));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new DaoException("Error while writing CSV file: " + ex.getMessage());
        }
    }

    private String[] getCsvColumnNames(Class<T> classType) {
        Field[] fields = classType.getDeclaredFields();
        String[] names = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            names[i] = fields[i].getName();
        }
        return names;
    }

    private T createInstance(Class<T> classType, String[] columnNames, String[] values) {
        try {
            Constructor<T> constructor = classType.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();

            for (int i = 0; i < columnNames.length && i < values.length; i++) {
                Field field = classType.getDeclaredField(columnNames[i]);
                field.setAccessible(true);
                field.set(instance, convertValue(field.getType(), values[i]));
            }

            return instance;
        } catch (ReflectiveOperationException ex) {
            throw new DaoException("Error while creating entity from CSV: " + ex.getMessage());
        }
    }

    private Object convertValue(Class<?> type, String value) {
        if (value == null || value.isEmpty()) {
            return getDefaultValue(type);
        }
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (type == LocalDate.class) {
            return LocalDate.parse(value);
        }
        return value;
    }

    private Object getDefaultValue(Class<?> type) {
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == double.class) {
            return 0.0;
        }
        if (type == boolean.class) {
            return false;
        }
        return null;
    }

    private String toCsvLine(T entity, String[] columnNames) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < columnNames.length; i++) {
                if (i > 0) {
                    sb.append(separator);
                }
                Field field = entity.getClass().getDeclaredField(columnNames[i]);
                field.setAccessible(true);
                Object value = field.get(entity);
                sb.append(value != null ? value.toString() : "");
            }
            return sb.toString();
        } catch (ReflectiveOperationException ex) {
            throw new DaoException("Error while converting entity to CSV: " + ex.getMessage());
        }
    }
}
