package com.wap.control.dao.daoImpl;

import com.wap.control.dao.StorageMapper;
import com.wap.model.Storage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 库位
 *
 * @auther CalmLake
 * @create 2017/11/24  11:57
 */
@Service("storageMapperImpl")
public class StorageMapperImpl implements StorageMapper {
    @Resource(name = "storageMapper")
    private StorageMapper storageMapper;

    public int deleteByPrimaryKey(Integer id) {
        return storageMapper.deleteByPrimaryKey(id);
    }

    public int insert(Storage record) {
        return storageMapper.insert(record);
    }

    public int insertSelective(Storage record) {
        return storageMapper.insertSelective(record);
    }

    public List<Storage> selectByStorage(Storage record) {
        return storageMapper.selectByStorage(record);
    }

    public Storage selectByPrimaryKey(Integer id) {
        return storageMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Storage record) {
        return storageMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Storage record) {
        return storageMapper.updateByPrimaryKey(record);
    }
}
