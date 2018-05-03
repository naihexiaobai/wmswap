package com.wap.control;

import com.kepware.opc.dao.OpcBlockMapper;
import com.kepware.opc.dao.OpcItemMapper;
import com.kepware.opc.dao.OpcOrderMapper;
import com.kepware.opc.dao.OpcWcsControlInfoMapper;
import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.dao.RouteMapper;
import com.thief.wcs.dao.RouteSiteMapper;
import com.wap.dao.daoImpl.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 向控制大佬低头
 *
 * @auther CalmLake
 * @create 2017/11/24  13:55
 */
@Controller
public class ControlCc {

    @Resource(name = "plcMapper", type = PlcMapper.class)
    public PlcMapper plcMapper;
    @Resource(name = "opcOrderMapper", type = OpcOrderMapper.class)
    public OpcOrderMapper opcOrderMapper;
    @Resource(name = "opcItemsMapperImpl", type = OpcItemsMapperImpl.class)
    public OpcItemsMapperImpl opcItemsMapperImpl;
    @Resource(name = "machineInfoMapperImpl", type = MachineInfoMapperImpl.class)
    public MachineInfoMapperImpl machineInfoMapperImpl;
    @Resource(name = "wCSControlInfoMapperImpl", type = WCSControlInfoMapperImpl.class)
    public WCSControlInfoMapperImpl wCSControlInfoMapperImpl;
    @Resource(name = "storageMapperImpl", type = StorageMapperImpl.class)
    public StorageMapperImpl storageMapperImpl;
    @Resource(name = "cargoMapperImpl", type = CargoMapperImpl.class)
    public CargoMapperImpl cargoMapperImpl;
    @Resource(name = "taskHistoryMapperImpl", type = TaskHistoryMapperImpl.class)
    public TaskHistoryMapperImpl taskHistoryMapperImpl;
    @Resource(name = "opcWcsControlInfoMapper", type = OpcWcsControlInfoMapper.class)
    public OpcWcsControlInfoMapper opcWcsControlInfoMapper;
    @Resource(name = "opcBlockMapper", type = OpcBlockMapper.class)
    public OpcBlockMapper opcBlockMapper;
    @Resource(name = "opcItemMapper", type = OpcItemMapper.class)
    public OpcItemMapper opcItemMapper;
    @Resource(name = "routeMapper", type = RouteMapper.class)
    public RouteMapper routeMapper;
    @Resource(name = "routeSiteMapper", type = RouteSiteMapper.class)
    public RouteSiteMapper routeSiteMapper;
}