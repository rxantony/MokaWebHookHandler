package com.mekari.mokaaddons.api.service.product.createtojurnal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.model.JurnalProduct;
import com.mekari.mokaaddons.common.handler.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * sample request handler
 */
@Service
public class CreateProductToJurnalHandler implements RequestHandler<CreateProductToJurnalRequest, List<JurnalProduct>> {

    private @Autowired ObjectMapper mapper;
    private final static Logger LOGGER = LogManager.getFormatterLogger(CreateProductToJurnalHandler.class);

    @Override
    public List<JurnalProduct> handle(CreateProductToJurnalRequest request) throws Exception {

        var result = new ArrayList<JurnalProduct>();
        for(var newProduct : request.getProducts()){
            LOGGER.debug("create product to jurnal through http post://jurnal.id/product/create, payload:%s", mapper.writeValueAsString(newProduct));
            var jurnalProduct = JurnalProduct.builder()
                                .id(UUID.randomUUID().toString())
                                .name(newProduct.getName())
                                .build();
            result.add(jurnalProduct);
        }
        return result;
    }
    
}
