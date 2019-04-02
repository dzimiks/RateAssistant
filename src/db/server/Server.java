package db.server;

import app.Constants;
import db.models.Memory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Memory memory;

    public Server(int port, int poolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.memory = new Memory();
    }

    @Override
    public void run() {
        System.out.println("Server is running on port " + Constants.PORT);

        while (true) {
            try {
                executorService.execute(new ServerThread(serverSocket.accept(), memory));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
