package com.mekari.mokaaddons.api.persistence.repository;

import com.mekari.mokaaddons.api.persistence.entity.ProductMapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductMappingRepository extends JpaRepository<ProductMapping, String> {
    
    @Query(value = "SELECT CASE WHEN COUNT(*) = 0 THEN 'false' ELSE 'true' END FROM product_mapping WHERE moka_item_id=?", nativeQuery = true)
    boolean isExistsByMokaItemId(String id);

    ProductMapping findByMokaItemId(String mokaItemId);
}
