package com.wap.control.dao;

import com.wap.model.MachineInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("machineInfoMapper")
public interface MachineInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MachineInfo record);

    int insertSelective(MachineInfo record);

    List<MachineInfo> selectByMachineInfo(MachineInfo machineInfo);

    MachineInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MachineInfo record);

    int updateByPrimaryKey(MachineInfo record);
}