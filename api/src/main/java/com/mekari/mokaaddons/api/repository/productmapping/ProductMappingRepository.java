package com.mekari.mokaaddons.api.repository.productmapping;

import java.util.List;

import com.mekari.mokaaddons.api.entity.ProductMapping;

public interface ProductMappingRepository {

    boolean isExistsByMokaItemId(String id);

    ProductMapping save(ProductMapping product);

    List<ProductMapping> save(List<ProductMapping> products);
}
