package com.mekari.mokaaddons.webhookconsumer.service.product.createProduct;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.handler.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CreateProductRequestHandler implements RequestHandler<CreateProductRequest, Boolean>{

    private final JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LogManager.getFormatterLogger(CreateProductRequestHandler.class);
    private static final String INSERT_ITEM_SQL = "INSERT INTO item (id, name, description, created_at, updated_at) VALUES (?,?,?,?,?)";
    
    public CreateProductRequestHandler(@Qualifier("mokaaddons") DataSource dataSource) {
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //just for testing
    @Override
    public Boolean handle(CreateProductRequest request) {
        LOGGER.info("insert new product id:%s, name:%s", request.getId(), request.getName());
        var result = jdbcTemplate.update(INSERT_ITEM_SQL, request.getId(), request.getName(), request.getDescription(),
            request.getCreatedAt(), request.getCreatedAt());
        return result == 0 ? false : true;
    }
    
}
