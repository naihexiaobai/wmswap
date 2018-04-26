package com.kepware.opc.dao;

import com.kepware.opc.entity.OpcItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("opcItemMapper")
public interface OpcItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpcItem record);

    int insertSelective(OpcItem record);

    List<OpcItem> selectAll();

    OpcItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpcItem record);

    int updateByPrimaryKey(OpcItem record);
}