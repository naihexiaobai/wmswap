package com.www.util;

import com.thief.wcs.dao.MessageLogMapper;
import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.dao.impl.PlcMapperImpl;
import com.wap.control.dao.daoImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * DB
 *
 * @auther CalmLake
 * @create 2017/11/30  14:14
 */
@Controller
@RequestMapping("dbUtil")
public class DBUtil {
    @Autowired
    public OpcItemsMapperImpl opcItemsMapperImpl;
    @Autowired
    public MachineInfoMapperImpl machineInfoMapperImpl;
    @Autowired
    public WCSControlInfoMapperImpl wCSControlInfoMapperImpl;
    @Autowired
    public StorageMapperImpl storageMapperImpl;
    @Autowired
    public CargoMapperImpl cargoMapperImpl;
    @Autowired
    public TaskHistoryMapperImpl taskHistoryMapperImpl;
    @Autowired
    public PlcMapperImpl plcMapperImpl;
    @Resource(name = "plcMapper")
    public PlcMapper plcMapper;


    public static DBUtil dbUtil = new DBUtil();
}
