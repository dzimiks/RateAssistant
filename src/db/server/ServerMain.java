package db.server;

import app.Constants;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) {
        try {
            Thread server = new Thread(new Server(Constants.PORT, 30));
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
