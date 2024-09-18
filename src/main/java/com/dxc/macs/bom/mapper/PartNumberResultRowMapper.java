package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.constants.BomConstants;
import com.dxc.macs.bom.model.BomPartNumber;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartNumberResultRowMapper implements RowMapper<BomPartNumber> {
    @Override
    public BomPartNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
        BomPartNumber bomPartNumber = new BomPartNumber();
        bomPartNumber.setId(rs.getInt(1));
        bomPartNumber.setPartNumber(rs.getString(2));
        bomPartNumber.setDescription(rs.getString(3));
        bomPartNumber.setQuantity(rs.getBigDecimal(4));
        bomPartNumber.setMorb(rs.getString(5));
        bomPartNumber.setWeight(rs.getBigDecimal(6));
        bomPartNumber.setUnitOfMeasure(rs.getString(7));
        bomPartNumber.setSupplierCode(rs.getString(8));
        bomPartNumber.setSupplierDescription(rs.getString(9));
        bomPartNumber.setProductNumber(rs.getString(10));
        bomPartNumber.setReleaseNumber(rs.getString(11));
        bomPartNumber.setMdsType(rs.getString(12));
        bomPartNumber.setMdsNodeId(rs.getLong(13));
        bomPartNumber.setPreliminary(rs.getString(14));
        bomPartNumber.setRefPartNumber(rs.getString(16));
        bomPartNumber.setVersion(rs.getBigDecimal(17));
        bomPartNumber.setMdsStatus(getMDSStatus(rs.getString(15)));
        bomPartNumber.setMacsPartNumber(rs.getString(18));
        bomPartNumber.setCertAttached(rs.getString(19));
        bomPartNumber.setReferenceFlag(rs.getString(20));
        bomPartNumber.setSupplierEmailStatus(rs.getBoolean(21));
        String currentStatus = rs.getString(22);
        if (currentStatus != null) {
            bomPartNumber.setNotifiedStatus(rs.getString(22));
        } else {
            bomPartNumber.setNotifiedStatus(getMDSStatus(rs.getString(25)));
        }
        bomPartNumber.setNotifiedCount(rs.getInt(23));
        bomPartNumber.setNotifiedDate(rs.getDate(24));
        return bomPartNumber;
    }

    private String getMDSStatus(String status) {
        if (status != null) {
            if (status.equalsIgnoreCase(BomConstants.REJECTED_R)){
                return BomConstants.REJECTED;
            } else if (status.equalsIgnoreCase(BomConstants.ACCEPTED_A)) {
                return BomConstants.ACCEPTED;
            } else if (status.equalsIgnoreCase(BomConstants.NOT_BROWSED_N) || status.equalsIgnoreCase(BomConstants.BROWSED_B)) {
                return BomConstants.IN_PROCESS_AT_RECIPIENT;
            } else if (status.equalsIgnoreCase(BomConstants.EMPTY_STRING)) {
                return BomConstants.NOT_RECEIVED;
            }
        } else {
            return BomConstants.NOT_RECEIVED;
        }
        return BomConstants.EMPTY_STRING;
    }
}
