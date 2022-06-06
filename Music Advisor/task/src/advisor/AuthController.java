package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthController {
    static String code;
    private static int timeout = 30;
    static String CLIENT_ID = "4ab578e9b0e94f2cacfd806c9d28d3fd";
    static String CLIENT_SECRET = "0e0280ede86242e48e2b400bcf94ec6e";
    static String accessToken;

    public static String getCode () {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/",
                    new HttpHandler() {
                        public void handle(HttpExchange exchange) throws IOException {
                            while (code == null && timeout > 0) {
                                String query = exchange.getRequestURI().toString();
                                if (query != null) {
                                    Matcher matcher = Pattern.compile("code=\\d+").matcher(query);
                                    if (matcher.find()) {

                                        code = matcher.group().replace("code=", "").strip();
                                        String response = "Got the code. Return back to your program.";
                                        exchange.sendResponseHeaders(200, response.length());
                                        exchange.getResponseBody().write(response.getBytes());
                                    } else {
                                        String response = "Authorization code not found. Try again.";
                                        exchange.sendResponseHeaders(404, response.length());
                                        exchange.getResponseBody().write(response.getBytes());
                                    }
                                }
                            }
                        }
                    }
            );
            server.start();
            System.out.println("use this link to request the access code:");
            System.out.println("https://accounts.spotify.com/authorize?client_id=4ab578e9b0e94f2cacfd806c9d28d3fd&redirect_uri=http://localhost:8080&response_type=code");
            System.out.println("waiting for code...");
            while (Objects.isNull(code)) {
                Thread.sleep(10);
            }
            server.stop(1);
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Exception!");
        }

        return code;
    }

    public static boolean tokenAuth (String serverPath) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(serverPath + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code&code=" + code
                        + "&client_id=" + CLIENT_ID
                        + "&client_secret=" + CLIENT_SECRET
                        + "&redirect_uri=http://localhost:8080"))
                .build();

        System.out.println("making http request for access_token...");
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(response.statusCode() != 200) {
            return false;
        }
        String responseBody = response.body();
        JsonObject responseBodyJson = JsonParser.parseString(responseBody).getAsJsonObject();
        accessToken = responseBodyJson.get("access_token").getAsString();

        return true;
    }
}
