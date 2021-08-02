package com.framework.web.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        int port = 0;
        if (args == null || args.length == 0)
            port = DEFAULT_PORT;
        else
            port = Integer.parseInt(args[0]);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트 대기
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // Request 당 하나의 스레드 할당
                Thread thread = new Thread(new RequestHandler(connection));
                thread.start();
            }
        }
    }
}