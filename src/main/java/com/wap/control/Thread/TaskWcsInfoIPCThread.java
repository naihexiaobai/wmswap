package com.wap.control.Thread;

import com.wap.control.ControlCc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 任务消息总处理
 *
 * @auther CalmLake
 * @create 2017/11/28  16:57
 */
@Controller
@RequestMapping("taskWcsInfoIPCThread")
public class TaskWcsInfoIPCThread extends ControlCc implements Runnable {

    public void run() {

    }
}
