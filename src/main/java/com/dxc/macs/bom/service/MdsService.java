package com.dxc.macs.bom.service;

import com.dxc.macs.bom.constants.BomConstants;
import com.dxc.macs.bom.entity.Bom;
import com.dxc.macs.bom.entity.BomModel;
import com.dxc.macs.bom.mapper.MdsTypeRowMapper;
import com.dxc.macs.bom.model.ComponentMdsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MdsService {

    private static final String PROC_FOR_RETRIEVE_REF_PN_BOM = "{ call retrieve_reference_pn_for_bom(?, ?) }";
    private final Logger logger = LoggerFactory.getLogger(MdsService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static void updateMdsStatus(Bom bom, ComponentMdsType comp) {
        bom.setMdsNodeId(comp.getNodeId());
        bom.setVersion(comp.getVersion());
        if (comp.getPartNumber()!= null) {
           bom.setPartNumber(trimLeadingZeroIfAny(comp.getPartNumber()));
        }
        if (Boolean.TRUE.equals(comp.getPreliminary())){
            bom.setPreliminary(BomConstants.YES);
        } else {
            bom.setPreliminary(BomConstants.NO);
        }
    }

    private static String trimLeadingZeroIfAny(String partNumber) {
        while(partNumber.charAt(0) == '0') {
            partNumber = partNumber.substring(1);
        }
        return partNumber;
    }

    public void retrieveMdsData(List<Bom> bomList) {
        String mdsQuery = "select case when mds_type = 'C' then 'Component' when mds_type = 'M' then 'Material' when mds_type = 'L' then 'Semicomponent' end mds_type, \n" +
                "part_number, top_level_node_id, version, preliminary, module_status, '', part_number \n" +
                "from part_number_mds_latest_association \n" +
                "where check_flag in (select param_value from config_param where param_name='MDS_FETCH_TYPE') \n" +
                "and LTRIM(TRIM(LEADING '0' FROM part_number )) in ( :partNumbers ) \n";
        List<String> partNumberList = bomList.stream().map(bom -> bom.getPartNumber()).collect(
            Collectors.toList());
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("partNumbers", partNumberList);
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        logger.info("Query for retrieving mds details from BOM upload :: {}",mdsQuery);
        List<ComponentMdsType> mdsTypes = namedJdbcTemplate.query(mdsQuery, params, new MdsTypeRowMapper());
        if (!mdsTypes.isEmpty()) {
            mapMdsDetailsToBom(mdsTypes, bomList);
        }
    }

    private void mapMdsDetailsToBom(List<ComponentMdsType> mdsTypes, List<Bom> bomList) {
        List<ComponentMdsType> componentMdsTypes = mdsTypes.stream().filter(componentMdsType -> componentMdsType.getComponentType().equalsIgnoreCase("Component")).collect(
            Collectors.toList());
        List<ComponentMdsType> semiCompMdsTypes = mdsTypes.stream().filter(componentMdsType -> componentMdsType.getComponentType().equalsIgnoreCase("Semicomponent")).collect(
            Collectors.toList());
        List<ComponentMdsType> materialMdsTypes = mdsTypes.stream().filter(componentMdsType -> componentMdsType.getComponentType().equalsIgnoreCase("Material")).collect(
            Collectors.toList());
        for (Bom bom:bomList) {
            List<ComponentMdsType> componentList = componentMdsTypes.stream().filter(componentMdsType -> componentMdsType.getTrimmedPartNumb().equalsIgnoreCase(bom.getPartNumber())).collect(
                Collectors.toList());
            validateComponent(componentList, bom, BomConstants.COMPONENT);
            if (bom.getMdsType() == null) {
                List<ComponentMdsType> semiComp = semiCompMdsTypes.stream().filter(componentMdsType -> componentMdsType.getTrimmedPartNumb().equalsIgnoreCase(bom.getPartNumber())).collect(
                    Collectors.toList());
                validateComponent(semiComp, bom, BomConstants.SEMICOMPONENT);
            }
            if (bom.getMdsType() == null) {
                List<ComponentMdsType> materialComp = materialMdsTypes.stream().filter(componentMdsType -> componentMdsType.getTrimmedPartNumb().equalsIgnoreCase(bom.getPartNumber())).collect(
                    Collectors.toList());
                validateComponent(materialComp, bom, BomConstants.MATERIAL);
            }
        }
    }

    private void validateComponent(List<ComponentMdsType> componentList, Bom bom, String mdsType) {
        if (componentList != null && componentList.size() == 1) {
            ComponentMdsType comp = componentList.get(0);
            bom.setMdsType(mdsType);
            logger.info("1 :: MDS Mapped for Part number {} using {}", bom.getPartNumber(), mdsType);
            updateMdsStatus(bom, comp);
        } else if (componentList != null && componentList.size() > 1) {
            bom.setMdsType(mdsType);
            ComponentMdsType selectedMds = null;
            Optional<ComponentMdsType> result = componentList.stream().filter(componentMdsType -> componentMdsType.getRecipientStatus().equalsIgnoreCase("A")).findFirst();
            if (result.isPresent()) {
                selectedMds = result.get();
            } else {
                Optional<ComponentMdsType> semiCompOpt = componentList.stream().filter(componentMdsType -> componentMdsType.getRecipientStatus().equalsIgnoreCase("R")).findFirst();
                selectedMds = semiCompOpt.orElseGet(() -> componentList.get(0));
            }
            logger.info("2 :: MDS Mapped for Part number {} using {}", bom.getPartNumber(), mdsType);
            updateMdsStatus(bom, selectedMds);
        }
    }

    public void retrieveReferencePNForBom(BomModel model) {
        logger.info("Retrieving of reference part numbers for the bom data not found in RS");
        jdbcTemplate.update(PROC_FOR_RETRIEVE_REF_PN_BOM, model.getProductNumber(), model.getReleaseNumber());
        logger.info("Completed Retrieving of reference part numbers for the bom data not found in RS");
    }
}
