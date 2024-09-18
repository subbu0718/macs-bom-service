package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.entity.Bom;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BomRowMapper implements RowMapper<Bom> {

    @Override
    public Bom mapRow(ResultSet rs, int rowNum) throws SQLException {
        Bom bom = new Bom();
        bom.setId(rs.getInt(1));
        bom.setPartNumber(rs.getString(2));
        bom.setDescription(rs.getString(3));
        bom.setQuantity(rs.getBigDecimal(4));
        bom.setWeight(rs.getBigDecimal(5));
        bom.setUnitOfMeasure(rs.getString(6));
        bom.setMorb(rs.getString(7));
        bom.setSupplierCode(rs.getString(8));
        bom.setSupplierDescription(rs.getString(9));
        bom.setProductNumber(rs.getString(11));
        bom.setReleaseNumber(rs.getString(12));
        bom.setMdsType(rs.getString(13));
        bom.setMdsNodeId(rs.getLong(14));
        bom.setPreliminary(rs.getString(15));
        return bom;
    }
}
