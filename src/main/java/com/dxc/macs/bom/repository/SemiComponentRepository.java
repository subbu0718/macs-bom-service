package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.SemiComponent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemiComponentRepository extends CrudRepository<SemiComponent, Integer> {
    List<SemiComponent> findByItemNo(String partNumber);

    List<SemiComponent> findByNodeIdOrderByVersionDesc(Long key);
}
