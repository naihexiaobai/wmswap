package com.kepware.opc.dao;

import com.kepware.opc.entity.OpcWharf;
import org.springframework.stereotype.Repository;

@Repository("opcWharfMapper")
public interface OpcWharfMapper {
    int deleteByPrimaryKey(Integer id);

    OpcWharf selectByBlockNo(String blockNo);

    int insert(OpcWharf record);

    int insertSelective(OpcWharf record);

    OpcWharf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpcWharf record);

    int updateByPrimaryKey(OpcWharf record);
}