package com.thief.wcs.dao;

import com.thief.wcs.entity.MessageLog;
import org.springframework.stereotype.Repository;

@Repository("messageLogMapper")
public interface MessageLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageLog record);

    int insertSelective(MessageLog record);

    MessageLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageLog record);

    int updateByPrimaryKey(MessageLog record);
}