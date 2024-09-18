package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.SubstanceApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubstanceApplicationRepository extends CrudRepository<SubstanceApplication, Integer> {
    List<SubstanceApplication> findBySubstanceApplicationId(Long applicationId);
}
