package com.mekari.mokaaddons.webhookconsumer.service.product.updateProduct;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.handler.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UpdateProductRequestHandler implements RequestHandler<UpdateProductRequest, Boolean>{

    private final JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LogManager.getFormatterLogger(UpdateProductRequestHandler.class);
    private static final String UPDATE_ITEM_SQL = "UPDATE item SET name=?, description=?, updated_at=? WHERE id=?";

    public UpdateProductRequestHandler(@Qualifier("mokaaddons") DataSource dataSource) {
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //just for testing
    @Override
    public Boolean handle(UpdateProductRequest request) {
        LOGGER.info("update new product id:%s, name:%s", request.getId(), request.getName());
        var result = jdbcTemplate.update(UPDATE_ITEM_SQL, request.getName(), request.getDescription(), request.getUpdatedAt(), request.getId());
        return result == 0 ? false : true;
    }
    
}