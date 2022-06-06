package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static advisor.AuthController.accessToken;
import static advisor.Main.*;


public class SpotifyController {

    public static void getAllCategories() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/categories"))
                .GET()
                .build();

        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        JsonObject jsonBody = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject categories = jsonBody.get("categories").getAsJsonObject();

        JsonArray items = categories.getAsJsonArray("items");
        List<String> entries = new ArrayList<>();
        for (JsonElement item : items) {
            entries.add(item.getAsJsonObject().get("name").getAsString() + "\n");
        }
        SpotifyView.printPages(entries);
    }

    public static void getPlaylist(String category_id) throws IOException, InterruptedException {
        Map<String, String> categoryId = getCategoryId();
        String s = categoryId.get(category_id);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + String.format("/v1/browse/categories/%s/playlists", s)))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        JsonObject jsonBody = JsonParser.parseString(responseBody).getAsJsonObject();
        if(jsonBody.has("error")) {
            System.out.println(jsonBody.getAsJsonObject("error").get("message").getAsString());
        } else {
            JsonObject playlists = jsonBody.get("playlists").getAsJsonObject();
            JsonArray items = playlists.getAsJsonArray("items");
            List<String> entries = new ArrayList<>();
            for (JsonElement item : items) {
                StringBuilder str = new StringBuilder();
                str.append(item.getAsJsonObject().get("name").getAsString() + "\n");
                str.append(item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
                entries.add(str.toString());
            }
            SpotifyView.printPages(entries);
        }
    }

    public static void getNewReleases() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/new-releases"))
                .GET()
                .build();

        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        JsonObject jsonBody = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject albums = jsonBody.get("albums").getAsJsonObject();

        JsonArray items = albums.getAsJsonArray("items");
        List<String> entries = new ArrayList<>();
        for (JsonElement item : items) {
            StringBuilder str = new StringBuilder();
            str.append(item.getAsJsonObject().get("name").getAsString() + "\n");
            JsonArray artists = item.getAsJsonObject().getAsJsonArray("artists");
            StringBuilder artistNames = new StringBuilder().append("[");
            for (int i = 0; i < artists.size(); i++) {
                artistNames.append(artists.get(i).getAsJsonObject().get("name").getAsString());
                if(i != artists.size() - 1) artistNames.append(", ");
            }
            artistNames.append("]");
            str.append(artistNames + "\n");
            str.append(item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
            entries.add(str.toString());
        }
        SpotifyView.printPages(entries);
    }

    public static void getFeaturedPlaylists() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/featured-playlists"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        JsonObject jsonBody = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject playlists = jsonBody.get("playlists").getAsJsonObject();

        JsonArray items = playlists.getAsJsonArray("items");
        List<String> entries = new ArrayList<>();
        for (JsonElement item : items) {
            StringBuilder str = new StringBuilder();
            str.append(item.getAsJsonObject().get("name").getAsString() + "\n");
            str.append(item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
            entries.add(str.toString());
        }
        SpotifyView.printPages(entries);
    }

    public static Map<String, String> getCategoryId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/categories"))
                .GET()
                .build();

        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        JsonObject jsonBody = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject categories = jsonBody.get("categories").getAsJsonObject();
        JsonArray items1 = categories.getAsJsonArray("items");
        Map<String, String> ids = new HashMap<>();
        for (JsonElement item : items1) {
            String name = item.getAsJsonObject().get("name").getAsString();
            String id = item.getAsJsonObject().get("id").getAsString();
            ids.put(name, id);
        }

        return ids;
    }

}
