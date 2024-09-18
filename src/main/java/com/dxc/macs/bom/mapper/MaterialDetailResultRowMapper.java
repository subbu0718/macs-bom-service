package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.model.BomDetailMaterialData;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialDetailResultRowMapper implements RowMapper<BomDetailMaterialData> {
    @Override
    public BomDetailMaterialData mapRow(ResultSet rs, int rowNum) throws SQLException {
        BomDetailMaterialData bomDetailMaterialData = new BomDetailMaterialData();
        bomDetailMaterialData.setMaterialNodeId(BigInteger.valueOf(rs.getLong(1)));
        bomDetailMaterialData.setMaterialName(rs.getString(2));
        bomDetailMaterialData.setMaterialWeight(rs.getBigDecimal(3));
        bomDetailMaterialData.setMaterialClassificationCode(rs.getString(4));
        bomDetailMaterialData.setMaterialClassificationName(rs.getString(5));
        bomDetailMaterialData.setSemiComponentNodeId(rs.getLong(6));
        bomDetailMaterialData.setArticleName(rs.getString(7));
        bomDetailMaterialData.setSemiWeight(rs.getBigDecimal(8));
        bomDetailMaterialData.setMaterialImmediateParentId(rs.getLong(9));
        bomDetailMaterialData.setSemiWeightedMean(rs.getBigDecimal(10));
        bomDetailMaterialData.setMaterialWeightedMean(rs.getBigDecimal(11));
        return bomDetailMaterialData;
    }
}
