package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.Component;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends CrudRepository<Component, Integer> {

    List<Component> findByPartItemNo(String partNumber);
}
