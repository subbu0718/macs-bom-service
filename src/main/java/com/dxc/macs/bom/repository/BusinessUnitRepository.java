package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.BusinessUnit;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BusinessUnitRepository extends CrudRepository<BusinessUnit, Integer> {
    BusinessUnit findByUserName(String userName);

    BusinessUnit findById(String userName);

    Optional<BusinessUnit> findByUserNameAndBusinessUnitName(String userName, int businessUnitId);

    Optional<BusinessUnit> findByUserNameAndId(String userName, int businessUnitId);
}
