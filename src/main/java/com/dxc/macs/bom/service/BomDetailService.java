package com.dxc.macs.bom.service;

import com.dxc.macs.bom.constants.BomConstants;
import com.dxc.macs.bom.entity.*;
import com.dxc.macs.bom.mapper.MaterialsNodeIdRowMapper;
import com.dxc.macs.bom.mapper.*;
import com.dxc.macs.bom.model.*;
import com.dxc.macs.bom.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class BomDetailService {

    public static final String FIELDS_ARE_NULL = "Error while adding the part Numbers : Mandatory fields are null";

    public static final String ALREADY_EXIST = "Error while adding the part Numbers : Part number already present in BOM";
    private final Logger logger = LoggerFactory.getLogger(BomDetailService.class);

    @Autowired
    public MdsTypeRepository mdsTypeRepository;

    @Autowired
    public BomRepository bomRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MdsService mdsService;

    @Autowired
    private RelAmountRepository relAmountRepository;

    @Autowired
    private RelWeightRepository relWeightRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MultilingualRepository multilingualRepository;

    @Autowired
    private BomMaterialRepository bomMaterialRepository;

    @Autowired
    private MaterialClassificationRepository materialClassificationRepository;

    @Autowired
    private ClassificationNameRepository classificationNameRepository;

    @Autowired
    private SemiComponentRepository semiComponentRepository;

    @Autowired
    private RelPercentageRepository relPercentageRepository;

    @Autowired
    private SubstanceRepository substanceRepository;

    @Autowired
    private SynonymRepository synonymRepository;

    @Autowired
    private SubstanceApplicationRepository substanceApplicationRepository;

    @Autowired
    private BomModelRepository bomModelRepository;

    @Autowired
    private ThreeRProductRepository threeRProductRepository;

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private Bom3RMaterialRepository bom3RMaterialRepository;

    @Autowired
    private Environment env;

    public Collection<MdsType> getMdsType() {
        return (Collection<MdsType>) mdsTypeRepository.findAll();
    }

    public List<BomPartNumber> findAllPartNumbers(BomDetailSearchCriteria bomSearch){
        logger.info("Retrieving the part number details for the selected product number : {}", bomSearch.getProductNumber());
        // select part number and version for CURRENT VERSION and pass it to query
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        StringBuilder queryForParts = new StringBuilder();
        queryForParts.append("select distinct id, part_number, description, quantity, morb, weight, unit_of_measure, supplier_code, supplier_description, \n")
                .append("bom_model_product_number, bom_model_release_number, mds_type, top_level_node_id, preliminary, module_status, reference_partnumber, version, macs_part_number, cert_attached, reference_flag, part_status, snstatus, notified_count, notified_date, newstatus from \n")
                .append("(select distinct bom.id, bom.part_number, bom.description, bom.quantity, \n")
                .append("CASE when pa1.part_number is not null and pa1.make_or_buy_flag = 'I' THEN 'M' ELSE ' ' END morb, \n")
                .append("bom.weight, bom.unit_of_measure, bom.supplier_code, bom.supplier_description, bom.bom_model_product_number, bom.bom_model_release_number, \n")
                .append("pa1.mds_type, pa1.top_level_node_id, pa1.preliminary, pa1.module_status, '', pa1.version, \n")
                .append("IF(bom.reference_partnumber is null or bom.reference_partnumber = '',IFNULL(pa1.macs_part_number, bom.part_number), bom.part_number) macs_part_number \n")
                .append(" , CASE WHEN map.cert_path IS NOT NULL THEN 'S' ELSE '' END cert_attached, bom.reference_flag, CASE WHEN bom.reference_flag = 'R' THEN bom.reference_partnumber ELSE NULL END AS reference_partnumber  \n")
                .append(" , IFNULL(pns.is_disabled, false) part_status,sn.recipient_status as snstatus, sn.notified_count, sn.notified_date, pa2.module_status newstatus \n")
                .append("from v_bom_mds_association bom \n")
                .append(" LEFT JOIN self_certificate_mapping map on map.part_number = bom.part_number ")
                .append(" LEFT JOIN part_number_notification_status pns on pns.part_number = bom.part_number ")
                .append("LEFT JOIN part_number_mds_latest_association pa1 \n");
        if (bomSearch.getCurrentVersion() != null && bomSearch.getCurrentVersion().booleanValue() == Boolean.TRUE) {
            queryForParts.append("ON pa1.part_number = if(bom.reference_partnumber is null or bom.reference_partnumber = '', bom.part_number, trim(leading '0' from bom.reference_partnumber)) and pa1.check_flag = 'LV' \n");
        } else {
            queryForParts.append("ON pa1.part_number = if(bom.reference_partnumber is null or bom.reference_partnumber = '', bom.part_number, trim(leading '0' from bom.reference_partnumber)) and pa1.check_flag = 'LA' \n");
        }
        queryForParts.append(" LEFT JOIN part_number_mds_latest_association pa2\n"
            + "ON pa2.part_number = if(bom.reference_partnumber is null or bom.reference_partnumber = '', bom.part_number, trim(leading '0' from bom.reference_partnumber)) and pa2.check_flag = 'LV' \n"
            + "LEFT JOIN supplier_notified_part_number_map sn\n"
            + "ON bom.part_number = sn.part_number AND (sn.recipient_status = \n"
            + "CASE WHEN pa2.module_status IS NOT NULL AND pa2.module_status = 'R' THEN 'Rejected'\n"
            + "WHEN pa2.module_status IS NULL THEN 'Not received'\n"
            + "WHEN IFNULL(pa2.preliminary, 9)=1  THEN 'Preliminary'\n"
            + "ELSE ''\n"
            + "END) \n");
        queryForParts.append("WHERE bom.bom_model_product_number = '").append(bomSearch.getProductNumber()).append("' \n")
                .append("and bom.bom_model_release_number = '").append(bomSearch.getReleaseNumber()).append("') tmp where 1=1 \n");
        if (bomSearch.getType().equalsIgnoreCase(BomConstants.MAKE)) {
            queryForParts.append("and tmp.morb = 'M' \n");
        } else if (bomSearch.getType().equalsIgnoreCase(BomConstants.BUY)) {
            queryForParts.append("and tmp.morb = ' ' \n");
        }
        if (bomSearch.getPartNumber() != null && !bomSearch.getPartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            queryForParts.append("and tmp.part_number like '%").append(bomSearch.getPartNumber()).append("%' \n");
        }
        if (bomSearch.getRefPartNumber() != null && !bomSearch.getRefPartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            queryForParts.append("and tmp.reference_partnumber like '%").append(bomSearch.getRefPartNumber()).append("%' \n");
        }
        if (bomSearch.getMdsType() != null && !bomSearch.getMdsType().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            String type = getMdsTypeValue(bomSearch.getMdsType());
            if (type != null) {
                queryForParts.append("and tmp.mds_type = '").append(type).append("' \n");
            }
        }
        if(bomSearch.getBomDescription() != null && !bomSearch.getBomDescription().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            queryForParts.append("and tmp.description like '%").append(bomSearch.getBomDescription()).append("%'\n");
        }
        if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.ACCEPTED)){
            queryForParts.append("and tmp.module_status = 'A' \n");
        } else if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.REJECTED)){
            queryForParts.append("and tmp.module_status = 'R' \n");
        } else if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.IN_PROCESS_AT_RECIPIENT)) {
            queryForParts.append("and (tmp.module_status = 'N'  or tmp.module_status = 'B') \n");
        } else if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.PRELIMINARY)) {
            queryForParts.append("and tmp.preliminary = 1 \n");
        }else if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.NOT_RECEIVED)) {
            if (bomSearch.getPreliminary() == Boolean.FALSE) {
                queryForParts.append("and ((tmp.module_status IS NULL) or (tmp.preliminary = 1)) \n");
            }  else {
                queryForParts.append("and (tmp.module_status IS NULL) \n");
            }
        }
        if (!bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.ALL_STRING) && bomSearch.getPreliminary() == Boolean.FALSE && !bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.NOT_RECEIVED) && !bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.PRELIMINARY)) {
            queryForParts.append("AND tmp.preliminary != 1 \n");
        }
        if (bomSearch.getIsCertified()!= null && bomSearch.getIsCertified()) {
            queryForParts.append(" and tmp.cert_attached='S' \n");
        }
        boolean referencePN = bomSearch.getReferencePN() != null && bomSearch.getReferencePN();
        boolean referencedPN = bomSearch.getReferencedPN() != null && bomSearch.getReferencedPN();

        if (referencePN && referencedPN) {
            queryForParts.append("and tmp.reference_flag IN ('R', 'D') \n");
        } else {
            if (referencePN) {
                queryForParts.append("and tmp.reference_flag = 'R' \n");
            }
            if (referencedPN) {
                queryForParts.append("and tmp.reference_flag = 'D' \n");
            }
        }
        logger.info("Retrieving Part number details query : {} ", queryForParts);
        //return namedJdbcTemplate.query(queryForParts.toString(), new PartNumberResultRowMapper());
        List<BomPartNumber> partNumbers = namedJdbcTemplate.query(queryForParts.toString(), new PartNumberResultRowMapper());
        // Process part numbers based on the preliminary flag
        partNumbers = processPartNumbers(partNumbers, bomSearch);
        return partNumbers;
    }

    public List<BomPartNumber> processPartNumbers(List<BomPartNumber> partNumbers, BomDetailSearchCriteria bomSearch) {
        for (BomPartNumber partNumber : partNumbers) {
            if (partNumber.getPreliminary() != null) {
                if (partNumber.getPreliminary().equalsIgnoreCase("1") && bomSearch.getPreliminary() == Boolean.FALSE) {
                    partNumber.setMdsStatus(BomConstants.NOT_RECEIVED);
                    partNumber.setMdsNodeId(BomConstants.ZERO);
                }
            }
        }
        return partNumbers;
    }

    private void filterConditions(BomDetailSearchCriteria bomSearch, StringBuilder queryForParts) {
        if (bomSearch.getType().equalsIgnoreCase(BomConstants.MAKE)) {
            queryForParts.append("and pa.make_or_buy_flag = 'I' \n");
        } else if (bomSearch.getType().equalsIgnoreCase(BomConstants.BUY)) {
            queryForParts.append("and pa.make_or_buy_flag = 'E' \n");
        }
        if (bomSearch.getPartNumber() != null && !bomSearch.getPartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            queryForParts.append("and bom.part_number like '%").append(bomSearch.getPartNumber()).append("%' \n");
        }
        if (bomSearch.getMdsType() != null && !bomSearch.getMdsType().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            String type = getMdsTypeValue(bomSearch.getMdsType());
            if (type != null) {
                queryForParts.append("and pa.mds_type = '").append(type).append("' \n");
            }
        }
        if(bomSearch.getBomDescription() != null && !bomSearch.getBomDescription().equalsIgnoreCase(BomConstants.EMPTY_STRING)){
            queryForParts.append("and bom.description like '").append(bomSearch.getBomDescription()).append("'\n");
        }
        if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.ACCEPTED)){
            queryForParts.append("and pa.module_status = 'A' \n");
        } else if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.REJECTED)){
            queryForParts.append("and pa.module_status = 'R' \n");
        } else if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.IN_PROCESS_AT_RECIPIENT)) {
            queryForParts.append("and (pa.module_status = 'N'  or pa.module_status = 'B') \n");
//        } else if (bomSearch.getMdsStatus().equalsIgnoreCase(BomConstants.CANCELLED)) {
//            queryForParts.append("and pa.module_status = 'C' \n");
        }
        if (bomSearch.getPreliminary() != null && bomSearch.getPreliminary() == Boolean.TRUE) {
            queryForParts.append("and (pa.preliminary = 0 or pa.preliminary = 1) \n");
        }
    }

    private String getMdsTypeValue(String mdsType) {
        String type = null;
        if (mdsType.equalsIgnoreCase("Component")) {
            type = "C";
        } else if (mdsType.equalsIgnoreCase("Material")) {
            type = "M";
        } else if (mdsType.equalsIgnoreCase("Semicomponent")) {
            type = "L";
        }
        return type;
    }

    public List<BomMdsData> getMdsDetailsForPartNumber(BomDetailInput bomDetailInput) {
        logger.info("Retrieving the MDS details for the selected part number : {}", bomDetailInput.getPartNumber());
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<BomMdsData> mdsDataList = getMdsDetailsForComponent(bomDetailInput, namedJdbcTemplate);
        if (mdsDataList.isEmpty()){
            mdsDataList = getMdsDetailsForSemicomponent(bomDetailInput, namedJdbcTemplate);
            if (mdsDataList.isEmpty()) {
                mdsDataList = getMdsDetailsForMaterial(bomDetailInput, namedJdbcTemplate);
            }
        }
        List<BomMdsData> result = new ArrayList<>();
        if (!mdsDataList.isEmpty()) {
            BomMdsData bomMdsData = mdsDataList.get(0);
            result.add(bomMdsData);
        }
        return result;
    }

    private List<BomMdsData> getMdsDetailsForMaterial(BomDetailInput bomDetailInput, NamedParameterJdbcTemplate namedJdbcTemplate) {
        List<BomMdsData> mdsDataList;
        // retrieve Material details
        StringBuilder queryForMaterialMDS = new StringBuilder();
        queryForMaterialMDS.append("SELECT distinct m.node_id, mi.version, mi.create_date, mc.org_unit_id, comp.company_name, bo.weight, bo.unit_of_measure, 0, 0, mi.module_id, '', \n")
                .append("ml.name, rs.information_for_mds_supplier, rs.recipient_status, bo.preliminary, bm.threer_status, IF(bo.reference_partnumber IS NULL OR bo.reference_partnumber = '', bo.part_number, trim(leading '0' from bo.reference_partnumber)) AS reference_partnumber \n")
                .append("FROM material m \n")
                .append("INNER JOIN part_number_mds_latest_association pa on pa.top_level_node_id = m.node_id \n")
                .append("INNER JOIN v_bom_mds_association bo ON pa.part_number = IF(bo.reference_partnumber is null or bo.reference_partnumber = '', bo.part_number, trim(leading '0' from bo.reference_partnumber)) \n")
                .append("INNER JOIN bom_model bm ON (bo.bom_model_product_number = bm.product_number and bo.bom_model_release_number = bm.release_number) \n")
                .append("INNER JOIN module_information mi ON m.node_id = mi.top_level_node_id \n")
                .append("INNER JOIN mdb_common mc ON (mi.module_id = mc.module_id and mi.version = mc.version) \n")
                .append("INNER JOIN recipient_spec rs ON (mi.top_level_node_id = rs.top_level_node_id and rs.top_level_node_id = ").append(bomDetailInput.getComponentNode()).append(") \n")
                .append("LEFT JOIN company comp ON mc.company_id = comp.company_id \n")
                .append("LEFT JOIN multilingual ml ON m.node_id = ml.node_id \n")
                .append("WHERE m.node_id = '").append(bomDetailInput.getComponentNode()).append("' \n")
                .append("and bm.product_number = '").append(bomDetailInput.getProductNumber()).append("' \n")
                .append("and bm.release_number = '").append(bomDetailInput.getReleaseNumber()).append("' \n")
                //.append("and bo.part_number = '").append(bomDetailInput.getPartNumber()).append("' \n")
                .append("order by mi.version desc\n");
        logger.info("Retrieving MDS details Material query : {} ", queryForMaterialMDS);
        mdsDataList = namedJdbcTemplate.query(queryForMaterialMDS.toString(), new MdsResultRowMapper());
        return mdsDataList;
    }

    private List<BomMdsData> getMdsDetailsForSemicomponent(BomDetailInput bomDetailInput, NamedParameterJdbcTemplate namedJdbcTemplate) {
        List<BomMdsData> mdsDataList;
        // retrieve semi component details
        StringBuilder queryForSemiComponentMDS = new StringBuilder();
        queryForSemiComponentMDS.append("SELECT distinct sc.node_id, mi.version, mi.create_date, mc.org_unit_id, comp.company_name, bo.weight, bo.unit_of_measure, 0, 0, mi.module_id, '', \n")
                .append("sc.article_name, rs.information_for_mds_supplier, rs.recipient_status, bo.preliminary, bm.threer_status, IF(bo.reference_partnumber IS NULL OR bo.reference_partnumber = '', bo.part_number, trim(leading '0' from bo.reference_partnumber)) AS reference_partnumber \n")
                .append("FROM semi_component sc \n")
                .append("INNER JOIN part_number_mds_latest_association pa on pa.top_level_node_id = sc.node_id \n")
                .append("INNER JOIN v_bom_mds_association bo ON pa.part_number = IF(bo.reference_partnumber is null or bo.reference_partnumber = '', bo.part_number, trim(leading '0' from bo.reference_partnumber)) \n")
                .append("INNER JOIN bom_model bm ON (bo.bom_model_product_number = bm.product_number and bo.bom_model_release_number = bm.release_number) \n")
                .append("INNER JOIN module_information mi ON sc.node_id = mi.top_level_node_id \n")
                .append("INNER JOIN mdb_common mc ON (mi.module_id = mc.module_id and mi.version = mc.version) \n")
                .append("INNER JOIN recipient_spec rs ON (mi.top_level_node_id = rs.top_level_node_id and rs.top_level_node_id = ").append(bomDetailInput.getComponentNode()).append(") \n")
                .append("LEFT JOIN company comp ON mc.company_id = comp.company_id \n")
                .append("WHERE sc.node_id = '").append(bomDetailInput.getComponentNode()).append("' \n")
                .append("and bm.product_number = '").append(bomDetailInput.getProductNumber()).append("' \n")
                .append("and bm.release_number = '").append(bomDetailInput.getReleaseNumber()).append("' \n")
                //.append("and bo.part_number = '").append(bomDetailInput.getPartNumber()).append("' \n")
                .append("order by mi.version desc");
        logger.info("Retrieving MDS details for semi componenet query : {} ", queryForSemiComponentMDS);
        mdsDataList = namedJdbcTemplate.query(queryForSemiComponentMDS.toString(), new MdsResultRowMapper());
        return mdsDataList;
    }

    private List<BomMdsData> getMdsDetailsForComponent(BomDetailInput bomDetailInput, NamedParameterJdbcTemplate namedJdbcTemplate) {
        StringBuilder queryForMds = new StringBuilder();
        queryForMds.append("SELECT distinct c.node_id, mi.version, mi.create_date, mc.org_unit_id, ")
                .append("comp.company_name, bo.weight, bo.unit_of_measure, c.measured_weight, c.calculated_weight, mi.module_id, c.weighted_unit, \n")
                .append("c.description, rs.information_for_mds_supplier, rs.recipient_status, bo.preliminary, bm.threer_status, IF(bo.reference_partnumber IS NULL OR bo.reference_partnumber = '', bo.part_number, trim(leading '0' from bo.reference_partnumber)) AS reference_partnumber \n")
                .append("FROM component c \n")
                .append("INNER JOIN part_number_mds_latest_association pa on pa.top_level_node_id = c.node_id \n")
                .append("INNER JOIN v_bom_mds_association bo ON pa.part_number = IF(bo.reference_partnumber is null or bo.reference_partnumber = '', bo.part_number, trim(leading '0' from bo.reference_partnumber)) \n")
                .append("INNER JOIN bom_model bm ON (bo.bom_model_product_number = bm.product_number and bo.bom_model_release_number = bm.release_number) \n")
                .append("INNER JOIN module_information mi ON c.node_id = mi.top_level_node_id \n")
                .append("INNER JOIN mdb_common mc ON (mi.module_id = mc.module_id and mi.version = mc.version) \n")
                .append("INNER JOIN recipient_spec rs ON (mi.top_level_node_id = rs.top_level_node_id and rs.top_level_node_id = ").append(bomDetailInput.getComponentNode()).append(") \n")
                .append("LEFT JOIN company comp ON mc.company_id = comp.company_id \n")
                .append("WHERE c.node_id = '").append(bomDetailInput.getComponentNode()).append("' \n")
                .append("and bm.product_number = '").append(bomDetailInput.getProductNumber()).append("' \n")
                .append("and bm.release_number = '").append(bomDetailInput.getReleaseNumber()).append("' \n")
                //.append("and bo.part_number = '").append(bomDetailInput.getPartNumber()).append("' \n")
                .append("order by mi.version desc \n");
        logger.info("Retrieving MDS details query : {} ", queryForMds);
        return namedJdbcTemplate.query(queryForMds.toString(), new MdsResultRowMapper());
    }

    @Transactional
    public List<BomMaterialData> getMaterialListForPartNumber(BomDetailInput bomDetailInput) {
        logger.info("Retrieving the Material details for the selected part number : {}", bomDetailInput.getPartNumber());
        List<BomMaterialData> bomMaterialDataList;
        StringBuilder queryForMaterial = new StringBuilder();
        queryForMaterial.append("select distinct m.node_id, ml.name, mc.material_classification_code, cn.material_classification_name \n")
                .append("from component c \n")
                .append("INNER JOIN rel_weight rw \n")
                .append("\tON c.node_id = rw.immediate_parent_node_id \n")
                .append("INNER JOIN material m \n")
                .append("\tON rw.node_id = m.node_id \n")
                .append("INNER JOIN multilingual ml \n")
                .append("\tON rw.node_id = ml.node_id \n")
                .append("LEFT JOIN material_classification mc \n")
                .append("\tON m.material_classification_code = mc.material_classification_code \n")
                .append("LEFT JOIN classification_name cn \n")
                .append("\tON (mc.material_classification_code = cn.material_classification_code \n")
                .append("\t\tAND cn.iso_language = '").append(bomDetailInput.getLang()).append("') \n")
                .append("where c.node_id = ").append(bomDetailInput.getComponentNode()).append(" \n")
                .append("and ml.iso_language = '").append(bomDetailInput.getLang()).append("' \n")
                .append("group by m.node_id, ml.name, mc.material_classification_code, cn.material_classification_name \n");
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        logger.info("Retrieving Material details query : {} ", queryForMaterial);
        bomMaterialDataList = namedJdbcTemplate.query(queryForMaterial.toString(), new MaterialResultRowMapper());
        if (bomMaterialDataList.isEmpty()) {
            logger.info("No data found on the component and relweight mapping.  So go for relamount mapping.");
            bomMaterialRepository.deleteByProductNumberAndReleaseNumberAndPartNumber(
                    bomDetailInput.getProductNumber(), bomDetailInput.getReleaseNumber(), bomDetailInput.getPartNumber()
            );
            getRelAmountData(bomDetailInput);  // retrieve all ra nodes for the component and store it in bom material table
            List<BomMaterial> bomMaterialList1 = bomMaterialRepository.findByProductNumberAndReleaseNumberAndPartNumber(
                    bomDetailInput.getProductNumber(), bomDetailInput.getReleaseNumber(), bomDetailInput.getPartNumber());
            for (BomMaterial bomMaterial : bomMaterialList1){
                retrieveMaterialData(bomMaterialDataList, bomMaterial);
            }
            logger.info("Material data retrieval done using component and relamount mapping.");
        }
        return bomMaterialDataList;
    }

    private void retrieveMaterialData(List<BomMaterialData> bomMaterialDataList, BomMaterial bomMaterial) {
        List<RelWeight> relWeightList = relWeightRepository.findByImmediateParentNodeIdOrderByVersionAsc(bomMaterial.getRaNodeId());
        Map<Long, BigDecimal> relWeightNodeMap = new HashMap<>();
        for (RelWeight relWeight : relWeightList){
            relWeightNodeMap.put(relWeight.getNodeId(), relWeight.getVersion());
        }
        for (Map.Entry<Long, BigDecimal> entry : relWeightNodeMap.entrySet()) {
            List<Material> materialList = materialRepository.findByNodeIdOrderByVersionDesc(entry.getKey());
            List<Multilingual> multilingualList = multilingualRepository.findByNodeIdAndIsoLanguageOrderByVersionDesc(entry.getKey(), "EN");
            if (materialList.isEmpty()){
                Map<Long, BigDecimal> relPercentageNodeMap = new HashMap<>();
                retrieveSemiComponentData(entry, relPercentageNodeMap);
                for (Map.Entry<Long, BigDecimal> entryRP : relPercentageNodeMap.entrySet()) {
                    materialList = materialRepository.findByNodeIdOrderByVersionDesc(entryRP.getKey());
                    multilingualList = multilingualRepository.findByNodeIdAndIsoLanguageOrderByVersionDesc(entryRP.getKey(), "EN");
                    buildMaterialData(bomMaterialDataList, materialList, multilingualList);
                }
            } else {
                buildMaterialData(bomMaterialDataList, materialList, multilingualList);
                List<RelPercentage> percentageList = relPercentageRepository.findByImmediateParentNodeIdOrderByNodeIdAscVersionAsc(entry.getKey());
                if (!percentageList.isEmpty()) {
                    Map<Long, BigDecimal> relPercentageNodeMap = new HashMap<>();
                    for (RelPercentage relPercentage : percentageList) {
                        relPercentageNodeMap.put(relPercentage.getNodeId(), relPercentage.getVersion());
                    }
                    for (Map.Entry<Long, BigDecimal> entry2 : relPercentageNodeMap.entrySet()) {
                        materialList = materialRepository.findByNodeIdOrderByVersionDesc(entry2.getKey());
                        multilingualList = multilingualRepository.findByNodeIdAndIsoLanguageOrderByVersionDesc(entry2.getKey(), "EN");
                        buildMaterialData(bomMaterialDataList, materialList, multilingualList);
                    }
                }
            }
        }
    }

    private void buildMaterialData(List<BomMaterialData> bomMaterialDataList, List<Material> materialList, List<Multilingual> multilingualList) {
        if (!materialList.isEmpty() && !multilingualList.isEmpty()){
            BomMaterialData bomMaterialData = new BomMaterialData();
            Material material = materialList.get(0);
            bomMaterialData.setMaterialNodeId(BigInteger.valueOf(material.getNodeId()));
            Multilingual multilingual = multilingualList.get(0);
            bomMaterialData.setMaterialName(multilingual.getName());
            retrieveMaterialClassificationData(bomMaterialData, material);
            bomMaterialDataList.add(bomMaterialData);
        }
    }

    private void retrieveSemiComponentData(Map.Entry<Long, BigDecimal> entry, Map<Long, BigDecimal> relPercentageNodeMap) {
        List<SemiComponent> semiComponentList = semiComponentRepository.findByNodeIdOrderByVersionDesc(entry.getKey());
        if (!semiComponentList.isEmpty()) {
            Long nodeId = semiComponentList.get(0).getNodeId();
            getListOfMaterialsForSemiComponent(relPercentageNodeMap, nodeId);
        }
    }

    private void retrieveMaterialClassificationData(BomMaterialData bomMaterialData, Material material) {
        List<MaterialClassification> materialClassificationList =
                materialClassificationRepository.findByMaterialClassificationCode(material.getMaterialClassificationCode());
        if (!materialClassificationList.isEmpty()){
            bomMaterialData.setClassificationCode(materialClassificationList.get(0).getMaterialClassificationCode());
            List<ClassificationName> classificationNameList =
                    classificationNameRepository.findByMaterialClassificationCode(
                            materialClassificationList.get(0).getMaterialClassificationCode());
            if (!classificationNameList.isEmpty()){
                bomMaterialData.setClassificationName(classificationNameList.get(0).getMaterialClassificationName());
            }
        }
    }

    private void getRelAmountData(BomDetailInput bomDetailInput) {
        getRaNodeIds(bomDetailInput);
        List<BomMaterial> bomMaterialList = bomMaterialRepository.findByRaFlag("No");
        do {
            for (BomMaterial bomMaterial : bomMaterialList) {
                bomDetailInput.setComponentNode(bomMaterial.getRaNodeId());
                getRaNodeIds(bomDetailInput);
                bomMaterial.setRaFlag("Yes");
                bomMaterialRepository.save(bomMaterial);
            }
            bomMaterialList = bomMaterialRepository.findByRaFlag("No");
        } while (!bomMaterialList.isEmpty());
    }

    private void getListOfMaterialsForSemiComponent(Map<Long, BigDecimal> relPercentageNodeMap, Long nodeId) {
        List<RelPercentage> relPercentageList = relPercentageRepository.findByImmediateParentNodeIdOrderByNodeIdAscVersionAsc(nodeId);
        for (RelPercentage relPercentage : relPercentageList) {
            relPercentageNodeMap.put(relPercentage.getNodeId(), relPercentage.getVersion());
        }
    }

    private void getRaNodeIds(BomDetailInput bomDetailInput) {
        List<RelAmount> relAmountList = relAmountRepository.findByImmediateParentNodeIdOrderBySequenceAscVersionAsc(Long.parseLong(bomDetailInput.getComponentNode().toString()));
        Map<Long, BigDecimal> relAmountNodeMap = new HashMap<>();
        for (RelAmount relAmount : relAmountList) {
            relAmountNodeMap.put(relAmount.getNodeId(), relAmount.getVersion());
        }
        for (Map.Entry<Long, BigDecimal> entry : relAmountNodeMap.entrySet()) {
            BomMaterial bomMaterial = new BomMaterial();
            bomMaterial.setProductNumber(bomDetailInput.getProductNumber());
            bomMaterial.setReleaseNumber(bomDetailInput.getReleaseNumber());
            bomMaterial.setPartNumber(bomDetailInput.getPartNumber());
            bomMaterial.setRaNodeId(entry.getKey());
            bomMaterial.setRaFlag("No");
            bomMaterialRepository.save(bomMaterial);
        }
    }

    public List<BomSubstanceData> getSubstanceListForMaterial(BomDetailInput bomDetailInput) {
        logger.info("Retrieving the Substance details for the selected Material : {}", bomDetailInput.getMaterialNode());
        List<BomSubstanceData> bomSubstanceDataList;
        StringBuilder queryForSubstanceRA = getSubstanceQueryWithRelAmount(bomDetailInput);
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        logger.info("Retrieving Substance details queryForSubstanceRA : {} ", queryForSubstanceRA);
        bomSubstanceDataList = namedJdbcTemplate.query(queryForSubstanceRA.toString(), new SubstanceResultRowMapper());
        retrieveApplicationDetails(bomDetailInput, bomSubstanceDataList);
        if (bomSubstanceDataList.isEmpty()) {
            StringBuilder queryForSubstanceRW = getSubstanceQueryWithRelWeight(bomDetailInput);
            NamedParameterJdbcTemplate namedJdbcTemplate2 = new NamedParameterJdbcTemplate(jdbcTemplate);
            logger.info("Retrieving Substance details queryForSubstanceRW : {} ", queryForSubstanceRW);
            bomSubstanceDataList = namedJdbcTemplate2.query(queryForSubstanceRW.toString(), new SubstanceResultRowMapper());
            retrieveApplicationDetails(bomDetailInput, bomSubstanceDataList);
        }
        if (bomSubstanceDataList.isEmpty()) {
            List<BomMaterial> bomMaterialList1 = bomMaterialRepository.findByProductNumberAndReleaseNumberAndPartNumber(
                    bomDetailInput.getProductNumber(), bomDetailInput.getReleaseNumber(), bomDetailInput.getPartNumber());
            for (BomMaterial bomMaterial : bomMaterialList1){
                List<RelWeight> relWeightList = relWeightRepository.findByImmediateParentNodeIdOrderByVersionAsc(bomMaterial.getRaNodeId());
                Map<Long, BigDecimal> relWeightNodeMap = new HashMap<>();
                for (RelWeight relWeight : relWeightList) {
                    relWeightNodeMap.put(relWeight.getNodeId(), relWeight.getVersion());
                }
                for (Map.Entry<Long, BigDecimal> entry : relWeightNodeMap.entrySet()) {
                    if (entry.getKey().longValue() == bomDetailInput.getMaterialNode().longValue()){
                        getRelPercentageData(bomSubstanceDataList, entry.getKey(), bomDetailInput);
                    }
                }
            }
        }
        return bomSubstanceDataList;
    }

    private static StringBuilder getSubstanceQueryWithRelWeight(BomDetailInput bomDetailInput) {
        StringBuilder queryForSubstanceRW = new StringBuilder();
        queryForSubstanceRW.append("select distinct s.cas_code, s.node_id, sy.synonym_name, rp.ingredient_value, s.gadsl_duty_to_declare, s.gadsl_is_prohibited, s.reach \n")
                .append("from component c, rel_weight rw, material m, multilingual ml, rel_percentage rp, substance s, synonym sy \n")
                .append("where c.node_id = rw.immediate_parent_node_id \n")
                .append("and rw.node_id = m.node_id \n")
                .append("and rw.node_id = ml.node_id \n")
                .append("and rw.node_id = rp.immediate_parent_node_id \n")
                .append("and rp.node_id = s.node_id \n")
                .append("and s.node_id = sy.substance_node_id \n")
                .append("and c.node_id = ").append(bomDetailInput.getComponentNode()).append(" \n")
                .append("and m.node_id = ").append(bomDetailInput.getMaterialNode()).append(" \n")
                .append("and ml.iso_language = '").append(bomDetailInput.getLang()).append("' \n")
                .append("and sy.iso_language = '").append(bomDetailInput.getLang()).append("' \n")
                .append("and sy.synonym_id = 0 \n")
                .append("group by s.cas_code, s.node_id, sy.synonym_name, rp.ingredient_value, s.gadsl_duty_to_declare, s.gadsl_is_prohibited, s.reach");
        return queryForSubstanceRW;
    }

    private void getRelPercentageData(List<BomSubstanceData> bomSubstanceDataList, Long materialNodeId, BomDetailInput bomDetailInput) {
        List<RelPercentage> relPercentageList = relPercentageRepository.findByImmediateParentNodeIdOrderByNodeIdAscVersionAsc(materialNodeId);

        Map<Long, BigDecimal> relPercentageNodeMap = new HashMap<>();
        Map<Long, RelPercentage> relPercentageDataMap = new HashMap<>();
        for (RelPercentage relPercentage : relPercentageList) {
            relPercentageNodeMap.put(relPercentage.getNodeId(), relPercentage.getVersion());
            relPercentageDataMap.put(relPercentage.getNodeId(), relPercentage);
        }
        for (Map.Entry<Long, BigDecimal> entry : relPercentageNodeMap.entrySet()) {
            BomSubstanceData bomSubstanceData = new BomSubstanceData();
            List<Substance> substanceList = substanceRepository.findByNodeId(entry.getKey());
            RelPercentage relPercentage = relPercentageDataMap.get(entry.getKey());
            if (!substanceList.isEmpty()) {
                getSubstanceData(relPercentage, bomSubstanceData, substanceList, bomDetailInput);
                getSubstanceApplicationData(relPercentage, bomSubstanceData);
                bomSubstanceDataList.add(bomSubstanceData);
            }
        }
    }

    private void getSubstanceData(RelPercentage relPercentage, BomSubstanceData bomSubstanceData, List<Substance> substanceList, BomDetailInput bomDetailInput) {
        Substance substance = substanceList.get(0);
        bomSubstanceData.setCasCode(substance.getCasCode());
        bomSubstanceData.setSubstanceNodeId(substance.getNodeId());
        bomSubstanceData.setPortion(relPercentage.getIngredientValue());
        StringBuilder category = getSubstanceCategory(substance);
        bomSubstanceData.setCategory(category.toString());
        List<Synonym> synonymList = synonymRepository.findBySubstanceNodeIdAndSynonymIdAndIsoLanguage(bomSubstanceData.getSubstanceNodeId(), 0, bomDetailInput.getLang());
        if (!synonymList.isEmpty()) {
            bomSubstanceData.setSubstanceName(synonymList.get(0).getSynonymName());
        }
    }

    private void getSubstanceApplicationData(RelPercentage relPercentage, BomSubstanceData bomSubstanceData) {
        List<SubstanceApplication> substanceApplicationList = substanceApplicationRepository.findBySubstanceApplicationId(relPercentage.getApplicationId());
        if (!substanceApplicationList.isEmpty()){
            SubstanceApplication substanceApplication = substanceApplicationList.get(0);
            bomSubstanceData.setApplicationId(substanceApplication.getSubstanceApplicationId());
            bomSubstanceData.setApplicationName(substanceApplication.getSubstanceApplicationText());
        }
    }

    private static StringBuilder getSubstanceCategory(Substance substance) {
        int dutyToDeclare = substance.getGadslDutyToDeclare();
        int prohibited = substance.getGadslIsProhibited();
        int reach = substance.getReach();
        StringBuilder category = new StringBuilder();
        if (dutyToDeclare == 1){
            category.append(BomConstants.CHAR_D);
        }
        if (prohibited == 1) {
            if (category.toString().length() > 0)
                category.append(BomConstants.STR_P_WITH_SLASH);
            else
                category.append(BomConstants.CHAR_P);
        }
        if (reach == 1){
            category.append(BomConstants.SVHC);
        }
        return category;
    }

    private static StringBuilder getSubstanceQueryWithRelAmount(BomDetailInput bomDetailInput) {
        StringBuilder queryForSubstanceRA = new StringBuilder();
        queryForSubstanceRA.append("select distinct s.cas_code, s.node_id, sy.synonym_name, rp.ingredient_value, s.gadsl_duty_to_declare, s.gadsl_is_prohibited, s.reach \n")
                .append("from component c, rel_amount ra, rel_weight rw, material m, multilingual ml, rel_percentage rp, substance s, synonym sy \n")
                .append("where c.node_id = ra.immediate_parent_node_id \n")
                .append("and ra.node_id = rw.immediate_parent_node_id \n")
                .append("and rw.node_id = m.node_id \n")
                .append("and rw.node_id = ml.node_id \n")
                .append("and rw.node_id = rp.immediate_parent_node_id \n")
                .append("and rp.node_id = s.node_id \n")
                .append("and s.node_id = sy.substance_node_id \n")
                .append("and c.node_id = ").append(bomDetailInput.getComponentNode()).append(" \n")
                .append("and m.node_id = ").append(bomDetailInput.getMaterialNode()).append(" \n")
                .append("and ml.iso_language = '").append(bomDetailInput.getLang()).append("' \n")
                .append("and sy.iso_language = '").append(bomDetailInput.getLang()).append("' \n")
                .append("and sy.synonym_id = 0 \n")
                .append("group by s.cas_code, s.node_id, sy.synonym_name, rp.ingredient_value, s.gadsl_duty_to_declare, s.gadsl_is_prohibited, s.reach");
        return queryForSubstanceRA;
    }

    private void retrieveApplicationDetails(BomDetailInput bomDetailInput, List<BomSubstanceData> bomSubstanceDataList) {
        for (BomSubstanceData bomSubstanceData : bomSubstanceDataList){
            logger.info("Retrieving the Application details for the selected Substance : {}", bomSubstanceData.getSubstanceNodeId());
            StringBuilder queryForApplication = new StringBuilder();
            queryForApplication.append("select distinct sa.substance_application_id, sa.substance_application_text \n")
                    .append("from component c, rel_weight rw, material m, multilingual ml, rel_percentage rp, substance s, synonym sy, substance_application sa \n")
                    .append("where c.node_id = rw.immediate_parent_node_id \n")
                    .append("and rw.node_id = m.node_id \n")
                    .append("and rw.node_id = ml.node_id \n")
                    .append("and rw.node_id = rp.immediate_parent_node_id \n")
                    .append("and rp.node_id = s.node_id \n")
                    .append("and s.node_id = sy.substance_node_id \n")
                    .append("and rp.application_id = sa.substance_application_id \n")
                    .append("and c.node_id = ").append(bomDetailInput.getComponentNode()).append("\n")
                    .append("and m.node_id = ").append(bomDetailInput.getMaterialNode()).append("\n")
                    .append("and s.node_id = ").append(bomSubstanceData.getSubstanceNodeId()).append("\n")
                    .append("and ml.iso_language = '").append(bomDetailInput.getLang()).append("' \n")
                    .append("and sy.iso_language = '").append(bomDetailInput.getLang()).append("' \n")
                    .append("and sy.synonym_id = 0 \n")
                    .append("group by sa.substance_application_id, sa.substance_application_text");
            NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            logger.info("Retrieving Application details query : {} ", queryForApplication);
            List<BomApplicationData> bomApplicationDataList = namedJdbcTemplate.query(queryForApplication.toString(), new ApplicationResultRowMapper());
            for (BomApplicationData bomApplicationData : bomApplicationDataList){
                bomSubstanceData.setApplicationId(bomApplicationData.getApplicationId());
                bomSubstanceData.setApplicationName(bomApplicationData.getApplicationName());
            }
        }
    }

    public Message updateBomPartNumber(BomPartNumber bomPartNumber) {
        Message message = new Message();
        try {
            Optional<Bom> bomList = bomRepository.findById(bomPartNumber.getId());
            Bom bom;
            if (bomList.isPresent()) {
                bom = bomList.get();
                bom.setPartNumber(bomPartNumber.getPartNumber());
                bom.setDescription(bomPartNumber.getDescription());
                bom.setQuantity(bomPartNumber.getQuantity());
                bom.setWeight(bomPartNumber.getWeight());
                bom.setUnitOfMeasure(bomPartNumber.getUnitOfMeasure());
                bom.setMorb(bomPartNumber.getMorb());
                bom.setSupplierCode(bomPartNumber.getSupplierCode());
                bom.setSupplierDescription(bomPartNumber.getSupplierDescription());
                bom.setReferencePartNumber(bomPartNumber.getRefPartNumber());
                if (bom.getReferencePartNumber() == null || bom.getReferencePartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
                    bom.setReferenceFlag("");
                } else {
                    bom.setReferenceFlag("R");
                }
                bomRepository.save(bom);
                logger.info("Successfully updated the bom part numbers {} and its details", bomPartNumber.getPartNumber());
                message.setCode(BomConstants.NUMBER_1000);
                message.setInformation("Successfully updated the bom part numbers and its details : " + bomPartNumber.getPartNumber());
            } else {
                logger.error("The part number does not exists {}", bomPartNumber.getPartNumber());
                message.setCode(BomConstants.NUMBER_1002);
                message.setInformation("The part number does not exists : " + bomPartNumber.getPartNumber());
            }
        } catch (Exception e){
            logger.error("Error while updating the part Numbers {}", bomPartNumber.getPartNumber());
            message.setCode(BomConstants.NUMBER_1001);
            message.setInformation("Error while updating the part Numbers : " + bomPartNumber.getPartNumber());
        }
        return message;
    }

    public Message deleteBomPartNumber(int id, String partNumber) {
        Message message = new Message();
        try {
            Optional<Bom> bomList = bomRepository.findById(id);
            Bom bom;
            if (bomList.isPresent()) {
                bom = bomList.get();
                String productNumber = bom.getProductNumber();
                String releaseNumber = bom.getReleaseNumber();
                bomRepository.delete(bom);
                logger.info("Successfully deleted the bom part numbers {} and its details", partNumber);
                deleteThreeRPartNumber(bom);
                message.setCode(BomConstants.NUMBER_1000);
                message.setInformation("Successfully deleted the bom part numbers and its details : " + partNumber);
            } else {
                logger.error("The part number does not exists {}", partNumber);
                message.setCode(BomConstants.NUMBER_1002);
                message.setInformation("The part number does not exists : " + partNumber);
            }
        } catch (Exception e){
            logger.error("Error while deleting the part Numbers {}", partNumber);
            message.setCode(BomConstants.NUMBER_1001);
            message.setInformation("Error while deleting the part Numbers : " + partNumber);
        }
        return message;
    }



    public Message addBomPartNumber(BomPartNumber bomPartNumber) {
        Message message = new Message();
        try {
            if (bomPartNumber != null && bomPartNumber.getPartNumber() != null &&
                bomPartNumber.getDescription() != null && bomPartNumber.getQuantity() != null &&
                !bomPartNumber.getPartNumber().equalsIgnoreCase(BomConstants.EMPTY_STRING) &&
                    !bomPartNumber.getDescription().equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
//              List<Bom> savedParts = bomRepository.findByProductNumberAndReleaseNumberAndPartNumber(bomPartNumber.getProductNumber(),
//                    bomPartNumber.getReleaseNumber(), bomPartNumber.getPartNumber());
//              List<Bom> savedParts = bomRepository.findByPartNumber(bomPartNumber.getPartNumber());
                String part_number=bomPartNumber.getPartNumber();
                while(part_number.charAt(0)=='0')
                    part_number=part_number.substring(1);
                StringBuilder query=new StringBuilder("select * from bom where bom_model_product_number like '")
                        .append(bomPartNumber.getProductNumber()).append("' and bom_model_release_number like '")
                        .append(bomPartNumber.getReleaseNumber()).append("' and ")
                        .append("LTRIM(TRIM(LEADING '0' FROM part_number)) = '")
                        .append(part_number).append("'");
                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                logger.info("Query for part number not present : {} ",query);
                List<Bom> savedParts = namedJdbcTemplate.query(query.toString(), new BomRowMapper());
                if (savedParts.isEmpty()) {
                    Bom bom = new Bom();
                    bom.setPartNumber(trimLeadingZeroIfAny(bomPartNumber.getPartNumber()));
                    bom.setDescription(bomPartNumber.getDescription());
                    bom.setQuantity(bomPartNumber.getQuantity());
                    if(bomPartNumber.getWeight() == null) {
                        bom.setWeight(BigDecimal.ZERO);
                    } else {
                        bom.setWeight(bomPartNumber.getWeight());
                    }
                    bom.setUnitOfMeasure(bomPartNumber.getUnitOfMeasure());
                    bom.setMorb(bomPartNumber.getMorb() == null ? "" : bomPartNumber.getMorb());
                    bom.setSupplierCode(bomPartNumber.getSupplierCode());
                    bom.setSupplierDescription(bomPartNumber.getSupplierDescription());
                    bom.setProductNumber(bomPartNumber.getProductNumber());
                    bom.setReleaseNumber(bomPartNumber.getReleaseNumber());
                    bom.setReferencePartNumber(bomPartNumber.getRefPartNumber());
                    List<Bom> bomList = new ArrayList<>();
                    bomList.add(bom);
                    //mdsService.retrieveMdsData(bomList);
                    BomModel bomModel = new BomModel();
                    bomModel.setProductNumber(bomPartNumber.getProductNumber());
                    bomModel.setReleaseNumber(bomPartNumber.getReleaseNumber());
                    bomRepository.save(bom);
                    mdsService.retrieveReferencePNForBom(bomModel);

                    logger.info("Successfully added the bom part numbers {} and its details", bomPartNumber.getPartNumber());
                    addThreeRPartNumber(bom);
                    message.setCode(BomConstants.NUMBER_1000);
                    message.setInformation("Successfully added the bom part numbers and its details : " + bomPartNumber.getPartNumber());
                } else {
                    logger.error("Part number {} already present in BOM", bomPartNumber.getPartNumber());
                    message.setCode(BomConstants.NUMBER_1003);
                    message.setInformation(ALREADY_EXIST);
                    return message;
                }
            } else {
                logger.error(FIELDS_ARE_NULL);
                message.setCode(BomConstants.NUMBER_1001);
                message.setInformation(FIELDS_ARE_NULL);
                return message;
            }
        } catch (Exception e) {
            if (bomPartNumber != null && bomPartNumber.getPartNumber() != null) {
                logger.error("Error while adding the part  Numbers {}", bomPartNumber.getPartNumber());
                message.setInformation("Error while adding the part Numbers : " + bomPartNumber.getPartNumber());
            }
            else {
                logger.error(FIELDS_ARE_NULL);
                message.setInformation(FIELDS_ARE_NULL);
            }
            message.setCode(BomConstants.NUMBER_1001);
        }
        return message;
    }

    private String trimLeadingZeroIfAny(String partNumber) {
        while(partNumber.charAt(0) == '0') {
            partNumber = partNumber.substring(1);
        }
        return partNumber;
    }

    private void deleteThreeRPartNumber(Bom bomPartNumber) {
        BomModel bomModel = bomModelRepository.findByProductNumberAndReleaseNumber(bomPartNumber.getProductNumber(),bomPartNumber.getReleaseNumber());
        Optional<ThreeRProduct> productOptional = threeRProductRepository.findByBomModelIdAndPartNumber(bomModel.getId(), bomPartNumber.getPartNumber());
        if (productOptional.isPresent()) {
            List<Bom3RMaterial> materials = bom3RMaterialRepository.findByBomModelId(bomModel.getId());
            if (materials!=null && !materials.isEmpty()) {
                bom3RMaterialRepository.deleteAll(materials);
            }
            ThreeRProduct threeRProduct = productOptional.get();
            threeRProductRepository.delete(threeRProduct);
        }
    }

    private void addThreeRPartNumber(Bom bomPartNumber) {
        BomModel bomModel = bomModelRepository.findByProductNumberAndReleaseNumber(bomPartNumber.getProductNumber(),bomPartNumber.getReleaseNumber());
        List<ThreeRProduct> threeRProductList = threeRProductRepository.findByBomModelId(bomModel.getId());
        if(!threeRProductList.isEmpty()) {
            Optional<ThreeRProduct> threeRProductOptional = threeRProductList.stream().filter(threeRPrd -> threeRPrd.getPartNumber().equalsIgnoreCase(bomPartNumber.getPartNumber())).findFirst();
            if(!threeRProductOptional.isPresent()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                RestTemplate restTemplate = new RestTemplate();
                Map<String, Object> requestData = new HashMap<>();
                requestData.put("productNumber", bomPartNumber.getProductNumber() );

                requestData.put("releaseNumber", bomPartNumber.getReleaseNumber());
                requestData.put("partNumber", bomPartNumber.getPartNumber());
                // Create the HttpEntity with headers and request body
                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
                String apiUrl = env.getProperty("gateway.threer-api");
                if (apiUrl == null || apiUrl.isEmpty())
                    apiUrl = "http://localhost:8095/macs/3r";
                String apiTemplateUrl = apiUrl+"/addPartNumber";
                String response = restTemplate.postForObject(apiTemplateUrl, requestEntity, String.class);
                logger.info(response);
            }
        }
    }

    public List<BomMaterialData> getMaterialList(BomDetailInput bomDetailInput) {
        logger.info("Retrieving the Material details for the selected part number : {}", bomDetailInput.getPartNumber());
        List<BomDetailMaterialData> bomDetailMaterialDataList;
        StringBuilder queryForMaterial = new StringBuilder();
        queryForMaterial.append("select distinct mds.material_node_id, ml.name, mds.mt_weight, mds.material_classification_code, cn.material_classification_name, \n")
                .append("mds.semi_comp_node_id, sc.article_name, mds.sc_weight, mat_immediate_parent_node_id, sc_ingredient_value, mt_ingredient_value, \n")
                .append("ra_levels, sc_levels, sc_sequence, mt_levels, mt_sequence \n")
                .append("from  mds_consolidated_details mds \n")
                .append("LEFT JOIN multilingual ml on (ml.node_id = mds.material_node_id and ml.iso_language = '").append(bomDetailInput.getLang()).append("') \n")
                .append("LEFT JOIN semi_component sc on sc.node_id = mds.semi_comp_node_id \n")
                .append("LEFT JOIN classification_name cn on (mds.material_classification_code = cn.material_classification_code \n")
                .append("\tand cn.iso_language = '").append(bomDetailInput.getLang()).append("') \n")
                .append("where mds.comp_node_id = ").append(bomDetailInput.getComponentNode()).append(" \n")
                .append("and ml.name <> '' \n")
                .append("order by ra_levels, sc_levels, sc_sequence, mt_levels, mt_sequence \n");
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        logger.info("Retrieving Material details query : {} ", queryForMaterial);
        bomDetailMaterialDataList = namedJdbcTemplate.query(queryForMaterial.toString(), new MaterialDetailResultRowMapper());
        if (bomDetailMaterialDataList.isEmpty()) {
            StringBuilder queryForMaterialSemi = new StringBuilder();
            queryForMaterial.append("select distinct mds.material_node_id, ml.name, mds.mt_weight, mds.material_classification_code, cn.material_classification_name, \n")
                    .append("mds.semi_comp_node_id, sc.article_name, mds.sc_weight, mat_immediate_parent_node_id, sc_ingredient_value, mt_ingredient_value, \n")
                    .append("ra_levels, sc_levels, sc_sequence, mt_levels, mt_sequence \n")
                    .append("from  mds_consolidated_details mds \n")
                    .append("LEFT JOIN multilingual ml on (ml.node_id = mds.material_node_id and ml.iso_language = '").append(bomDetailInput.getLang()).append("') \n")
                    .append("LEFT JOIN semi_component sc on sc.node_id = mds.semi_comp_node_id \n")
                    .append("LEFT JOIN classification_name cn on (mds.material_classification_code = cn.material_classification_code \n")
                    .append("\tand cn.iso_language = '").append(bomDetailInput.getLang()).append("') \n")
                    .append("where mds.semi_comp_node_id = ").append(bomDetailInput.getComponentNode()).append(" \n")
                    .append("and ml.name <> '' \n")
                    .append("order by ra_levels, sc_levels, sc_sequence, mt_levels, mt_sequence \n");
            NamedParameterJdbcTemplate namedJdbcTemplate2 = new NamedParameterJdbcTemplate(jdbcTemplate);
            logger.info("Retrieving Material details (Semicomponent) query : {} ", queryForMaterialSemi);
            bomDetailMaterialDataList = namedJdbcTemplate2.query(queryForMaterialSemi.toString(), new MaterialDetailResultRowMapper());
        }
        List<BomMaterialData> bomMaterialDataList = new ArrayList<>();
        for (BomDetailMaterialData bomDetailMaterialData : bomDetailMaterialDataList) {
            BomMaterialData bomMaterialData = new BomMaterialData();
            if (bomDetailMaterialData != null && bomDetailMaterialData.getMaterialWeight() != null){
                bomMaterialData.setMaterialNodeId(bomDetailMaterialData.getMaterialNodeId());
                bomMaterialData.setMaterialName(bomDetailMaterialData.getMaterialName());
                bomMaterialData.setClassificationCode(bomDetailMaterialData.getMaterialClassificationCode());
                bomMaterialData.setClassificationName(bomDetailMaterialData.getMaterialClassificationName());
                bomMaterialDataList.add(bomMaterialData);
            } else if (bomDetailMaterialData != null && bomDetailMaterialData.getSemiWeight() != null) {
                bomMaterialData.setMaterialNodeId(bomDetailMaterialData.getMaterialNodeId());
                bomMaterialData.setMaterialName(bomDetailMaterialData.getArticleName());
                bomMaterialData.setClassificationCode(bomDetailMaterialData.getMaterialClassificationCode());
                bomMaterialData.setClassificationName(bomDetailMaterialData.getMaterialClassificationName());
                bomMaterialDataList.add(bomMaterialData);
            } else if (bomDetailMaterialData != null) {
                bomMaterialData.setMaterialNodeId(bomDetailMaterialData.getMaterialNodeId());
                bomMaterialData.setMaterialName(bomDetailMaterialData.getMaterialName());
                bomMaterialData.setClassificationCode(bomDetailMaterialData.getMaterialClassificationCode());
                bomMaterialData.setClassificationName(bomDetailMaterialData.getMaterialClassificationName());
                bomMaterialDataList.add(bomMaterialData);
            }
        }
        return bomMaterialDataList;
    }

    public List<BomSubstanceData> getSubstanceList(BomDetailInput bomDetailInput) {
        logger.info("Retrieving the Substance details for the selected Material : {}", bomDetailInput.getMaterialNode());
        String queryForFindMaterialNodes = "select distinct material_node_id from mds_consolidated_details \n" +
                "where mat_immediate_parent_node_id = " + bomDetailInput.getMaterialNode() + " \n" +
                "and comp_node_id = " + bomDetailInput.getComponentNode() + " \n";
        NamedParameterJdbcTemplate namedJdbcTemplate2 = new NamedParameterJdbcTemplate(jdbcTemplate);
        logger.info("Retrieving child node ids for the selected node : {} ", queryForFindMaterialNodes);
        List<MaterialNodeId> materialNodeIdList = namedJdbcTemplate2.query(queryForFindMaterialNodes, new MaterialsNodeIdRowMapper());
        String listOfMaterialNodes = new String();
        if (materialNodeIdList.size() > 0){
            listOfMaterialNodes = getListOfNodes(materialNodeIdList);
        } else {
            listOfMaterialNodes = String.valueOf(bomDetailInput.getMaterialNode());
        }
        StringBuilder queryForSubstance = new StringBuilder();
        queryForSubstance.append("select distinct mds.cas_code, mds.substance_node_id, sy.synonym_name, mds.sub_ingredient_value, \n")
                .append("mds.gadsl_duty_to_declare, mds.gadsl_is_prohibited, mds.reach, sp.substance_application_id, sp.substance_application_text \n")
                .append("from mds_consolidated_details mds \n")
                .append("LEFT JOIN synonym sy on sy.substance_node_id = mds.substance_node_id \n")
                .append("LEFT JOIN substance_application sp on sp.substance_application_id = mds.rp_application_id and deleted = 0\n")
                .append("where mds.material_node_id in (").append(listOfMaterialNodes).append(") \n")
                .append("and sy.iso_language = 'EN' and sy.synonym_id = 0 \n")
                .append("order by mds.cas_code, mds.substance_node_id, sy.synonym_name, mds.sub_ingredient_value, mds.gadsl_duty_to_declare, mds.gadsl_is_prohibited, mds.reach ");
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        logger.info("Retrieving the Substance details for the selected Material query : {} ", queryForSubstance.toString());
        return namedJdbcTemplate.query(queryForSubstance.toString(), new SubstanceResultRowMapper());
    }

    private String getListOfNodes(List<MaterialNodeId> materialNodeIds) {
        StringBuilder listOfNodes = new StringBuilder();
        for (MaterialNodeId materialNodeId : materialNodeIds) {
            Long nodeId = materialNodeId.getMaterialNodeId();
            listOfNodes.append(nodeId).append(",");
        }
        String nodeIds = listOfNodes.toString();
        nodeIds = nodeIds.substring(0,nodeIds.length() -  1);
        return nodeIds;
    }

    public Collection<MdsStatusBom> getBomDetailMdsStatus() {
        String queryForMdsStatus = "SELECT status_name FROM mds_status_bom where id not in (1,6)";
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedJdbcTemplate.query(queryForMdsStatus, new MdsStatusResultRowMapper());
    }
}
