package com.kepware.opc.server;

import com.kepware.opc.entity.OpcBlock;
import com.thief.wcs.dao.RouteMapper;
import com.thief.wcs.dao.RouteSiteMapper;
import com.thief.wcs.entity.Route;
import com.thief.wcs.entity.RouteSite;
import com.www.util.SpringTool;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 路线数据
 */
public class RouteSingleton {

    private static HashMap<Integer, Route> routeHashMap = new HashMap<Integer, Route>();
    private static HashMap<Integer, RouteSite> routeSiteHashMap = new HashMap<Integer, RouteSite>();

    private static RouteSingleton ourInstance = new RouteSingleton();

    public static RouteSingleton getInstance() {
        return ourInstance;
    }

    private RouteSingleton() {
    }


    public void setHashMap() {
        List<Route> routeList = ((RouteMapper) SpringTool.getBeanByClass(RouteMapper.class)).selectAll();
        for (Route route : routeList) {
            List<RouteSite> routeSiteList = ((RouteSiteMapper) SpringTool.getBeanByClass(RouteSiteMapper.class)).selectByRouteId(route.getId());
            for (RouteSite routeSite : routeSiteList) {
                routeSiteHashMap.put(routeSite.getId(), routeSite);
            }
            routeHashMap.put(route.getId(), route);
        }
    }

    public HashMap<Integer, RouteSite> getRouteSiteHashMap() {
        return routeSiteHashMap;
    }

    public HashMap<Integer, Route> getRouteHashMap() {
        return routeHashMap;
    }

    public Collection<RouteSite> getCollectionRouteSite() {
        return routeSiteHashMap.values();
    }

    /**
     * 判断路径，以升降机为主体
     *
     * @param routeID
     * @param beforeBlockNo,        mckey
     * @param blockNo，reservedMckey
     * @return
     */
    public boolean routeSiteIsEqual(int routeID, String beforeBlockNo, String blockNo) {
        boolean result = false;
        Collection<RouteSite> routeSites = RouteSingleton.getInstance().getCollectionRouteSite();
        for (RouteSite routeSite : routeSites) {
            if (routeSite.getRouteid() == routeID && beforeBlockNo.equals(routeSite.getPresentblockno()) && blockNo.equals(routeSite.getNextblockno())) {
                result = true;
            }
        }
        return result;
    }


    /**
     * 获取下一个blockNo名称
     *
     * @param routeID
     * @param blockNo
     * @return
     */
    public String getNextBlockNo(int routeID, String blockNo) {
        String next_blockNo = "";
        Collection<RouteSite> routeSiteCollection = this.getCollectionRouteSite();
        for (RouteSite routeSite : routeSiteCollection) {
            if (routeSite.getRouteid() == routeID && blockNo.equals(routeSite.getPresentblockno())) {
                next_blockNo = routeSite.getNextblockno();
            }
        }
        return next_blockNo;
    }

    /**
     * 获取前一个blockNo
     *
     * @param routeID
     * @param blockNo
     * @return
     */
    public String getBeforeBlockNo(int routeID, String blockNo) {
        String next_blockNo = "";
        Collection<RouteSite> routeSiteCollection = this.getCollectionRouteSite();
        for (RouteSite routeSite : routeSiteCollection) {
            if (routeSite.getRouteid() == routeID && blockNo.equals(routeSite.getNextblockno())) {
                next_blockNo = routeSite.getPresentblockno();
            }
        }
        return next_blockNo;
    }

    /**
     * 当前blockNo是否为起始站名称
     * @param blockNo
     * @return
     */
    public boolean isStartStation(String blockNo) {
        boolean result = false;
        Collection<Route> routeCollection = this.getRouteHashMap().values();
        for (Route route : routeCollection) {
            if (StringUtils.isNotEmpty(blockNo) && blockNo.equals(route.getStartblockno())) {
                result = true;
            }
        }
        return result;
    }
}
