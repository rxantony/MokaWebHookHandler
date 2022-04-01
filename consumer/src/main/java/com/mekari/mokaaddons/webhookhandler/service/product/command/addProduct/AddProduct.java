package com.mekari.mokaaddons.webhookhandler.service.product.command.addProduct;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.handler.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AddProduct implements RequestHandler<AddProductRequest, Boolean>{

    private final JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LogManager.getFormatterLogger(AddProduct.class);
    private static final String INSERT_ITEM_SQL = "INSERT INTO item (id, name, description, created_at, updated_at) VALUES (?,?,?,?,?)";
    
    public AddProduct(@Qualifier("mokaaddons") DataSource dataSource) {
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //just for testing
    @Override
    public Boolean handle(AddProductRequest request) {
        LOGGER.info("insert new product id:%s, name:%s", request.getId(), request.getName());
        var result = jdbcTemplate.update(INSERT_ITEM_SQL, request.getId(), request.getName(), request.getDescription(),
            request.getCreatedAt(), request.getCreatedAt());
        return result == 0 ? false : true;
    }
    
}
