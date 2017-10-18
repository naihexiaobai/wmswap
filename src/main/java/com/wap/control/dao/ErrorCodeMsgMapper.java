package com.wap.control.dao;

import com.wap.model.ErrorCodeMsg;

public interface ErrorCodeMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErrorCodeMsg record);

    int insertSelective(ErrorCodeMsg record);

    ErrorCodeMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErrorCodeMsg record);

    int updateByPrimaryKey(ErrorCodeMsg record);
}