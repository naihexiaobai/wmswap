package com.wap.control.dao.daoImpl;

import com.wap.control.dao.MachineInfoMapper;
import com.wap.model.MachineInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * impl
 *
 * @auther CalmLake
 * @create 2017/11/21  15:09
 */
@Service("machineInfoMapperImpl")
public class MachineInfoMapperImpl implements MachineInfoMapper {
    @Resource(name = "machineInfoMapper")
    private MachineInfoMapper machineInfoMapper;

    public int deleteByPrimaryKey(Integer id) {
        return machineInfoMapper.deleteByPrimaryKey(id);
    }

    public int insert(MachineInfo record) {
        return machineInfoMapper.insert(record);
    }

    public int insertSelective(MachineInfo record) {
        return machineInfoMapper.insertSelective(record);
    }

    public List<MachineInfo> selectByMachineInfo(MachineInfo machineInfo) {
        return machineInfoMapper.selectByMachineInfo(machineInfo);
    }

    public MachineInfo selectByPrimaryKey(Integer id) {
        return machineInfoMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(MachineInfo record) {
        return machineInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(MachineInfo record) {
        return machineInfoMapper.updateByPrimaryKey(record);
    }
}
