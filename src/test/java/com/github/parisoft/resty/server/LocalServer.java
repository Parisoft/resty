package com.github.parisoft.resty.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.SocketUtils;

@ComponentScan
@EnableAutoConfiguration
public class LocalServer {

    private static int port;
    private static ConfigurableApplicationContext context;

    public static void start() {
        if (isRunning()) {
            return;
        }

        port = SocketUtils.findAvailableTcpPort();

        System.getProperties().put("server.port", port);

        context = SpringApplication.run(LocalServer.class);
    }

    public static int getPort() {
        return port;
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static boolean isRunning() {
        return context != null && context.isRunning();
    }
}
