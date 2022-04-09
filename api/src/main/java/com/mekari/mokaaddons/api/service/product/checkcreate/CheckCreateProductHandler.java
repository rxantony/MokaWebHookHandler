package com.mekari.mokaaddons.api.service.product.checkcreate;

import java.util.stream.Collectors;

import com.mekari.mokaaddons.api.entity.ProductMapping;
import com.mekari.mokaaddons.api.repository.productmapping.ProductMappingRepository;
import com.mekari.mokaaddons.api.service.product.createtojurnal.CreateProductToJurnalRequest;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * sample request handler
 */
@Service
public class CheckCreateProductHandler extends AbstractVoidRequestHandler<CheckCreateProductRequest> {

    private @Autowired ProductMappingRepository productRepository;
    private @Autowired RequestHandlerManager requestManager;

    @Override
    protected void handleInternal(CheckCreateProductRequest request) throws Exception {
        if(request.getProducts().size() == 0) 
            return;

        var notExistsProducts = request.getProducts().stream()
                                .filter(p-> !productRepository.isExistsByMokaItemId(p.getMokaId()))
                                .collect(Collectors.toList());

        if(notExistsProducts.size() == 0) 
            return ;

        var createProductRequestBuilder = CreateProductToJurnalRequest.builder();
        notExistsProducts.forEach(p->{
            logger.debug("create new jurnal product for mokaItemId:%s", p.getMokaId());
            createProductRequestBuilder.product(CreateProductToJurnalRequest.NewJurnalProduct.builder()
                                        .name(p.getName())
                                        .build());
        });

        var jurnalProducts = requestManager.handle(createProductRequestBuilder.build());
        var productMappings = jurnalProducts.stream().map(jp->{
            var requestProduct = request.getProducts().stream().filter(p->p.getName().equals(p.getName())).findFirst().get();
            return ProductMapping.builder()
                    .mokaId(requestProduct.getMokaId())
                    .jurnalId(jp.getId())
                    .name(requestProduct.getName())
                    .build();
        }).collect(Collectors.toList());
        productRepository.save(productMappings);
    }
}
