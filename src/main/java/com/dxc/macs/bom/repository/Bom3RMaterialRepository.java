package com.dxc.macs.bom.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dxc.macs.bom.entity.Bom3RMaterial;

public interface Bom3RMaterialRepository extends CrudRepository<Bom3RMaterial, Integer> {

    List<Bom3RMaterial> findByBomModelId(int bomModelId);
}
