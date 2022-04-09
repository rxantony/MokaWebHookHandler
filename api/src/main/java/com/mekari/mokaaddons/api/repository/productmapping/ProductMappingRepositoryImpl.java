package com.mekari.mokaaddons.api.repository.productmapping;

import java.util.ArrayList;
import java.util.List;

import com.mekari.mokaaddons.api.entity.ProductMapping;

import org.springframework.stereotype.Repository;

import lombok.Synchronized;

/**
 * repository sample
 */
@Repository
public class ProductMappingRepositoryImpl implements ProductMappingRepository {

    private int idCounter;
    private List<ProductMapping> db = new ArrayList<>();

    @Override
    public boolean isExistsByMokaItemId(String id) {
       return db.stream()
                .filter(p-> p.getMokaId() == id)
                .findFirst().isPresent();
    }

    @Override
    @Synchronized
    public ProductMapping save(ProductMapping product) {
        product.setId(++idCounter);
        db.add(product);
        return product;
    }

    @Override
    public List<ProductMapping> save(List<ProductMapping> products) {
        var result = new ArrayList<ProductMapping>();
        products.forEach(p->{
            save(p);
            result.add(p);
        });
        return result;
    }
    
}
