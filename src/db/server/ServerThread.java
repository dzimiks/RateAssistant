package db.server;

import com.google.gson.Gson;
import db.models.Memory;
import models.Request;
import models.Response;
import models.Review;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ServerThread implements Runnable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Memory memory;
    private Gson gson;

    public ServerThread(Socket socket, Memory memory) throws IOException {
        this.socket = socket;
        this.memory = memory;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            String json = in.readUTF();
            Request request = gson.fromJson(json, Request.class);

            if (request.getType().equals("POST")) {
                memory.addReview(request.getReview());
            } else if (request.getType().equals("GET")) {
                List<Review> reviews = memory.getReviews();
                Response response = new Response("OK", reviews);
                out.writeUTF(gson.toJson(response));
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}