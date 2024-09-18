package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.model.BomMaterialData;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialResultRowMapper implements RowMapper<BomMaterialData> {
    @Override
    public BomMaterialData mapRow(ResultSet rs, int rowNum) throws SQLException {
        BomMaterialData bomMaterialData = new BomMaterialData();
        bomMaterialData.setMaterialNodeId(BigInteger.valueOf(rs.getInt(1)));
        bomMaterialData.setMaterialName(rs.getString(2));
        bomMaterialData.setClassificationCode(rs.getString(3));
        bomMaterialData.setClassificationName(rs.getString(4));
        return bomMaterialData;
    }
}
