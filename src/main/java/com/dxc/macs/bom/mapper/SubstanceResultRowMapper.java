package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.constants.BomConstants;
import com.dxc.macs.bom.model.BomSubstanceData;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubstanceResultRowMapper implements RowMapper<BomSubstanceData> {

    @Override
    public BomSubstanceData mapRow(ResultSet rs, int rowNum) throws SQLException {
        BomSubstanceData bomSubstanceData = new BomSubstanceData();
        bomSubstanceData.setCasCode(rs.getString(1));
        bomSubstanceData.setSubstanceNodeId(rs.getLong(2));
        bomSubstanceData.setSubstanceName(rs.getString(3));
        bomSubstanceData.setPortion(rs.getBigDecimal(4));
        int dutyToDeclare = rs.getInt(5);
        int prohibited = rs.getInt(6);
        int reach = rs.getInt(7);
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
        bomSubstanceData.setCategory(category.toString());
        bomSubstanceData.setApplicationId(rs.getLong(8));
        bomSubstanceData.setApplicationName(rs.getString(9));
        return bomSubstanceData;
    }
}
