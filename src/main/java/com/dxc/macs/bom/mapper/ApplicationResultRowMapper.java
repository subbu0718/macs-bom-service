package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.model.BomApplicationData;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationResultRowMapper implements RowMapper<BomApplicationData> {
    @Override
    public BomApplicationData mapRow(ResultSet rs, int rowNum) throws SQLException {
        BomApplicationData bomApplicationData = new BomApplicationData();
        bomApplicationData.setApplicationId(rs.getLong(1));
        bomApplicationData.setApplicationName(rs.getString(2));
        return bomApplicationData;
    }
}
