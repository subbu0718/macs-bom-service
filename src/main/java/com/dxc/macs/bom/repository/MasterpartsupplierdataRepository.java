package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.Masterpartsupplierdata;
import org.springframework.data.repository.CrudRepository;

public interface MasterpartsupplierdataRepository extends CrudRepository<Masterpartsupplierdata, Integer> {
    boolean existsByPartnumbercode(String partnumber);
}
