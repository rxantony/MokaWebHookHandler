package com.mekari.mokaaddons.api.service.jurnal.command.saveproduct;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.RequestHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaveJurnalProductHandler implements RequestHandler<SaveJurnalProductRequest, List<SaveJurnalProductResult>> {

    private ObjectMapper mapper;
    private static final Logger logger = LogManager.getFormatterLogger(SaveJurnalProductHandler.class);

    @Override
    public List<SaveJurnalProductResult> handle(SaveJurnalProductRequest request) throws Exception {

        var result = new ArrayList<SaveJurnalProductResult>();
        for(var product : request.getProducts()){

            var op = Strings.isBlank(product.getId()) ? "create" : "update";

            logger.debug("%s product to jurnal through http post://jurnal.id/product/%s, payload:%s", op, op, mapper.writeValueAsString(product));
            
            var id = Strings.isBlank(product.getId()) ? UUID.randomUUID().toString() : product.getId();
            var jurnalProduct = SaveJurnalProductResult.builder()
                                .id(id)
                                .name(product.getName())
                                .build();
            result.add(jurnalProduct);
        }
        return result;
    }
    
}
