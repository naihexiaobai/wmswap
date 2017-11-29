package com.wap.control.init;


import com.ren.util.LoggerUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 *
 */
@Service
public class StartInit implements ApplicationListener<ContextRefreshedEvent> {

    LoggerUtil loggerUtil = new LoggerUtil("StartInit");

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            //只在初始化“根上下文”的时候执行
            if (contextRefreshedEvent.getSource() instanceof XmlWebApplicationContext) {
                if (((XmlWebApplicationContext) contextRefreshedEvent.getSource()).getDisplayName().equals("Root WebApplicationContext")) {
                    loggerUtil.getLogger().info("测试 ---  进来了");
                }
            }
        } catch (Exception e) {
        }
    }

}
