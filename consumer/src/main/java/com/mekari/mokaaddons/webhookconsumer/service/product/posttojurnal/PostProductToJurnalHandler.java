package com.mekari.mokaaddons.webhookconsumer.service.product.posttojurnal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookconsumer.model.JurnalProduct;
import com.mekari.mokaaddons.common.handler.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * sample request handler
 */
@Service
public class PostProductToJurnalHandler implements RequestHandler<PostProductToJurnalRequest, List<JurnalProduct>> {

    private @Autowired ObjectMapper mapper;
    private final static Logger LOGGER = LogManager.getFormatterLogger(PostProductToJurnalHandler.class);

    @Override
    public List<JurnalProduct> handle(PostProductToJurnalRequest request) throws Exception {

        var result = new ArrayList<JurnalProduct>();
        for(var product : request.getProducts()){

            var op = Strings.isBlank(product.getId()) ? "create" : "update";

            LOGGER.debug("%s product to jurnal through http post://jurnal.id/product/%s, payload:%s", op, op, mapper.writeValueAsString(product));
            
            var id = Strings.isBlank(product.getId()) ? UUID.randomUUID().toString() : product.getId();
            var jurnalProduct = JurnalProduct.builder()
                                .id(id)
                                .name(product.getName())
                                .build();
            result.add(jurnalProduct);
        }
        return result;
    }
    
}
