package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.RelWeight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelWeightRepository extends CrudRepository<RelWeight, Integer> {

    List<RelWeight> findByImmediateParentNodeIdOrderByVersionAsc(Long raNodeId);
}
