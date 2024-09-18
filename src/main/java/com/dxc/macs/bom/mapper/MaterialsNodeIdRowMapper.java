package com.dxc.macs.bom.mapper;

import com.dxc.macs.bom.model.MaterialNodeId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialsNodeIdRowMapper implements RowMapper<MaterialNodeId> {
    @Override
    public MaterialNodeId mapRow(ResultSet rs, int rowNum) throws SQLException {
        MaterialNodeId materialNodeId = new MaterialNodeId();
        materialNodeId.setMaterialNodeId(rs.getLong(1));
        return materialNodeId;
    }
}
