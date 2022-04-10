package com.mekari.mokaaddons.webhookconsumer.repository.productmapping;

import com.mekari.mokaaddons.webhookconsumer.entity.ProductMapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * repository sample
 */
@Repository
public interface ProductMappingRepository extends JpaRepository<ProductMapping, String> {
    @Query(value="SELECT CASE count(*)  WHEN 0 THEN true ELSE false END FROM product_mapping WHERE moka_item_id=?", nativeQuery=true)
    boolean isExistsByMokaItemId(String id);

    ProductMapping findByMokaItemId(String mokaItemId);
}
