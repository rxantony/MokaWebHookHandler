package com.mekari.mokaaddons.webhookconsumer.service.jurnal.command.saveproduct;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.RequestHandler;

import lombok.RequiredArgsConstructor;

/**
 * sample request handler
 */
@Service
@RequiredArgsConstructor
public class SaveJurnalproductHandler implements RequestHandler<SaveJurnalProductRequest, SaveJurnalProductResult> {

    private final ObjectMapper mapper;
    private static final Logger logger = LogManager.getFormatterLogger(SaveJurnalproductHandler.class);

    @Override
    public SaveJurnalProductResult handle(SaveJurnalProductRequest request) throws Exception {

        var resultBuilder = SaveJurnalProductResult.builder();
        for(var product : request.getProducts()){
            var op = Strings.isBlank(product.getId()) ? "create" : "update";

            var json = mapper.writeValueAsString(product);
            logger.debug("%s product to jurnal through http post://jurnal.id/product/%s, payload:%s", op, op, json);
            
            var id = Strings.isBlank(product.getId()) ? UUID.randomUUID().toString() : product.getId();
            var jurnalProduct = SaveJurnalProductResult.JurnalProduct.builder()
                                .id(id)
                                .name(product.getName())
                                .build();
            resultBuilder.product(jurnalProduct);
        }
        return resultBuilder.build();
    }
    
}
