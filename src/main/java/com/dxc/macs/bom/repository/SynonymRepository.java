package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.entity.Synonym;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;

public interface SynonymRepository extends CrudRepository<Synonym, Integer> {

    List<Synonym> findBySubstanceNodeId(Long substanceNodeId);

    List<Synonym> findBySubstanceNodeIdAndSynonymIdAndIsoLanguage(Long substanceNodeId, int i, String lang);
}
