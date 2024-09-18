package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.ClassificationName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassificationNameRepository extends CrudRepository<ClassificationName, Integer> {
    List<ClassificationName> findByMaterialClassificationCode(String materialClassificationCode);
}
