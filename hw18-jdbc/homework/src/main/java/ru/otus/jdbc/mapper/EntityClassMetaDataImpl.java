package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;
import ru.otus.crm.model.TableName;
import ru.otus.jdbc.interfaces.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> object;

    public EntityClassMetaDataImpl(Class<T> object) {
        this.object = object;
    }

    @Override
    public Constructor<T> getConstructor() throws NoSuchMethodException {
        Class<?>[] args = new Class[object.getDeclaredFields().length];
        for (int i=0;i<args.length;i++){
            args[i] = object.getDeclaredFields()[i].getType();
        }
        return object.getDeclaredConstructor(args);
    }

    /*
    добавил аннотацию @TableName над классами которые сохраняются в дб. Она задает имя таблицы в базе
     */
    @Override
    public String getName() {
        if (object.getDeclaredAnnotation(TableName.class)!=null){
            return object.getDeclaredAnnotation(TableName.class).tableName();
        }else {
            throw new RuntimeException("table name is not specified on corresponded class!");
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(object.getDeclaredFields())
                .filter(f->f.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(object.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(object.getDeclaredFields()).filter(f -> !f.isAnnotationPresent(Id.class)).toList();
    }
}
