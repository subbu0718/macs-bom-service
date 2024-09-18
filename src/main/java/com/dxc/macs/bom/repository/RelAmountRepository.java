package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.RelAmount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelAmountRepository extends CrudRepository<RelAmount, Integer> {
    List<RelAmount> findByImmediateParentNodeIdOrderByVersionAsc(Long componentNode);

    List<RelAmount> findByImmediateParentNodeIdOrderBySequenceAscVersionAsc(long componentNode);
}
