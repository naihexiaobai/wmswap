package com.kepware.opc.dao;

import com.kepware.opc.entity.OpcOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("opcOrderMapper")
public interface OpcOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpcOrder record);

    OpcOrder selectByOrderKey(String orderKey);

    List<OpcOrder> selectOpcOrderListAll();

    List<OpcOrder> selectOpcOrderListByStatus(Integer status);

    int insertSelective(OpcOrder record);

    OpcOrder selectByPrimaryKey(Integer id);

    OpcOrder selectByStatus(Integer integer);

    int updateByPrimaryKeySelective(OpcOrder record);

    int updateByPrimaryKey(OpcOrder record);
}