package dataLayer.dataAccessObjects.file.services;

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

                    Constructor<T> constructor = classType.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    T entity = constructor.newInstance();

                    for (int i = 0; i < columnNames.length && i < values.length; i++) {
                        Field field = classType.getDeclaredField(columnNames[i]);
                        field.setAccessible(true);
                        String val = values[i];
                        Class<?> type = field.getType();
                        Object converted;

                        if (val == null || val.isEmpty()) {
                            if (type == int.class) {
                                converted = 0;
                            } else if (type == long.class) {
                                converted = 0L;
                            } else if (type == double.class) {
                                converted = 0.0;
                            } else if (type == boolean.class) {
                                converted = false;
                            } else {
                                converted = null;
                            }
                        } else if (type == String.class) {
                            converted = val;
                        } else if (type == int.class || type == Integer.class) {
                            converted = Integer.parseInt(val);
                        } else if (type == long.class || type == Long.class) {
                            converted = Long.parseLong(val);
                        } else if (type == double.class || type == Double.class) {
                            converted = Double.parseDouble(val);
                        } else if (type == boolean.class || type == Boolean.class) {
                            converted = Boolean.parseBoolean(val);
                        } else if (type == LocalDate.class) {
                            converted = LocalDate.parse(val);
                        } else {
                            converted = val;
                        }

                        field.set(entity, converted);
                    }

                    result.add(entity);
                }
            }
        } catch (IOException ex) {
            throw new DaoException("Error while reading CSV file: " + ex.getMessage());
        } catch (ReflectiveOperationException ex) {
            throw new DaoException("Error while creating entity from CSV: " + ex.getMessage());
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
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new DaoException("Error while writing CSV file: " + ex.getMessage());
        } catch (ReflectiveOperationException ex) {
            throw new DaoException("Error while converting entity to CSV: " + ex.getMessage());
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
}
