package com.aquila.order;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class OrderServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);

        server.createContext("/health", exchange ->
                sendResponse(exchange, 200, "{\"status\":\"Order Service Running\"}")
        );

        server.createContext("/create", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 201, "{\"message\":\"Order created\"}");
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Order Service started on port 8082");
    }

    private static void sendResponse(com.sun.net.httpserver.HttpExchange exchange,
                                     int status,
                                     String body) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, body.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
}
