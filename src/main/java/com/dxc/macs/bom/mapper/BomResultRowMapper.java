package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.model.BomResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class BomResultRowMapper implements RowMapper<BomResult> {
    @Override
    public BomResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        BomResult bomResult = new BomResult();
        bomResult.setProductId(rs.getInt(1));
        bomResult.setProductNumber(rs.getString(2));
        bomResult.setDescription(rs.getString(3));
        bomResult.setReleaseNumber(rs.getString(4));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        if (rs.getDate(5) != null) {
            bomResult.setCreateDate(dateFormat.format(rs.getDate(5)).substring(0,10));
        }
        bomResult.setCreatedBy(rs.getString(6));
        bomResult.setShortDescription(rs.getString(7));
        bomResult.setMarket(rs.getString(8));
        bomResult.setBusinessUnit(rs.getString(9));
        bomResult.setStatus(rs.getString(10));
        bomResult.setLine(rs.getString(11));
        bomResult.setTaricCode(rs.getString(12));
        bomResult.setTaricForComponents(rs.getString(13));
        bomResult.setTotalPartNumbers(rs.getInt(14));
        bomResult.setSelfCertificateCount(rs.getInt(15));
        bomResult.setMdsAccepted(rs.getInt(16));
        bomResult.setMdsRejected(rs.getInt(17));
        bomResult.setMdsSuspended(rs.getInt(18));
        int missingMds = (bomResult.getTotalPartNumbers() - bomResult.getMdsAccepted()- bomResult.getMdsRejected() - bomResult.getMdsSuspended());
        bomResult.setMissingMds(missingMds);
        bomResult.setMdsPreliminary(rs.getInt(19));
        bomResult.setThreeR(rs.getString(20));
        bomResult.setBusinessUnitId(rs.getInt(21));
        bomResult.setMeasuredWeight(rs.getBigDecimal(22));
        bomResult.setMeasuredUnit(rs.getString(23));
        bomResult.setDossier(rs.getString(24));
        bomResult.setTotMeasuredWeight(rs.getBigDecimal(25));
        bomResult.setTotCalculatedWeight(rs.getBigDecimal(26));
        bomResult.setCdxJobId(rs.getInt(27));
        bomResult.setEchaId(rs.getLong(28));
        bomResult.setScipNumber(rs.getString(29));
        bomResult.setEchaUrl(rs.getString(30));
        bomResult.setScipValidationMessage(rs.getString(31));
        bomResult.setRegulation(rs.getString(32));
        return bomResult;
    }
}
