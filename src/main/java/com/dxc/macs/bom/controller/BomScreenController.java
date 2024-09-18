package com.dxc.macs.bom.controller;

import com.dxc.macs.bom.entity.BomStatus;
import com.dxc.macs.bom.entity.BusinessUnit;
import com.dxc.macs.bom.entity.BusinessUnitName;
import com.dxc.macs.bom.entity.MdsStatusBom;
import com.dxc.macs.bom.filter.BomFilter;
import com.dxc.macs.bom.model.*;
import com.dxc.macs.bom.service.BomScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/macs/bom-service")
public class BomScreenController {

    @Autowired
    public BomScreenService bomScreenService;

    private final BomFilter bomFilter;

    public BomScreenController(BomFilter bomFilter) {
        this.bomFilter = bomFilter;
    }

    @PostMapping("/getProducts")
    public Collection<BomResult> getProductsList(@RequestBody BomSearchCriteria bomSearch){
        return bomScreenService.getProductsList(bomSearch);
    }

    @GetMapping("/getBusinessUnits")
    public Collection<BusinessUnitName> getBusinessUnitNames(){
        return bomScreenService.getBusinessUnitNames();
    }

    @GetMapping("/getBomStatus")
    public Collection<BomStatus> getBomStatuses(){
        return bomScreenService.getBomStatuses();
    }

    @GetMapping("getMdsStatus")
    public Collection<MdsStatusBom> getMdsStatuses(){
        return bomScreenService.getMdsStatuses();
    }

    @PostMapping("/duplicateBom")
    public Message duplicateBom(@RequestBody BomProduct bomProduct, @RequestHeader("Authorization") String authorizationHeader) {
        String userName = getUserNameFromSession(authorizationHeader);
        return bomScreenService.duplicateBom(bomProduct,userName);
    }

    @GetMapping("/getBusinessUnitForUser")
    public BusinessUnit getBusinessUnitForUser(@RequestHeader("Authorization") String authorizationHeader){
        String userName = getUserNameFromSession(authorizationHeader);
        return bomScreenService.getBusinessUnitForUser(userName);
    }

    private String getUserNameFromSession(String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove the "Bearer " prefix
        return bomFilter.extractUsername(token);
    }

}
