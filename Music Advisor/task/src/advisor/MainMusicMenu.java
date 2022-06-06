package advisor;

import java.io.IOException;

public class MainMusicMenu {
    public static void getChosenOption(String input) {
        try {
            switch (input) {
                case "featured":
                    SpotifyController.getFeaturedPlaylists();
                    break;
                    case "new":
                        SpotifyController.getNewReleases();
                        break;
                        case "categories":
                            SpotifyController.getAllCategories();
                            break;
                        default:
                            if(input.contains("playlist")) {
                                String playlistName = input.substring(10);
                                SpotifyController.getPlaylist(playlistName);
                            }
                            break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
