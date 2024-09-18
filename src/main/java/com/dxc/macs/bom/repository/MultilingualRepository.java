package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.Multilingual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultilingualRepository extends CrudRepository<Multilingual, Integer> {

    List<Multilingual> findByNodeIdAndIsoLanguageOrderByVersionDesc(Long key, String en);
}
