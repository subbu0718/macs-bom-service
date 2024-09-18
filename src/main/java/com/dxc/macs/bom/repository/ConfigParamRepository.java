package com.dxc.macs.bom.repository;

import com.dxc.macs.bom.model.ConfigParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigParamRepository extends JpaRepository<ConfigParam, Integer> {

    Optional<ConfigParam> findByParamName(String paramName);
}
