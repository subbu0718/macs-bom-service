package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.entity.MdsStatusBom;
import com.dxc.macs.bom.model.BomMdsData;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MdsStatusResultRowMapper implements RowMapper<MdsStatusBom> {
    @Override
    public MdsStatusBom mapRow(ResultSet rs, int rowNum) throws SQLException {
        MdsStatusBom mdsStatusBom = new MdsStatusBom();
        mdsStatusBom.setStatusName(rs.getString(1));
        return mdsStatusBom;
    }
}
