package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.RelPercentage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelPercentageRepository extends CrudRepository<RelPercentage, Integer> {
    List<RelPercentage> findByImmediateParentNodeIdOrderByVersionDesc(Long nodeId);

    List<RelPercentage> findByImmediateParentNodeIdOrderByNodeIdAscVersionAsc(Long materialNodeId);
}
