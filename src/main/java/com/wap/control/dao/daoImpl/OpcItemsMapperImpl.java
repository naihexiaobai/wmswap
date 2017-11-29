package com.wap.control.dao.daoImpl;

import com.wap.control.dao.OpcItemsMapper;
import com.wap.model.OpcItems;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * jdbc
 *
 * @auther CalmLake
 * @create 2017/11/21  11:02
 */
@Service("opcItemsMapperImpl")
public class OpcItemsMapperImpl implements OpcItemsMapper {
    @Resource(name = "opcItemsMapper")
    private OpcItemsMapper opcItemsMapper;

    public int deleteByPrimaryKey(Integer id) {
        return opcItemsMapper.deleteByPrimaryKey(id);
    }

    public int insert(OpcItems record) {
        return opcItemsMapper.insert(record);
    }

    public int insertSelective(OpcItems record) {
        return opcItemsMapper.insertSelective(record);
    }

    public List<OpcItems> selectByGroups() {
        return opcItemsMapper.selectByGroups();
    }

    public List<OpcItems> selectByOpcItems(OpcItems opcItems) {
        return opcItemsMapper.selectByOpcItems(opcItems);
    }

    public OpcItems selectByPrimaryKey(Integer id) {
        return opcItemsMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(OpcItems record) {
        return opcItemsMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(OpcItems record) {
        return opcItemsMapper.updateByPrimaryKey(record);
    }
}
