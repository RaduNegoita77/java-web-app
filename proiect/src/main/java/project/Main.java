package project;

import project.server.ClientHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Server pornit la http://localhost:8080");
            while (true) {
                Socket s = server.accept();
                pool.execute(new ClientHandler(s));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}