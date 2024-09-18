package com.dxc.macs.bom.service;

import com.dxc.macs.bom.constants.BomConstants;
import com.dxc.macs.bom.entity.*;
import com.dxc.macs.bom.mapper.BomResultRowMapper;
import com.dxc.macs.bom.model.*;
import com.dxc.macs.bom.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class BomScreenService {

    private final Logger logger = LoggerFactory.getLogger(BomScreenService.class);

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BusinessUnitNameRepository businessUnitNameRepository;

    @Autowired
    private BomStatusRepository bomStatusRepository;

    @Autowired
    private MdsStatusRepository mdsStatusRepository;

    @Autowired
    private MdsStatusBomRepository mdsStatusBomRepository;

    @Autowired
    private RecipientSpecRepository recipientSpecRepository;

    @Autowired
    private BomModelRepository bomModelRepository;

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    public BusinessUnitRepository businessUnitRepository;

    public Collection<BomResult> getProductsList(BomSearchCriteria bomSearch){
        logger.info("Retrieving the list of products for the logged in user");
        String queryForProducts = buildQuery(bomSearch);
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedJdbcTemplate.query(queryForProducts, new BomResultRowMapper());
    }

    private String buildQuery(BomSearchCriteria bomSearch) {
        boolean whereClause = false;
        StringBuilder query = new StringBuilder("select a.id, a.product_number, a.description, a.release_number, a.created_date, a.created_by, a.short_description, a.market \n" +
                ", c.business_unit_name \n" +
                ", a.status, a.long_description, a.taric_code, a.taric_code_components \n" +
                ", COUNT(bom.part_number) as totalParts \n" +
                ", SUM(CASE WHEN scm.part_number IS NOT NULL THEN 1 ELSE 0 END) as no_of_certificate_attached \n" +
                ", SUM(CASE WHEN ((bom.preliminary = 'No' OR bom.preliminary is null) AND pn.module_status = 'A') THEN 1 ELSE 0 END) acceptedParts \n" +
                ", SUM(CASE WHEN ((bom.preliminary = 'No' OR bom.preliminary is null) AND pn.module_status = 'R') THEN 1 ELSE 0 END) rejectedParts \n" +
                ", SUM(CASE WHEN ((bom.preliminary = 'No' OR bom.preliminary is null) AND pn.module_status IN ('N', 'B')) THEN 1 ELSE 0 END) suspendedParts \n" +
                ", SUM(CASE WHEN bom.preliminary = 'Yes' THEN 1 ELSE 0 END) preliminaryParts \n" +
                ", a.threer_status, a.business_unit_id, a.measured_weight, a.weighted_unit, a.dossier_number \n" +
                ", sub_query.tot_measured_weight, sub_query.tot_calculated_weight \n" +
                ", a.cdx_job_id, a.echa_id, a.scip_number, a.echa_url, a.scip_validation_message, IFNULL(regulation, 'Europe') as regulation \n" +
                "from bom_model a \n" +
                "JOIN bom bom \n" +
                "ON a.product_number = bom.bom_model_product_number \n" +
                "AND a.release_number = bom.bom_model_release_number \n");
        if (bomSearch != null) {
            buildQueryForTotWeight(bomSearch,query);
            buildQueryForBU(bomSearch, query);
            buildQueryForTotalBuyMdsStatusParts(bomSearch, query);
            buildQueryForPartNumber(bomSearch,query);
            buildWhereClause(bomSearch, whereClause, query);
            query.append(" group by a.id, a.product_number, a.description, a.release_number, a.created_date, a.created_by, a.short_description, a.market \n" +
                    ", c.business_unit_name \n" +
                    ", a.status, a.long_description, a.taric_code, a.taric_code_components \n" +
                    ", a.threer_status, a.business_unit_id, a.measured_weight, a.weighted_unit, a.dossier_number \n" +
                    ", sub_query.tot_measured_weight, sub_query.tot_calculated_weight \n" +
                    ", a.cdx_job_id, a.echa_id, a.scip_number, a.echa_url, a.scip_validation_message \n");
            query.append("order by a.product_number, a.id asc, a.created_date desc \n");
        }
        logger.info("Query for retrieving all the products ::: {}", query);
        return query.toString();
    }

    private static String convertToDateFormat(Date dateField) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dateField);
    }

    private void buildQueryForTotWeight(BomSearchCriteria bomSearch, StringBuilder query) {
        ConfigParam configParam = configParamService.getConfigValue("ORG_UNIT");
        String paramValue= configParam.getParamValue();
        String check_flag="";
        if(paramValue.contains("92551")){
            check_flag="LV";
        }
        else {
            check_flag="LA";
        }
        query.append("\nLEFT JOIN (SELECT ");
        query.append("bom.bom_model_product_number, \n");
        query.append("bom.bom_model_release_number, \n");
        query.append("ROUND(COALESCE(SUM(IFNULL(CASE \n");
        query.append("WHEN UPPER(bom.unit_of_measure) = 'KG' THEN \n");
        query.append("CASE WHEN bom.weight IS NULL OR bom.weight = 0 THEN c.measured_weight ELSE bom.weight * 1000 END \n");
        query.append("ELSE \n");
        query.append("CASE WHEN bom.weight IS NULL OR bom.weight = 0 THEN c.measured_weight ELSE bom.weight END \n");
        query.append("END, c.measured_weight)),0),3) AS tot_measured_weight, \n");
        query.append("ROUND(SUM(IFNULL(c.calculated_weight, 0)),3) AS tot_calculated_weight \n");
        query.append("FROM bom bom \n");
        query.append("INNER JOIN part_number_mds_latest_association pa ON pa.part_number = bom.part_number and pa.check_flag = '").append(check_flag).append("' \n");
        query.append("INNER JOIN bom_model bm ON (bom.bom_model_product_number = bm.product_number AND bom.bom_model_release_number = bm.release_number) \n");
        query.append("LEFT JOIN component c ON c.node_id = bom.mds_node_id AND c.version = bom.version \n");
        if (!bomSearch.getProduct().equalsIgnoreCase("")){
            query.append("WHERE bm.product_number = '").append(bomSearch.getProduct()).append("' \n");
        }
        query.append("GROUP BY bom.bom_model_product_number, bom.bom_model_release_number) sub_query ON a.product_number = sub_query.bom_model_product_number AND sub_query.bom_model_release_number = a.release_number \n");
    }


    private static void buildQueryForBU(BomSearchCriteria bomSearch, StringBuilder query) {
        if (bomSearch.getBusinessUnit().equalsIgnoreCase(BomConstants.ALL_BU) || bomSearch.getBusinessUnit().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            query.append("JOIN business_unit c ON a.business_unit_id = c.id \n" +
                    "AND c.business_unit_name <> 'Race Cars' \n");
        } else {
            query.append("RIGHT JOIN (\n" +
                    "\tselect b.id, b.business_unit_name from business_unit b \n" +
                    "\t\t where b.business_unit_name  like '%"+ bomSearch.getBusinessUnit() + "%' \n" +
                    ") c \t\n" +
                    "on a.business_unit_id = c.id ");
        }
    }

    private void  buildQueryForPartNumber(BomSearchCriteria bomSearch, StringBuilder query) {
        if (bomSearch.getPartNumber() != null && !bomSearch.getPartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("JOIN bom l on l.bom_model_product_number=a.product_number and l.bom_model_release_number=a.release_number \n");
        }
    }
    private void buildQueryForTotalBuyMdsStatusParts(BomSearchCriteria bomSearch, StringBuilder query) {
        ConfigParam configParam = configParamService.getConfigValue("ORG_UNIT");
        String paramValue= configParam.getParamValue();
        String check_flag="";
        if(paramValue.contains("92551")){
            check_flag="LV";
        }
        else {
            check_flag="LA";
        }
        query.append("LEFT JOIN part_number_mds_latest_association pn ON (bom.part_number = pn.part_number and pn.check_flag = '").append(check_flag).append("') \n");
        query.append("LEFT JOIN self_certificate_mapping scm ON bom.part_number = scm.part_number AND scm.cert_path IS NOT NULL \n");
    }



    private static void buildWhereClause(BomSearchCriteria bomSearch, boolean whereClause, StringBuilder query) {
        buildStartOfWhereClause(bomSearch, query);
        if (!bomSearch.getProduct().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            query.append("a.product_number like '%").append(bomSearch.getProduct()).append("%' ");
            whereClause = true;
        } else {
            query.append(" 1=1 ");
            whereClause = true;
        }
        if (whereClause && !bomSearch.getDescription().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("and a.description like '%").append(bomSearch.getDescription()).append("%' ");
        } else if (bomSearch.getDescription() != null && !bomSearch.getDescription().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("a.description like '").append(bomSearch.getDescription()).append("' ");
            whereClause = true;
        }
        if (whereClause && !bomSearch.getStatus().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("and a.status = '").append(bomSearch.getStatus()).append("' ");
        } else if (bomSearch.getStatus() != null && !bomSearch.getStatus().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("a.status = '").append(bomSearch.getStatus()).append("' ");
            whereClause = true;
        }
        if(bomSearch.getPartNumber() != null && !bomSearch.getPartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            query.append("and (l.part_number = LTRIM(TRIM(LEADING '0' FROM '").append(bomSearch.getPartNumber()).append("')) or  l.part_number='").append(bomSearch.getPartNumber()).append("') ");
        }
        if (bomSearch.getFromCreateDate() != null) {
            query.append(" and a.created_date >= '").append(convertToDateFormat(bomSearch.getFromCreateDate())).append("' ");
        }
        if (bomSearch.getToCreateDate() != null) {
            query.append(" and a.created_date <= '").append(convertToDateFormat(bomSearch.getToCreateDate())).append("' ");
        }
        whereClauseFor3RAndReleaseNumber(bomSearch, whereClause, query);
    }

    private static void whereClauseFor3RAndReleaseNumber(BomSearchCriteria bomSearch, boolean whereClause, StringBuilder query) {
        if (whereClause && bomSearch.getReleaseNumber() != null && !bomSearch.getReleaseNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("and a.release_number = '").append(bomSearch.getReleaseNumber()).append("' ");
        } else if (bomSearch.getReleaseNumber() != null && !bomSearch.getReleaseNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("a.release_number = '").append(bomSearch.getReleaseNumber()).append("' ");
            whereClause = true;
        }
        if (whereClause && !bomSearch.getThreeR().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("and a.threer_status = '").append(bomSearch.getThreeR()).append("' ");
        } else if (!bomSearch.getThreeR().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
            query.append("a.threer_status = '").append(bomSearch.getThreeR()).append("' ");
        }
    }

    private static void buildStartOfWhereClause(BomSearchCriteria bomSearch, StringBuilder query) {
        if ((!bomSearch.getStatus().equalsIgnoreCase(BomConstants.EMPTY_STRING) ||
                !bomSearch.getDescription().equalsIgnoreCase(BomConstants.EMPTY_STRING) ||
                !bomSearch.getProduct().equalsIgnoreCase(BomConstants.EMPTY_STRING) ||
                !bomSearch.getThreeR().equalsIgnoreCase(BomConstants.EMPTY_STRING) ||
                !bomSearch.getPartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING))){
            query.append("where ");
        }
    }

    public Collection<BusinessUnitName> getBusinessUnitNames() {
        return (Collection<BusinessUnitName>) businessUnitNameRepository.findAll();
    }

    public Collection<BomStatus> getBomStatuses() {
        return (Collection<BomStatus>) bomStatusRepository.findAll();
    }

    public Collection<MdsStatusBom> getMdsStatuses() {
        return (Collection<MdsStatusBom>) mdsStatusBomRepository.findAll();
    }

    public Message duplicateBom(BomProduct bomProduct, String userName) {
        logger.info("Duplicate of bom initiated for the product number {} and  release number {}", bomProduct.getProductNumber(), bomProduct.getReleaseNumber());
        Message message = new Message();
        BomModel bomModel = bomModelRepository.findByProductNumberAndReleaseNumber(bomProduct.getProductNumber(), bomProduct.getReleaseNumber());
        List<Bom> bomList = bomRepository.findByProductNumberAndReleaseNumber(bomProduct.getProductNumber(), bomProduct.getReleaseNumber());
        List<BomModel> releaseNumbers = bomModelRepository.findByProductNumberOrderByIdDesc(bomProduct.getProductNumber());
        if (!releaseNumbers.isEmpty()) {
            String latestReleaseNumber = selectLatestBOM(releaseNumbers).getReleaseNumber();
            if (bomModel != null) {
                BomModel duplicateBomModel = new BomModel();
                duplicateBomModel.setProductNumber(bomModel.getProductNumber());
                double releaseNumber = new BigDecimal(latestReleaseNumber).doubleValue() + 0.1;
                DecimalFormat df = new DecimalFormat("##0.0");
                duplicateBomModel.setReleaseNumber(df.format(new BigDecimal(releaseNumber)));
                duplicateBomModel.setMarket(bomModel.getMarket());
                duplicateBomModel.setTaricCode(bomModel.getTaricCode());
                duplicateBomModel.setDescription(bomModel.getDescription());
                duplicateBomModel.setLongDescription(bomModel.getLongDescription());
                duplicateBomModel.setShortDescription(bomModel.getShortDescription());
                duplicateBomModel.setBusinessUnitId(bomModel.getBusinessUnitId());
                duplicateBomModel.setStatus(bomModel.getStatus());
                duplicateBomModel.setTaricForComponents(bomModel.getTaricForComponents());
                duplicateBomModel.setDossierNumber(bomModel.getDossierNumber());
                duplicateBomModel.setThreeRStatus(bomModel.getThreeRStatus());
                duplicateBomModel.setCreated_by(userName);
                duplicateBomModel.setUpdated_by(userName);
                if (!bomList.isEmpty()){
                    bomModelRepository.save(duplicateBomModel);
                    for (Bom bom : bomList) {
                        Bom duplicateBom = new Bom();
                        duplicateBom.setProductNumber(duplicateBomModel.getProductNumber());
                        duplicateBom.setReleaseNumber(duplicateBomModel.getReleaseNumber());
                        duplicateBom.setWeight(bom.getWeight());
                        duplicateBom.setMorb(bom.getMorb());
                        duplicateBom.setMdsType(bom.getMdsType());
                        duplicateBom.setPreliminary(bom.getPreliminary());
                        duplicateBom.setQuantity(bom.getQuantity());
                        duplicateBom.setSupplierDescription(bom.getSupplierDescription());
                        duplicateBom.setSupplierCode(bom.getSupplierCode());
                        duplicateBom.setUnitOfMeasure(bom.getUnitOfMeasure());
                        duplicateBom.setDescription(bom.getDescription());
                        duplicateBom.setPartNumber(bom.getPartNumber());
                        duplicateBom.setMdsNodeId(bom.getMdsNodeId());
                        duplicateBom.setVersion(bom.getVersion());
                        bomRepository.save(duplicateBom);
                    }
                    message.setCode(BomConstants.NUMBER_1000);
                    message.setInformation("Successfully duplicated!");
                }
                logger.info("Duplicate of bom completed for the product number {} and  release number {}", bomProduct.getProductNumber(), bomProduct.getReleaseNumber());
            } else {
                message.setCode(BomConstants.NUMBER_1001);
                message.setInformation("Duplicates of Bom failed");
                logger.info("Duplicate of bom failed for the product number {} and  release number {}", bomProduct.getProductNumber(), bomProduct.getReleaseNumber());
            }
        } else {
            message.setCode(BomConstants.NUMBER_1001);
            message.setInformation("Duplicates of Bom failed");
            logger.info("Duplicate of bom failed for the product number {} and  release number {} as there is no release number found", bomProduct.getProductNumber(), bomProduct.getReleaseNumber());
        }

        return message;
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

    public BusinessUnit getBusinessUnitForUser(String userName) {
        return businessUnitRepository.findByUserName(userName);
    }
}
