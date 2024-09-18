package com.dxc.macs.bom.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dxc.macs.bom.model.ComponentMdsType;

public class MdsTypeRowMapper implements RowMapper<ComponentMdsType> {

    @Override
    public ComponentMdsType mapRow(ResultSet rs, int rowNum) throws SQLException {
        ComponentMdsType compMDSType = new ComponentMdsType();
        compMDSType.setComponentType(rs.getString(1));
        compMDSType.setPartNumber(rs.getString(2));
        compMDSType.setNodeId(rs.getLong(3));
        compMDSType.setVersion(rs.getBigDecimal(4));
        compMDSType.setPreliminary(rs.getBoolean(5));
        compMDSType.setRecipientStatus(rs.getString(6));
        compMDSType.setTrimmedPartNumb(rs.getString(8));
        return compMDSType;
    }
}
