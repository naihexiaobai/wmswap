package com.wap.dao.daoImpl;

import com.wap.dao.CargoMapper;
import com.wap.model.Cargo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 货物
 *
 * @auther CalmLake
 * @create 2017/11/24  11:58
 */
@Service("cargoMapperImpl")
public class CargoMapperImpl implements CargoMapper {
    @Resource(name = "cargoMapper")
    private CargoMapper cargoMapper;

    public int deleteByPrimaryKey(Integer id) {
        return cargoMapper.deleteByPrimaryKey(id);
    }

    public int insert(Cargo record) {
        return cargoMapper.insert(record);
    }

    public int insertSelective(Cargo record) {
        return cargoMapper.insertSelective(record);
    }

    public List<Cargo> selectByCargo(Cargo record) {
        return cargoMapper.selectByCargo(record);
    }

    @Override
    public List<Cargo> selectAll() {
        return cargoMapper.selectAll();
    }

    @Override
    public Cargo selectByCargoStorageNo(String storageNo) {
        return cargoMapper.selectByCargoStorageNo(storageNo);
    }

    @Override
    public Cargo selectByCargoPalletNo(String palletNo) {
        return cargoMapper.selectByCargoPalletNo(palletNo);
    }

    public Cargo selectByPrimaryKey(Integer id) {
        return cargoMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Cargo record) {
        return cargoMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Cargo record) {
        return cargoMapper.updateByPrimaryKey(record);
    }
}
