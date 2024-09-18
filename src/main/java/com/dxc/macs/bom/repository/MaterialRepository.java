package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.Material;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends CrudRepository<Material, Integer> {
    List<Material> findBySupplierMaterialNo(String partNumber);

    List<Material> findByNodeIdOrderByVersionDesc(Long key);
}
