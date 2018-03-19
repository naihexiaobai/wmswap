package com.thief.wcs.dao.impl;

import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.entity.Plc;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther CalmLake
 * @create 2018/3/14  13:57
 */
@Service("plcMapperImpl")
public class PlcMapperImpl implements PlcMapper {
    @Resource(name = "plcMapper")
    PlcMapper plcMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return plcMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Plc record) {
        return plcMapper.insert(record);
    }

    @Override
    public int insertSelective(Plc record) {
        return plcMapper.insertSelective(record);
    }

    @Override
    public Plc selectByPrimaryKey(Integer id) {
        return plcMapper.selectByPrimaryKey(id);
    }

    @Override
    public Plc selectByPlcName(String plcName) {
        return plcMapper.selectByPlcName(plcName);
    }

    @Override
    public List<Plc> selectByPlc(Plc plc) {
        return plcMapper.selectByPlc(plc);
    }

    @Override
    public int updateByPrimaryKeySelective(Plc record) {
        return plcMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Plc record) {
        return plcMapper.updateByPrimaryKey(record);
    }
}
