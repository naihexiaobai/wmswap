package com.wap.dao.daoImpl;

import com.wap.dao.WCSControlInfoMapper;
import com.wap.model.WCSControlInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * wcs消息体
 *
 * @auther CalmLake
 * @create 2017/11/24  11:55
 */
@Service("wCSControlInfoMapperImpl")
public class WCSControlInfoMapperImpl implements WCSControlInfoMapper {
    @Resource(name = "wCSControlInfoMapper")
    private WCSControlInfoMapper wCSControlInfoMapper;

    public int updateByStatusMachineIdTaskNO(WCSControlInfo record) {
        return wCSControlInfoMapper.updateByStatusMachineIdTaskNO(record);
    }

    public int deleteByPrimaryKey(Integer id) {
        return wCSControlInfoMapper.deleteByPrimaryKey(id);
    }

    public int insert(WCSControlInfo record) {
        return wCSControlInfoMapper.insert(record);
    }

    public int insertSelective(WCSControlInfo record) {
        return wCSControlInfoMapper.insertSelective(record);
    }

    public WCSControlInfo selectOneByWCSControlInfo(WCSControlInfo record) {
        return wCSControlInfoMapper.selectOneByWCSControlInfo(record);
    }

    public List<WCSControlInfo> selectByWCSControlInfo(WCSControlInfo record) {
        return wCSControlInfoMapper.selectByWCSControlInfo(record);
    }

    public WCSControlInfo selectByPrimaryKey(Integer id) {
        return wCSControlInfoMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(WCSControlInfo record) {
        return wCSControlInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(WCSControlInfo record) {
        return wCSControlInfoMapper.updateByPrimaryKey(record);
    }
}
