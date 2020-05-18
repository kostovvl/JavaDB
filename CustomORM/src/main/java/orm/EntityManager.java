package orm;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EntityManager<E> implements DbContext<E> {
    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field primary = this.getID(entity.getClass());
        primary.setAccessible(true);
        Object value = primary.get(entity);

        if (value == null || (int)value <= 0) {
            return this.doInsert(entity.getClass(), primary);
        }
        return this.doUpdate(entity.getClass(), primary);
    }

    public Iterable<E> find(Class<E> table) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return this.find(table, null);
    }

    public Iterable<E> find(Class<E> table, String where) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + table.getSimpleName()
                + "WHERE 1 " + (where.trim().isEmpty() ? "" : "AND " + where);
        ResultSet resultSet = statement.executeQuery(query);
        List<E> entities = new ArrayList<>();
        while (resultSet.next()) {
            E entity = table.getDeclaredConstructor().newInstance();
            resultSet.next();
            this.fillEntitty(table, resultSet, entity);
            entities.add(entity);
        }
        return entities;
    }

    public E findFirst(Class<E> table) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return this.findFirst(table, null);
    }

    public E findFirst(Class<E> table, String where) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + table.getSimpleName()
                + "WHERE 1 " + (where.trim().isEmpty() ? "" : "AND " + where);
        ResultSet resultSet = statement.executeQuery(query);
        E entity = table.getDeclaredConstructor().newInstance();
        resultSet.next();
        this.fillEntitty(table, resultSet, entity);
        return entity;
    }

    private Field getID(Class entity) {
        return Arrays.stream(entity.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity does not have PK"));
    }

    private boolean doInsert(Class entity, Field primary) throws IllegalAccessException, SQLException {
        String tableName = this.getTableName(entity);
        String query = "INSERT INTO " + tableName + " ";
        String columns = "(";
        String values = "(";

        Field[] fields = entity.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Id.class)) {
                columns += "`" + this.getColumnName(field) + "`, ";
                Object value = field.get(entity);

                if (value instanceof Date) {
                    values += "'" + new SimpleDateFormat("yyyy-MM-dd").format(value) ;
                } else if (value instanceof Integer) {
                    values += value;
                } else if (value instanceof String) {
                    values += "'" + value + "'";
                }
            }
             if (i < fields.length - 1) {
                 columns += ", ";
                 values += ", ";
             }
        }
        query += columns + ") VALUES " + values + ")";
        return connection.prepareStatement(query).execute();
    }

    private boolean doUpdate(Class entity, Field primary) throws IllegalAccessException, SQLException {
        String query = "UPDATE " + this.getTableName(entity.getClass()) + " SET";
        String columnAndValue = "";
        String where = "";
        Field[] fields = entity.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            Object value = field.get(entity);

            if (field.isAnnotationPresent(Id.class)) {
                where += getColumnName(field) + " + " + value;
            } else {
                if (value instanceof Date) {
                    columnAndValue += this.getColumnName(field) + " = '" +
                             new SimpleDateFormat("yyyy-MM-dd").format(value) + "'";
                } else if (value instanceof Integer) {
                    columnAndValue += this.getColumnName(field) + " = " +
                          value ;
                } else if (value instanceof String) {
                    columnAndValue += this.getColumnName(field) + " = '" +
                            value + "'";
                }
            }
            if (i < fields.length - 1) {
                columnAndValue += ", ";
            }
        }
        return connection.prepareStatement(query).execute();
    }

    private String getTableName (Class entity){
        String tablename = ((Entity)entity.getAnnotation(Entity.class)).name();

        if (tablename.trim().isEmpty()) {
            tablename = entity.getSimpleName();
        }
        return tablename;
    }

    private String getColumnName(Field field) {
        String columnName = field.getAnnotation(Column.class).name();

        if (columnName.trim().isEmpty()) {
            columnName = field.getName();
        }
        return columnName;
    }

    private void fillEntitty(Class<E> table, ResultSet resultSet, E entiry) throws SQLException, IllegalAccessException {
        Field[] fields = table.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            this.fillField(field, entiry, resultSet, this.getColumnName(field));
        }

    }

    private void fillField(Field field, Object instance, ResultSet resultset, String fieldName) throws SQLException, IllegalAccessException {
        field.setAccessible(true);
        if (field.getType() == Integer.class) {
            field.set(instance, resultset.getInt(fieldName));
        } else if (field.getType() == String.class) {
            field.set(instance, resultset.getString(fieldName));
        } else if (field.getType() == Date.class) {
            field.set(instance, resultset.getDate(fieldName));
        }
    }
}
