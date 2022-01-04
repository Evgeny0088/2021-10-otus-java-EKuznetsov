package ru.otus.jdbc.mapper;

import ru.otus.jdbc.interfaces.EntityClassMetaData;
import ru.otus.jdbc.interfaces.EntitySQLMetaData;
import ru.otus.jdbc.interfaces.SQLRecervedWords;

import java.sql.SQLException;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() throws SQLException {
        if (!entityClassMetaData.getAllFields().isEmpty()){
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(SQLRecervedWords.SELECT);

            entityClassMetaData.getAllFields().forEach(f->
                    sqlBuilder.append(f.getName()).append(", "));

            // remove comma after last parameter!
            commaRemovalAfterLastField(sqlBuilder);

            sqlBuilder
                    .append(SQLRecervedWords.FROM)
                    .append(entityClassMetaData.getName());

            return sqlBuilder.toString();
        }else {
            throw new SQLException("failed to build sql query!");
        }
    }

    @Override
    public String getSelectByIdSql() throws SQLException {
        if (!entityClassMetaData.getAllFields().isEmpty()){
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(SQLRecervedWords.SELECT);

            entityClassMetaData.getAllFields().forEach(f->
                    sqlBuilder.append(f.getName()).append(", "));

            // remove comma after last parameter!
            commaRemovalAfterLastField(sqlBuilder);

            sqlBuilder
                    .append(SQLRecervedWords.FROM)
                    .append(entityClassMetaData.getName()).append(" ")
                    .append(SQLRecervedWords.WHERE)
                    .append(entityClassMetaData.getIdField().getName()).append("=?");

            return sqlBuilder.toString();
        }else {
            throw new SQLException("failed to build sql query!");
        }
    }

    @Override
    public String getInsertSql() throws SQLException {
        if (!entityClassMetaData.getAllFields().isEmpty()){
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder
                    .append(SQLRecervedWords.INSERT_INTO)
                    .append(entityClassMetaData.getName())
                    .append("(");

            entityClassMetaData.getFieldsWithoutId().forEach(f->sqlBuilder.append(f.getName()).append(", "));

            // remove comma after last parameter!
            commaRemovalAfterLastField(sqlBuilder);
            sqlBuilder.append(") ");

            sqlBuilder.append(SQLRecervedWords.VALUES);

            // inserts placeholders (?,? ...) for required class parameters
            fieldPlaceholders(entityClassMetaData.getFieldsWithoutId().size(),sqlBuilder);

            return sqlBuilder.toString();
        }else {
            throw new SQLException("failed to build sql query!");
        }
    }

    @Override
    public String getUpdateSql() throws SQLException {
        if (!entityClassMetaData.getAllFields().isEmpty()){
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder
                    .append(SQLRecervedWords.UPDATE)
                    .append(entityClassMetaData.getName()).append(" ")
                    .append(SQLRecervedWords.SET);

            entityClassMetaData.getFieldsWithoutId().forEach(f->sqlBuilder.append(f.getName()).append("=?, "));

            // remove comma after last parameter!
            commaRemovalAfterLastField(sqlBuilder);

            sqlBuilder
                    .append(SQLRecervedWords.WHERE)
                    .append(entityClassMetaData.getIdField().getName()).append("=?");

            return sqlBuilder.toString();
        }else {
            throw new SQLException("failed to build sql query!");
        }
    }

    private void commaRemovalAfterLastField(StringBuilder sqlBuilder){
        sqlBuilder.replace(sqlBuilder.length()-2,sqlBuilder.length()-1,"");
    }

    private void fieldPlaceholders(int fieldsCount,StringBuilder sqlBuilder){
        sqlBuilder.append("(");

        sqlBuilder.append("?, ".repeat(Math.max(0, fieldsCount)));
        commaRemovalAfterLastField(sqlBuilder);

        sqlBuilder.append(")");
    }
}
