package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.model.BomMdsData;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class MdsResultRowMapper implements RowMapper<BomMdsData> {
    @Override
    public BomMdsData mapRow(ResultSet rs, int rowNum) throws SQLException {
        BomMdsData bomMdsData = new BomMdsData();
        bomMdsData.setNodeId(BigInteger.valueOf(rs.getInt(1)));
        bomMdsData.setMdsId(BigInteger.valueOf(rs.getInt(10)));
        bomMdsData.setMdsVersion(rs.getBigDecimal(2));
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy h:mm a");
        if (rs.getDate(3) != null) {
            bomMdsData.setCreateDate(dateFormat.format(rs.getDate(3)));
        }
        bomMdsData.setSupplierCode(rs.getString(4));
        bomMdsData.setSupplierDescription(rs.getString(5));
        bomMdsData.setPartNumberWeight(rs.getBigDecimal(6));
        bomMdsData.setUnitOfMeasure(rs.getString(7));
        BigDecimal measuredWeight = rs.getBigDecimal(8);
        bomMdsData.setMeasuredWeight(measuredWeight);
        BigDecimal calculatedWeight = rs.getBigDecimal(9);
        bomMdsData.setCalculatedWeight(calculatedWeight);
        DecimalFormat df = new DecimalFormat("##0.000");
        BigDecimal deviation = new BigDecimal(0);
        if (calculatedWeight.doubleValue() > 0 && measuredWeight.doubleValue() > 0) {
            deviation = BigDecimal.valueOf((calculatedWeight.doubleValue() - measuredWeight.doubleValue()) / calculatedWeight.doubleValue() * 100);
        }
        bomMdsData.setDeviation(df.format(deviation));
        bomMdsData.setWeightedUnit(rs.getString(11));
        bomMdsData.setCompDescription(rs.getString(12));
        bomMdsData.setRejectReason(rs.getString(13));
        bomMdsData.setPartMdsStatus(rs.getString(14));
        bomMdsData.setPreliminary(rs.getString(15));
        bomMdsData.setThreeR(rs.getString(16));
        bomMdsData.setRefpartNumber(rs.getString(17));
        return bomMdsData;
    }
}
