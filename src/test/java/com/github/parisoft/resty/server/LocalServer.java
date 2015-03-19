package com.github.parisoft.resty.server;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.SocketUtils;

@ComponentScan
@EnableAutoConfiguration
public class LocalServer {

    private static Integer port;
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() {
        if (isRunning()) {
            return;
        }

        port = SocketUtils.findAvailableTcpPort();

        System.getProperties().put("server.port", port);

        context = SpringApplication.run(LocalServer.class);
    }

    public static Integer getPort() {
        return port;
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static String getHost() {
        if (!isRunning()) {
            return null;
        }

        return "http://localhost:" + port;
    }

    public static boolean isRunning() {
        return context != null && context.isRunning();
    }
}
