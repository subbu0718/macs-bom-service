package com.dxc.macs.bom.repository;

import org.springframework.data.repository.CrudRepository;

import com.dxc.macs.bom.entity.ThreeRResidue;

public interface ThreeRResidueRepository extends CrudRepository<ThreeRResidue, Integer> {

    void deleteByBomModelId(int bomModelId);
}
