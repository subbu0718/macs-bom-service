package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.Bom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BomRepository extends CrudRepository<Bom, Integer> {

    List<Bom> findByProductNumberAndReleaseNumber(String productNumber, String releaseNumber);

    List<Bom> findByProductNumberAndReleaseNumberAndPartNumber(String productNumber, String releaseNumber, String partNumber);

    List<Bom> findByPartNumber(String partNumber);
}
