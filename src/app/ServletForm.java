package app;

import models.Assistant;
import models.Review;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ServletForm", urlPatterns = {"/rate"})
public class ServletForm extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        int rating = Integer.parseInt(request.getParameter("rating"));
        Ratings.getInstance().postReview(new Review(new Assistant(firstName, lastName), rating));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (firstName.toUpperCase().equals(Constants.FORBIDDEN_NAME)) {
            rating = 0;
        }

        out.println("<head><title>Ratings</title><meta charset=\"UTF-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"/></head>");
        out.println("<body><div class=\"page-wrap\"><h1>Your rating:</h1><br>");
        out.println("<p>First name: <strong>" + firstName.toUpperCase() + "</strong></p><br>");
        out.println("<p>Last name: <strong>" + lastName.toUpperCase() + "</strong></p><br>");
        out.println("<p>Rating: <strong>" + rating + "</strong></p><br>");
        out.println("<a href=\"rate\"><button class=\"button-success\">See all ratings</button></a>");
        out.println("</div></body>");
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Review> reviews = Ratings.getInstance().getReviews();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map<Assistant, List<Integer>> ratingsMap = new HashMap<>();

        out.println("<head><title>Ratings</title><meta charset=\"UTF-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"/></head>");
        out.println("<body><div class=\"page-wrap\"><h1>Ratings:</h1><br>");
        out.println("<table><tr><th>Assistant</th><th>Rating</th></tr>");

        for (Review rating : reviews) {
            if (!ratingsMap.containsKey(rating.getAssistant())) {
                ratingsMap.put(rating.getAssistant(), new ArrayList<>());
            }

            ratingsMap.get(rating.getAssistant()).add(rating.getRating());
        }

        for (Assistant assistant : ratingsMap.keySet()) {
            int size = ratingsMap.get(assistant).size();
            double sum = 0;

            for (double rating : ratingsMap.get(assistant)) {
                sum += rating;
            }

            double avgRating = sum / (size);
            out.println("<tr><td>" + assistant.getFirstName() + " " + assistant.getLastName() + "</td><td>" + avgRating + "</td></tr>");
        }

        out.print("</table><br>");
        out.println("<a href=\"index.html\"><button class=\"button-success\">Enter new rating</button></a>");
        out.println("</div></body>");
        out.close();
    }
}
