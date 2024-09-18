package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.ThreeRPartnumberWeightMap;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ThreeRPartnumberWeightMapRepository extends CrudRepository<ThreeRPartnumberWeightMap,Integer> {
        List<ThreeRPartnumberWeightMap> findBypartNumber(String partNumber);
}
