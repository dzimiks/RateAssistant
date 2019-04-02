package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Request;
import models.Response;
import models.Review;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Ratings {

    private Gson gson;
    private static Ratings instance = null;

    private Ratings() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void postReview(Review review) throws IOException {
        Socket socket = new Socket(Constants.HOST, Constants.PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        Request request = new Request("POST", review);
        out.writeUTF(gson.toJson(request));

        in.close();
        out.close();
        socket.close();
    }

    public List<Review> getReviews() throws IOException {
        Socket socket = new Socket(Constants.HOST, Constants.PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        Request request = new Request("GET", null);
        out.writeUTF(gson.toJson(request));

        String json = in.readUTF();
        Response response = gson.fromJson(json, Response.class);

        in.close();
        out.close();
        socket.close();

        return response.getReviews();
    }

    public static Ratings getInstance() {
        if (instance == null) {
            instance = new Ratings();
        }

        return instance;
    }
}
