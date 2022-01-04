package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.interfaces.EntityClassMetaData;
import ru.otus.jdbc.interfaces.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) throws SQLException {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            Optional<T> itemFromDB = Optional.empty();
            try {
                if (rs.next()) {
                    Object[] args = getArgsForConstructor(rs);
                    return Optional.of(entityClassMetaData.getConstructor().newInstance(args));
                }
                return itemFromDB;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public List<T> findAll (Connection connection) throws SQLException {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            List<T> objectList = new ArrayList<>();
            try {
                while (rs.next()) {
                    Object[] args = getArgsForConstructor(rs);
                    objectList.add(entityClassMetaData.getConstructor().newInstance(args));
                }
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | SQLException e) {
                throw new DataTemplateException(e);
            }
            return objectList;
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert (Connection connection, T object){
        try {
            List<Object> fieldValues = fieldsValuesFromObject(entityClassMetaData.getFieldsWithoutId(),object);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update (Connection connection, T object){
        try {
            List<Object> fieldValues = fieldsValuesFromObject(entityClassMetaData.getFieldsWithoutId(), object);
            fieldValues.add(getIdValueFromObject(entityClassMetaData,object));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    /*
    collects args for constructor of specified class
    */
    private Object[] getArgsForConstructor (ResultSet rs) throws SQLException {
        int argsCount = rs.getMetaData().getColumnCount();
        Object[] args = new Object[argsCount];
        for (int j = 0; j < argsCount; j++) {
            args[j] = rs.getObject(j + 1);
        }
        return args;
    }

    /*
    take values from object fields and save them into arrayList
     */
    private List<Object> fieldsValuesFromObject(List<Field> fields, T object) {
        return fields.stream().map(f->{
            try {
                f.setAccessible(true);
                return f.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    /*
    takes value from Id field of specified object
     */
    private Object getIdValueFromObject(EntityClassMetaData<T> entityClassMetaData, T object){
        try{
            Field id = entityClassMetaData.getIdField();
            id.setAccessible(true);
            return id.get(object);
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}