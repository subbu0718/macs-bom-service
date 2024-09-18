package com.dxc.macs.bom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dxc.macs.bom.entity.ThreeRProduct;

public interface ThreeRProductRepository extends CrudRepository<ThreeRProduct, Integer> {

    List<ThreeRProduct> findByBomModelId(int bomModelId);

    Optional<ThreeRProduct> findByBomModelIdAndPartNumber(int bomModelId, String partNumber);
}
