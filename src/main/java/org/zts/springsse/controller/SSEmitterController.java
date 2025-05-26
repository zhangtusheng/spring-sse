package org.zts.springsse.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;


@RestController
@RequestMapping("/sse")
public class SSEmitterController {

    @GetMapping("/stream")
    public SseEmitter stream() {
        // 用于创建一个 SSE 连接对象
        SseEmitter emitter = new SseEmitter();

        // 在后台线程中模拟实时数据
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    // emitter.send() 方法向客户端发送消息
                    // 使用SseEmitter.event()创建一个事件对象，设置事件名称和数据
                    emitter.send(SseEmitter.event().name("message").data("[" + new Date() + "] Data #" + i));
                    Thread.sleep(1000);
                }
                // 数据发送完成后，关闭连接
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                // 发生错误时，关闭连接并报错
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }
}
