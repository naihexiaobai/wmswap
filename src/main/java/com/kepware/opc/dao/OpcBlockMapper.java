package com.kepware.opc.dao;

import com.kepware.opc.entity.OpcBlock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("opcBlockMapper")
public interface OpcBlockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpcBlock record);

    int insertSelective(OpcBlock record);

    List<OpcBlock> selectAll();

    int clearKeyByKey(String mckey);

    /**
     * 清除key,两条sql语句
     * @param key
     * @return
     */
    int updateByKey(String key);

    OpcBlock selectByMcKey(String mcKey);

    OpcBlock selectByBlockNo(String blockNo);

    OpcBlock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpcBlock record);

    int updateByPrimaryKey(OpcBlock record);
}