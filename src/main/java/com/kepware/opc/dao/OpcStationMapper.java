package com.kepware.opc.dao;

import com.kepware.opc.entity.OpcStation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("opcStationMapper")
public interface OpcStationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpcStation record);

    int insertSelective(OpcStation record);

    List<OpcStation> selectAll();

    OpcStation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpcStation record);

    int updateByPrimaryKey(OpcStation record);
}