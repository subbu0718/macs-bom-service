package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.BomMaterial;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BomMaterialRepository extends CrudRepository<BomMaterial, Integer> {
    List<BomMaterial> findByRaFlag(String yes);

    List<BomMaterial> findByProductNumberAndReleaseNumberAndPartNumber(String productNumber, String releaseNumber, String partNumber);

    void deleteByProductNumberAndReleaseNumberAndPartNumber(String productNumber, String releaseNumber, String partNumber);
}
