package com.mekari.mokaaddons.webhookconsumer.service.product.command.save;

import java.util.UUID;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.webhookconsumer.persistence.entity.ProductMapping;
import com.mekari.mokaaddons.webhookconsumer.persistence.repository.ProductMappingRepository;
import com.mekari.mokaaddons.webhookconsumer.service.jurnal.command.saveproduct.SaveJurnalProductRequest;

import lombok.RequiredArgsConstructor;

/**
 * sample request handler
 */
@Service
@RequiredArgsConstructor
public class SaveProductHandler extends AbstractVoidRequestHandler<SaveProductRequest> {

    private final ProductMappingRepository productRepository;
    private final RequestHandlerManager handlerManager;

    @Override
    protected void handleInternal(SaveProductRequest request) throws Exception {
        if(request.getProducts().isEmpty()){
            return;
        }

        //map each request.Products with a match ProductMapping
        var productsMapped = request.getProducts().stream()
                                .map(p-> Pair.with(p, productRepository.findByMokaItemId(p.getMokaItemId())))
                                .collect(Collectors.toList());

        var saveJurnalProductRequestBuilder = SaveJurnalProductRequest.builder();

        /**
         * supply PostProductToJurnalRequest.Products with productsMapped item, 
         * set id of PostProductToJurnalRequest.JurnalProduct with null if no matched ProductMapping
         * to indicate Insert or Update operation.
         */
        productsMapped.forEach(p-> saveJurnalProductRequestBuilder.product(SaveJurnalProductRequest.JurnalProduct.builder()
            .id(p.getValue1() == null ? null : p.getValue1().getJurnalId())
            .name(p.getValue0().getName())
            .build())
        );

        //send request to handler manager for handler routing and handling. 
        var saveJurnalProductResult = handlerManager.handle(saveJurnalProductRequestBuilder.build());

        //classify jurnalProducts for ProductMapping creating and updating 
        var productMappings = saveJurnalProductResult.getProducts().stream().map(jp->{
            var productMapped = productsMapped.stream()
                                    .filter(p->p.getValue0().getName().equals(jp.getName()))
                                    .findFirst()
                                    .get();

            var now = DateUtil.utcNow();

            //ProductMapping for creating 
            if(productMapped.getValue1() == null){
                return ProductMapping.builder()
                        .id(UUID.randomUUID().toString())
                        .mokaItemId(productMapped.getValue0().getMokaItemId())
                        .jurnalId(jp.getId())
                        .name(productMapped.getValue0().getName())
                        .createdAt(now)
                        .updatedAt(now)
                        .build();
            }

            //ProductMapping for updating
            var productMapping = productMapped.getValue1();
            productMapping.setName(productMapped.getValue0().getName());
            productMapping.setUpdatedAt(now);
            return productMapping;

        }).collect(Collectors.toList());

        //save all ProductMappings  throuh ProductRepository
        productRepository.saveAll(productMappings);
    }
}
