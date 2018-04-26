package com.kepware.opc.thread.monitor;

import com.kepware.opc.dto.status.*;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.interfaces.BlockStatusOperation;
import com.kepware.opc.interfaces.impl.*;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.www.util.LoggerUtil;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;

/**
 * @auther CalmLake
 * @create 2018/4/12  14:49
 */
public class DataCallBackDumper implements DataCallback {

    private OpcItem opcItem;

    public DataCallBackDumper(OpcItem opcItem) {
        this.opcItem = opcItem;
    }

    public void changed(final Item item, final ItemState itemState) {

    }



}
