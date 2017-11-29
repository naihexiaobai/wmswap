package com.wap.control.dao;

import com.wap.model.WCSControlInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("wCSControlInfoMapper")
public interface WCSControlInfoMapper {

    int updateByStatusMachineIdTaskNO(WCSControlInfo record);

    int deleteByPrimaryKey(Integer id);

    int insert(WCSControlInfo record);

    int insertSelective(WCSControlInfo record);

    WCSControlInfo selectOneByWCSControlInfo(WCSControlInfo record);

    List<WCSControlInfo> selectByWCSControlInfo(WCSControlInfo record);

    WCSControlInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WCSControlInfo record);

    int updateByPrimaryKey(WCSControlInfo record);
}