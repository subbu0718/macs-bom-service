package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.BomModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BomModelRepository extends CrudRepository<BomModel, Integer> {

    BomModel findByProductNumberAndReleaseNumber(String productNumber, String releaseNumber);
    List<BomModel> findByProductNumberOrderByIdDesc(String productNumber);

    List<BomModel> findByProductNumberAndStatusOrderByIdDesc(String productNumber, String status);
}
