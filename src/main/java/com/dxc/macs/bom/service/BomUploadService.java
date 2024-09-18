package com.dxc.macs.bom.service;

import com.dxc.macs.bom.constants.BomConstants;
import com.dxc.macs.bom.entity.*;
import com.dxc.macs.bom.model.InputBomModel;
import com.dxc.macs.bom.model.Message;
import com.dxc.macs.bom.model.NewBomModel;
import com.dxc.macs.bom.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class BomUploadService {

    private final Logger logger = LoggerFactory.getLogger(BomUploadService.class);

    @Autowired
    private BomModelRepository bomModelRepository;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private ThreeRPartnumberWeightMapRepository threeRPartnumberWeightMapRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private MdsService mdsService;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private ThreeRProductRepository threeRProductRepository;

    @Autowired
    private ThreeRResidueRepository threeRResidueRepository;

    @Autowired
    private ProductCalculationRepository productCalculationRepository;

    public Message uploadBom(InputBomModel model, int businessUnitId,String userName) {
        Message message = validate(model.getModelFile());
        if(!message.getCode().contains(BomConstants.NUMBER_1001)) {
            boolean validator = updateHistoryData(model.getProductNumber(), model.getReleaseNumber());
            if (validator) {
                BomModel bomModel = new BomModel();
                bomModel.setId(model.getId());
                bomModel.setProductNumber(model.getProductNumber());
                bomModel.setMarket(model.getMarket());
                bomModel.setDescription(model.getDescription());
                bomModel.setShortDescription(model.getShortDescription());
                bomModel.setLongDescription(model.getLongDescription());
                bomModel.setReleaseNumber(model.getReleaseNumber());
                bomModel.setStatus(BomConstants.ACTIVE);
                bomModel.setBusinessUnitId(businessUnitId);
                bomModel.setTaricCode(model.getTaricCode());
                bomModel.setTaricForComponents(model.getTaricForComponents());
                bomModel.setThreeRStatus("No");
                bomModel.setSentFlag("N");
                bomModel.setCreated_by(userName);
                bomModel.setUpdated_by(userName);
                if (!model.getPlant().equals(BomConstants.EMPTY_STRING)) {
                    bomModel.setPlant(model.getPlant());
                }
                bomModelRepository.save(bomModel);
                message = importBomData(model.getModelFile(), bomModel);
            } else {
                message.setCode(BomConstants.NUMBER_1003);
                message.setInformation("BOM already present.");
            }
        }
        return  message;
    }

    private Message validate(MultipartFile modelFile) {
        Message message=new Message();
        try (XSSFWorkbook workbook = new XSSFWorkbook(modelFile.getInputStream())) {
            if(workbook.getNumberOfSheets()!=1)
            {
                message.setCode(BomConstants.NUMBER_1001);
                message.setInformation("Excel file contains multiple sheets");
                return message;
            }
            XSSFSheet worksheet = workbook.getSheetAt(0);
            XSSFRow row = worksheet.getRow(0);
            if(checkColumnname(row,0,"PART NUMBER"))
            {
                message.setCode(BomConstants.NUMBER_1001);
                message.setInformation("First column name should be part number");
                return message;
            }
            if(checkColumnname(row,1,"DESCRIPTION"))
            {
                message.setCode(BomConstants.NUMBER_1001);
                message.setInformation("Second column name should be description");
                return message;
            }
            if(checkColumnname(row,2,"QUANTITY"))
            {
                message.setCode(BomConstants.NUMBER_1001);
                message.setInformation("Third column name should be quantity");
                return message;
            }
            for(int index=1;index<worksheet.getPhysicalNumberOfRows();index++)
            {
                row=worksheet.getRow(index);
                if(checkPartNumber(row,0)) {
                    message.setCode(BomConstants.NUMBER_1001);
                    message.setInformation("Data Should be Alphanumeric for PartNumber = "+row.getCell(0));
                    return message;
                }
                if(checkDescription(row,1)){
                    message.setCode(BomConstants.NUMBER_1001);
                    message.setInformation("Description Data should be String for PartNumber = "+row.getCell(0)+". It should not contain vlookup formulas");
                    return message;
                }
                if(checkQuantity(row,2)){
                    message.setCode(BomConstants.NUMBER_1001);
                    message.setInformation("Quantity Data should be Integer/Decimal for PartNumber = "+row.getCell(0)+". It should not contain vlookup formula,commas,alphabets");
                    return message;
                }
                if(checkWeightAndUnitOfMeasure(row,3,4))
                {
                    message.setCode(BomConstants.NUMBER_1001);
                    message.setInformation("Weight or its unit of measure is wrong for PartNumber = "+row.getCell(0)+".Weight should be given along with its unit of measure");
                    return message;
                }
            }
        }
        catch(Exception e)
        {
            logger.error("Exception while uploading bom file ::: {}", e.getMessage(), e);
            message.setCode(BomConstants.NUMBER_1001);
            message.setInformation("Exception while uploading bom file "+e.getMessage());
            return message;
        }
        message.setCode(BomConstants.NUMBER_1000);
        message.setInformation("Successfully uploaded!");
        return message;
    }

    private boolean checkColumnname(XSSFRow row, int index, String col_name) {
        if(row==null)return true;
        XSSFCell cellData = row.getCell(index);
        if(cellData != null && cellData.getStringCellValue().toUpperCase().contains(col_name))
                return false;

        return true;
    }

    private boolean checkQuantity(XSSFRow row, int index) {
        XSSFCell cellData = row.getCell(index);
        if(cellData != null && !cellData.equals(BomConstants.EMPTY_STRING))
        {
            String data=cellData.toString().trim();
            if(data.contains("VLOOKUP") || data.contains(",") )
                return true;
            for(char ch:data.toCharArray())
            {
                if(ch=='.')continue;
                if (!Character.isDigit(ch)) return true;
            }
        }
        return false;
    }

    private boolean checkWeightAndUnitOfMeasure(XSSFRow row, int index1,int index2) {
        if(row.getCell(index1)==null && row.getCell(index2)==null)return false;
        String weightData = row.getCell(index1).toString().trim();
        String unitofMeasure=row.getCell(index2).toString().trim();
        if(weightData.equals(""))return false;
        else {
            for(char ch:weightData.toCharArray()) {
                if(ch=='.')continue;
                if (!Character.isDigit(ch)) return true;
            }
            if(unitofMeasure.equals(""))return true;
        }
        return false;
    }

    private boolean checkDescription(XSSFRow row, int index) {
        XSSFCell cellData = row.getCell(index);
        if(cellData != null && !cellData.equals(BomConstants.EMPTY_STRING))
        {
            String data=cellData.toString().trim();
            if(data.contains("VLOOKUP"))
                return true;
        }
        return false;
    }

    private boolean checkPartNumber(XSSFRow row, int index) {
        XSSFCell cellData = row.getCell(index);
        Object data=null;
        String value="";
        if(cellData != null && !cellData.equals(BomConstants.EMPTY_STRING))
        {
            switch (cellData.getCellType()) {
                case STRING:
                    data = cellData.getStringCellValue();
                    value = data.toString().trim();
                    break;
                case NUMERIC:
                    data = cellData.getRawValue();
                    value = new BigDecimal(data.toString()).toPlainString().trim();
                    break;
                default:
                    return true;
            }
            if(!Pattern.compile("^[a-zA-Z0-9\\s|\\-\\'\\:\\_\\.\\,\\*\\#\\!]*$").matcher(value).find())
                return true;
            if(value.contains("VLOOKUP"))
                return true;
        }
        return false;
    }

    private boolean updateHistoryData(String productNumber, String releaseNumber) {
        List<BomModel> historyModelList = bomModelRepository.findByProductNumberAndStatusOrderByIdDesc(productNumber, BomConstants.ACTIVE);
        List<BomModel> storedData = historyModelList.stream().filter(bomModel -> bomModel.getProductNumber().equals(productNumber) && bomModel.getReleaseNumber().equals(releaseNumber)).collect(
            Collectors.toList());
        if (!storedData.isEmpty()) {
            return false;
        }
        String[] newMajorAndMinorVersion = releaseNumber.split("\\.");
        if( Integer.parseInt(newMajorAndMinorVersion[1]) == 0) {
            historyModelList.parallelStream().forEach(bomModel -> { bomModel.setStatus(BomConstants.HISTORY);});
            bomModelRepository.saveAll(historyModelList);
        }
        return true;
    }

    private Message importBomData(MultipartFile modelFile, BomModel model) {
        Message message = new Message();
        try (XSSFWorkbook workbook = new XSSFWorkbook(modelFile.getInputStream())) {
            XSSFSheet worksheet = workbook.getSheetAt(0);
            List<Bom> bomList = new ArrayList<>();
            List<String> partNumbers = new ArrayList<>();
            for(int index=1;index<worksheet.getPhysicalNumberOfRows() ;index++) {
                XSSFRow row = worksheet.getRow(index);
                String partNumber = getPartNumber(row, 0);
                if(partNumber.equals(BomConstants.EMPTY_STRING))
                    continue;
//                if(!masterpartsupplierdataRepository.existsByPartnumbercode(partNumber))
//                    continue;
                if (!partNumbers.contains(partNumber)) {
                    partNumbers.add(partNumber);
                    Bom bom = new Bom();
                    bom.setPartNumber(partNumber);
                    bom.setDescription(getStringCellValue(row, 1));
                    bom.setQuantity(getDecimalCellValue(row, 2));
                    if(row.getCell(3)!=null && !row.getCell(3).toString().equals(BomConstants.EMPTY_STRING))
                        bom.setWeight(getDecimalCellValue(row, 3));
                    else
                        bom.setWeight(null);
                    bom.setUnitOfMeasure(getStringCellValue(row, 4));
                    String morb = getStringCellValue(row, 5);
                    if (morb != null && morb.equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
                        bom.setMorb(BomConstants.BUY_PARTS);
                    } else {
                        bom.setMorb(morb);
                    }
                    bom.setSupplierCode(getStringCellValue(row, 6));
                    bom.setSupplierDescription(getStringCellValue(row, 7));
                    bom.setProductNumber(model.getProductNumber());
                    bom.setReleaseNumber(model.getReleaseNumber());
                    String reference=getStringCellValue(row, 8);
                    if (reference != null && !reference.isEmpty()) {
                        bom.setReferencePartNumber(reference);
                        bom.setReferenceFlag("R");
                    }
                    bomList.add(bom);
                }
                else {
                    Bom bom=bomList.get(partNumbers.indexOf(partNumber));
                    bomList.remove(bom);
                    double quantity=bom.getQuantity().doubleValue();
                    quantity+=getDecimalCellValue(row, 2).doubleValue();
                    bom.setQuantity(BigDecimal.valueOf(quantity));
                    bomList.add(partNumbers.indexOf(partNumber),bom);
                }
            }
            //mdsService.retrieveMdsData(bomList);
            bomRepository.saveAll(bomList);
            mdsService.retrieveReferencePNForBom(model);
            message.setCode(BomConstants.NUMBER_1000);
            message.setInformation("Successfully uploaded!");
        } catch (IOException e) {
            logger.error("IOException while uploading bom file ::: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Exception while uploading bom file ::: {}", e.getMessage(), e);
        }
        return message;
    }

    private String getPartNumber(XSSFRow row, int index) {
        String value = BomConstants.EMPTY_STRING;
        XSSFCell cellData = row.getCell(index);
        Object data = null;
        if (cellData != null) {
            switch (cellData.getCellType()) {
                case STRING:
                    data = cellData.getStringCellValue();
                    value = data.toString();
                    break;
                case NUMERIC:
                    data = cellData.getRawValue();
                    value = new BigDecimal(data.toString()).toPlainString();
                    break;
            }
        }
        while ( (value.length() > 0) && (value.charAt(0) == '0' || value.charAt(0) == ' ')) {
            value =  value.substring(1);
        }
        return value;
    }

    private static BigDecimal getDecimalCellValue(XSSFRow row, int index) {
        BigDecimal value = BigDecimal.valueOf(0.00);
        XSSFCell cellData = row.getCell(index);
        if (cellData != null && !cellData.toString().equals("")){
            if(cellData.getCellType() == CellType.NUMERIC)
            value = BigDecimal.valueOf(cellData.getNumericCellValue());
            else value= BigDecimal.valueOf(Long.parseLong(cellData.getStringCellValue()));
        }
        return value;
    }

    private static long getLongCellValue(XSSFRow row, int index) {
        long value = 0;

        XSSFCell cellData = row.getCell(index);
        if (cellData != null) {
            value = (long) cellData.getNumericCellValue();
        }
        return value;
    }



    private static String getStringCellValue(XSSFRow row, int index) {
        String value = BomConstants.EMPTY_STRING;
        DataFormatter dataFormatter = new DataFormatter();
        XSSFCell cellData = row.getCell(index);
        if (cellData != null){
            value = dataFormatter.formatCellValue(cellData);
        }
        return value;
    }

    public InputBomModel getModelDetails(String productNumber, int businessUnit, String userName) {
        List<BomModel> bomModelList = bomModelRepository.findByProductNumberAndStatusOrderByIdDesc(productNumber, BomConstants.ACTIVE);
        InputBomModel outputModel = new InputBomModel();
        if (!bomModelList.isEmpty()){
            BomModel model = selectLatestBOM(bomModelList);
            Optional<BusinessUnit> checkBU=businessUnitRepository.findById(model.getBusinessUnitId());
            String businessUnitName=businessUnitRepository.findByUserName(userName).getBusinessUnitName();
            if (businessUnitName.equalsIgnoreCase(checkBU.get().getBusinessUnitName())) {
                outputModel.setId(model.getId());
                outputModel.setProductNumber(model.getProductNumber());
                outputModel.setReleaseNumber(model.getReleaseNumber());
                outputModel.setDescription(model.getDescription());
                outputModel.setMarket(model.getMarket());
                outputModel.setShortDescription(model.getShortDescription());
                outputModel.setLongDescription(model.getLongDescription());
                outputModel.setStatus(model.getStatus());
                outputModel.setTaricCode(model.getTaricCode());
                outputModel.setTaricForComponents(model.getTaricForComponents());
                outputModel.setDossierNumber(model.getDossierNumber());
                outputModel.setThreeRStatus(model.getThreeRStatus());
                outputModel.setBusinessUnitId(model.getBusinessUnitId());
                outputModel.setPlant(model.getPlant());
                outputModel.setBusinessUnitName(businessUnitName);
            } else {
                outputModel.setAccessState(HttpStatus.UNAUTHORIZED.name());
            }
        }
        return outputModel;
    }

    private BomModel selectLatestBOM(List<BomModel> bomModelList) {
        BomModel model = bomModelList.get(0);
        Integer minorVer = 0;
        Integer majorVer = 0;
        for (BomModel bomModel : bomModelList) {
            String[] release = bomModel.getReleaseNumber().split("\\.");
            if (Integer.parseInt(release[0]) > majorVer) {
                model = bomModel;
                majorVer = Integer.parseInt(release[0]);
                if(release.length == 1) {
                    minorVer = 0;
                }else {
                    minorVer = Integer.parseInt(release[1]);
                }
            } else if(Integer.parseInt(release[0]) == majorVer) {
                int version = 0;
                if (release.length == 2) {
                    version = Integer.parseInt(release[1]);
                }
                if (version > minorVer) {
                    model = bomModel;
                    minorVer = version;
                }
            }
        }
        return model;
    }

    public BusinessUnit getBusinessUnit(String userName) {
        return businessUnitRepository.findByUserName(userName);
    }

    @Transactional
    public NewBomModel updateBomModel(InputBomModel bomModel, String userName) {
        BomModel newModel = bomModelRepository.findByProductNumberAndReleaseNumber(bomModel.getProductNumber(), bomModel.getReleaseNumber());
        newModel.setMarket(bomModel.getMarket());
        newModel.setDescription(bomModel.getDescription());
        newModel.setLongDescription(bomModel.getLongDescription());
        newModel.setTaricCode(bomModel.getTaricCode());
        newModel.setTaricForComponents(bomModel.getTaricForComponents());
        newModel.setUpdated_by(userName);
        newModel.setShortDescription(bomModel.getShortDescription());
        newModel.setMeasuredWeight(bomModel.getMeasuredWeight());
        newModel.setMeasuredUnit(bomModel.getMeasuredUnit());
        boolean vFlag = validateRegulation(newModel, bomModel);
        newModel.setRegulation(bomModel.getRegulation());
        bomModelRepository.save(newModel);
        if (vFlag) {
            remove3RRegulationData(newModel);
        }
        logger.info("Save Completed");
        NewBomModel newBomModel=new NewBomModel();
        newBomModel.setBusinessUnit(bomModel.getBusinessUnit());
        newBomModel.setDescription(bomModel.getDescription());
        newBomModel.setId(bomModel.getId());
        newBomModel.setProductNumber(bomModel.getProductNumber());
        newBomModel.setReleaseNumber(bomModel.getReleaseNumber());
        newBomModel.setMarket(bomModel.getMarket());
        newBomModel.setLongDescription(bomModel.getLongDescription());
        newBomModel.setTaricCode(bomModel.getTaricCode());
        newBomModel.setTaricForComponents(bomModel.getTaricForComponents());
        newBomModel.setUpdated_by(userName);
        newBomModel.setShortDescription(bomModel.getShortDescription());
        newBomModel.setBusinessUnitId(bomModel.getBusinessUnitId());
        newBomModel.setTaricCode(bomModel.getTaricCode());
        newBomModel.setTaricForComponents(bomModel.getTaricForComponents());
        newBomModel.setThreeRStatus(bomModel.getThreeRStatus());
        newBomModel.setDossierNumber(bomModel.getDossierNumber());
        newBomModel.setCreated_by(newModel.getCreated_by());
        newBomModel.setPlant(newModel.getPlant());
        newBomModel.setUpdated_by(newModel.getUpdated_by());
        newBomModel.setMeasuredWeight(newModel.getMeasuredWeight());
        newBomModel.setMeasuredUnit(newModel.getMeasuredUnit());
        newBomModel.setRegulation(newModel.getRegulation());
        return newBomModel;
    }

    /**
     * If regulation changes
     *
     */
    private boolean validateRegulation(BomModel savedModel, InputBomModel changedModel) {
        if (savedModel.getRegulation() == null && changedModel.getRegulation().equalsIgnoreCase("Europe")) {
            return false;
        } else if (savedModel.getRegulation() == null && null!=changedModel.getRegulation() && !changedModel.getRegulation().equalsIgnoreCase("Europe")) {
            return true;
        } else if (!savedModel.getRegulation().equalsIgnoreCase(changedModel.getRegulation())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removing 3R dependent data;
     *
     */

    private void remove3RRegulationData(BomModel newModel) {
        logger.info("Removing 3R regulation dependent data");
        List<ThreeRProduct> list = threeRProductRepository.findByBomModelId(newModel.getId());
        list.stream().forEach(threeRProduct -> threeRProduct.setProvenDismountingId(null));
        threeRProductRepository.saveAll(list);
        threeRResidueRepository.deleteByBomModelId(newModel.getId());
        productCalculationRepository.deleteByBomModelId(newModel.getId());
    }

    private boolean isMinorVersionIncremented(String newReleaseNo, String oldReleaseNo){
        String[] newMajorAndMinorVersion = newReleaseNo.split("\\.");
        String[] oldMajorAndMinorVersion = oldReleaseNo.split("\\.");
        logger.info("Old version number : {}", newReleaseNo);
        logger.info("New version number : {}", oldReleaseNo);
        return ((Integer.parseInt(newMajorAndMinorVersion[0]) == Integer.parseInt(oldMajorAndMinorVersion[0])) &&
                (Integer.parseInt(newMajorAndMinorVersion[1]) > Integer.parseInt(oldMajorAndMinorVersion[1])));
    }

    public List<plant> getplant() {
        return (List<plant>) plantRepository.findAll();
    }

}
