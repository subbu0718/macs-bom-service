package com.dxc.macs.bom.controller;

import com.dxc.macs.bom.entity.MdsStatusBom;
import com.dxc.macs.bom.entity.MdsType;
import com.dxc.macs.bom.model.*;
import com.dxc.macs.bom.service.BomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/macs/bom-service")
public class BomDetailController {

    @Autowired
    public BomDetailService bomDetailService;

    @GetMapping("/getMdsType")
    public Collection<MdsType> getMdsType(){
        return bomDetailService.getMdsType();
    }

    @PostMapping("/getPartNumbers")
    public List<BomPartNumber> getPartNumbers(@RequestBody BomDetailSearchCriteria bomSearch){
        return bomDetailService.findAllPartNumbers(bomSearch);
    }

    @PostMapping("/getMdsDetails")
    public List<BomMdsData> getMdsDetails(@RequestBody BomDetailInput bomDetailInput){
        return bomDetailService.getMdsDetailsForPartNumber(bomDetailInput);
    }

    @PostMapping("/getMaterialList")
    public List<BomMaterialData> getMaterialList(@RequestBody BomDetailInput bomDetailInput) {
//        return bomDetailService.getMaterialListForPartNumber(bomDetailInput);
        return bomDetailService.getMaterialList(bomDetailInput);
    }

    @PostMapping("/getSubstanceList")
    public List<BomSubstanceData> getSubstanceList(@RequestBody BomDetailInput bomDetailInput){
//        return bomDetailService.getSubstanceListForMaterial(bomDetailInput);
        return bomDetailService.getSubstanceList(bomDetailInput);
    }

    @PostMapping("/updateBomDetailPartNumber")
    public Message updateBomPartNumber(@RequestBody BomPartNumber bomPartNumber){
        return bomDetailService.updateBomPartNumber(bomPartNumber);
    }

    @GetMapping("/deleteBomDetailPartNumber/{id}/{partNumber}")
    public Message deleteBomPartNumber(@PathVariable int id, @PathVariable String partNumber){
        return bomDetailService.deleteBomPartNumber(id, partNumber);
    }

    @PostMapping("/addBomDetailPartNumber")
    public Message addBomPartNumber(@RequestBody BomPartNumber bomPartNumber){
        return bomDetailService.addBomPartNumber(bomPartNumber);
    }

    @GetMapping("/getBomDetailMdsStatus")
    public Collection<MdsStatusBom> getBomDetailMdsStatus(){
        return bomDetailService.getBomDetailMdsStatus();
    }
}
