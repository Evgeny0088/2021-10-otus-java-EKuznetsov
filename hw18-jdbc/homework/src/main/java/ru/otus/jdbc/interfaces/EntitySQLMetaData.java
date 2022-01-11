package ru.otus.jdbc.interfaces;

import java.sql.SQLException;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData {
    String getSelectAllSql() throws SQLException;

    String getSelectByIdSql() throws SQLException;

    String getInsertSql() throws SQLException;

    String getUpdateSql() throws SQLException;
}
