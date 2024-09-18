package com.dxc.macs.bom.service;

import com.dxc.macs.bom.model.ConfigParam;
import com.dxc.macs.bom.repository.ConfigParamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ConfigParamService {
    @Autowired
    private ConfigParamRepository configParamRepository;


    public ConfigParam getConfigValue(String configParamName) {
        if (configParamName!=null) {
            Optional<ConfigParam> configParamOptional = configParamRepository.findByParamName(configParamName);
            if (configParamOptional.isPresent()) {
                return configParamOptional.get();
            }
        }
        return new ConfigParam();
    }
}
