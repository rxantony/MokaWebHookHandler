package com.mekari.mokaaddons.webhookconsumer.service.product.productexists;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.handler.RequestHandler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ProductExistsRequestHandler implements RequestHandler<ProductExistsRequest, Boolean> {

    private final JdbcTemplate jdbcTemplate;
    private static final String IS_ITEM_EXISTS_SQL = "SELECT id FROM item WHERE id=?";

    public ProductExistsRequestHandler(@Qualifier("mokaaddons") DataSource dataSource) {
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // just for testing
    @Override
    public Boolean handle(ProductExistsRequest request) {
        var rs = jdbcTemplate.queryForRowSet(IS_ITEM_EXISTS_SQL, request.getId());
        return rs.next();
    }

}
