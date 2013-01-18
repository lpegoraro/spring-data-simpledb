package org.springframework.data.simpledb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.simpledb.annotation.AnnotationParser;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformation;

import java.io.Serializable;
import java.util.List;

/**
 *
 */

public class SimpleDbOperationsImpl<T, ID extends Serializable> implements SimpleDbOperations {


    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDbOperationsImpl.class);
    private final String accessID;
    private final String secretKey;
    private final DDL ddl;

    public enum DDL{
        drop_create,
        update,
        nothing
    }

    public SimpleDbOperationsImpl(String accessID, String secretKey) {
        this(accessID, secretKey, DDL.nothing.name());
    }

    public SimpleDbOperationsImpl(String accessID, String secretKey, String ddl) {
        this.accessID = accessID;
        this.secretKey = secretKey;
        this.ddl = DDL.valueOf(ddl);
    }

    public String getAccessID() {
        return accessID;
    }

    public String getSecretKey() {
        return secretKey;
    }

    @Override
    public void delete(SimpleDbEntity sdbEntity) {
        logOperation("Delete", sdbEntity);
    }

    @Override
    public Object addItem(SimpleDbEntity entity) {
        logOperation("Add  ", entity);
        return entity.getItem();
    }

    @Override
    public Object updateItem(SimpleDbEntity entity) {
        logOperation("Update", entity);
        return entity.getItem();
    }

    @Override
    public Object findOne(SimpleDbEntityInformation entityInformation, Serializable id) {
        LOGGER.info("FindOne ItemName \"{}\"\"", id);
        return null;
    }

    @Override
    public boolean exists(SimpleDbEntityInformation entityInformation, Serializable id) {
        LOGGER.info("Exists ItemName \"{}\"\"", id);
        return false;
    }

    @Override
    public List findAll(SimpleDbEntityInformation entityInformation, Iterable ids) {
        LOGGER.info("Find All Domain \"{}\"\"", entityInformation.getDomain());
        return null;
    }

    private void logOperation(String operation, SimpleDbEntity<T, ID> entity) {
        LOGGER.info(operation + " \"{}\" ItemName \"{}\"\"", entity.getDomain(), entity.getItemName());
    }

}