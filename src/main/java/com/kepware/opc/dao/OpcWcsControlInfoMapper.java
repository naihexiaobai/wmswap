package com.kepware.opc.dao;

import com.kepware.opc.entity.OpcWcsControlInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("opcWcsControlInfoMapper")
public interface OpcWcsControlInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpcWcsControlInfo record);

    int insertSelective(OpcWcsControlInfo record);

    List<OpcWcsControlInfo> selectAll();

    OpcWcsControlInfo selectByPrimaryKey(Integer id);

    int updateByWcsTaskNoAndBlockNo(OpcWcsControlInfo record);

    int updateByPrimaryKeySelective(OpcWcsControlInfo record);

    int updateByPrimaryKey(OpcWcsControlInfo record);
}