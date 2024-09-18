package com.dxc.macs.bom.controller;

import com.dxc.macs.bom.constants.BomConstants;
import com.dxc.macs.bom.entity.BomModel;
import com.dxc.macs.bom.entity.BusinessUnit;
import com.dxc.macs.bom.entity.plant;
import com.dxc.macs.bom.filter.BomFilter;
import com.dxc.macs.bom.model.InputBomModel;
import com.dxc.macs.bom.model.Message;
import com.dxc.macs.bom.model.NewBomModel;
import com.dxc.macs.bom.service.BomUploadService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/macs/bom-service")
@Slf4j
public class BomUploadController {

    private final Logger logger = LoggerFactory.getLogger(BomUploadController.class);
    private final BomFilter bomFilter;
    @Autowired
    private BomUploadService bomUploadService;

    public BomUploadController(BomFilter bomFilter) {
        this.bomFilter = bomFilter;
    }

    @GetMapping("/getplant")
    public List<plant> getplant(){

        return bomUploadService.getplant();
    }

    @PostMapping("/uploadBom")
    public Message uploadBom(@ModelAttribute InputBomModel model, @RequestHeader("Authorization") String authorizationHeader){
        logger.info("Uploaded bom model");
        logger.info("Product number = {}", model.getProductNumber());
        String userName = getUserNameFromSession(authorizationHeader);
        Message message = new Message();
        BusinessUnit businessUnit = bomUploadService.getBusinessUnit(userName);
        logger.info("Logged in user name = {}", userName);
        logger.info("Logged in user business unit = {}", businessUnit.getBusinessUnitName());
        try {
            message = bomUploadService.uploadBom(model, businessUnit.getId(),userName);
        } catch (Exception e) {
            logger.error("Exception while uploading new bom {}", e.getMessage());
            message.setCode(BomConstants.NUMBER_1001);
            message.setInformation("BAD_REQUEST");
        }
        return message;
    }

    private String getUserNameFromSession(String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove the "Bearer " prefix
        return bomFilter.extractUsername(token);
    }

    @GetMapping("/getBomModel/{productNumber}")
    public InputBomModel getBomModel(@PathVariable String productNumber, @RequestHeader("Authorization") String authorizationHeader){
        logger.info("get bom model");
        logger.info("Product number = {}", productNumber);
        String userName = getUserNameFromSession(authorizationHeader);
        BusinessUnit businessUnit = bomUploadService.getBusinessUnit(userName);
        return bomUploadService.getModelDetails(productNumber, businessUnit.getId(), userName);
    }

    @PostMapping("/updateBomModel")
    public NewBomModel updateBomModel(@RequestBody InputBomModel bomModel, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("get username");
        String userName = getUserNameFromSession(authorizationHeader);
        logger.info("Updating bom model for a product number {} and release number {}", bomModel.getProductNumber(), bomModel.getReleaseNumber());
        return bomUploadService.updateBomModel(bomModel,userName);
    }
}
