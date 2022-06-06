package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static String code;
    static String authServerPath;
    static String apiServerPath;
    static int pageLength;

    public static void main(String[] args) {
        readCL(args);

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        boolean isAuth = false;

        while (!input.equals("exit")) {

            if(isAuth == false && !input.equals("auth")) {
                System.out.println("Please, provide access for application.");
            } else if(input.equals("auth")){
                code = AuthController.getCode();
                isAuth = AuthController.tokenAuth(authServerPath);
                if(isAuth) {
                    System.out.println("Success!");
                } else System.out.println("---FAILED AUTH---");
            }

            if(isAuth) {
                MainMusicMenu.getChosenOption(input);
            }
            input = sc.nextLine();
        }
        System.out.println("---GOODBYE!---");
    }

    private static void readCL(String[] args) {
        if (args.length > 1 && "-access".equals(args[0]) && "-resource".equals(args[2])) {
            authServerPath = args[1];
            apiServerPath = args[3];
        }
        if(args.length > 4) {
            pageLength = Integer.parseInt(args[5]);
        } else {
            pageLength = 5;
        }

        if(authServerPath.isEmpty()) authServerPath = "https://accounts.spotify.com";
        if(apiServerPath.isEmpty()) apiServerPath = "https://api.spotify.com";
    }
}
