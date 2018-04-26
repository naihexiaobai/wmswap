package com.thief.wcs.dao;

import com.thief.wcs.entity.RouteSite;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("routeSiteMapper")
public interface RouteSiteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RouteSite record);

    int insertSelective(RouteSite record);

    List<RouteSite> selectByRouteId(Integer id);

    RouteSite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RouteSite record);

    int updateByPrimaryKey(RouteSite record);
}