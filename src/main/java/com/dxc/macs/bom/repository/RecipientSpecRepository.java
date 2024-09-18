package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.RecipientSpec;
import org.springframework.data.repository.CrudRepository;

public interface RecipientSpecRepository extends CrudRepository<RecipientSpec, Integer> {
    RecipientSpec findByPartNumber(String partNumber);
}
