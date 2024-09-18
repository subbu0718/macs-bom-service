package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.Substance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubstanceRepository extends CrudRepository<Substance, Integer> {
    List<Substance> findByNodeId(Long key);
}
