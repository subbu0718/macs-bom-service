package com.dxc.macs.bom.repository;

import org.springframework.data.repository.CrudRepository;

import com.dxc.macs.bom.entity.ProductCalculation;

public interface ProductCalculationRepository extends CrudRepository<ProductCalculation, Integer> {

    void deleteByBomModelId(int bomModelId);
}
