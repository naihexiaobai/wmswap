package com.thief.wcs.dao;

import com.thief.wcs.entity.Plc;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("plcMapper")
public interface PlcMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Plc record);

    int insertSelective(Plc record);

    Plc selectByPrimaryKey(Integer id);

    Plc selectByPlcName(String plcName);

    List<Plc> selectByPlc(Plc plc);

    int updateByPrimaryKeySelective(Plc record);

    int updateByPrimaryKey(Plc record);
}