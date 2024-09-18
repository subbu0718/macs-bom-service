package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.MaterialClassification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MaterialClassificationRepository extends CrudRepository<MaterialClassification, Integer> {
    List<MaterialClassification> findByMaterialClassificationCode(String materialClassificationCode);
}
