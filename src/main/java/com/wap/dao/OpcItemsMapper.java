package com.wap.dao;

import com.wap.model.OpcItems;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("opcItemsMapper")
public interface OpcItemsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpcItems record);

    int insertSelective(OpcItems record);

    List<OpcItems> selectByGroups();

    List<OpcItems> selectByOpcItems(OpcItems opcItems);

    OpcItems selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpcItems record);

    int updateByPrimaryKey(OpcItems record);
}