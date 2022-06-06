package advisor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SpotifyView {

    public static void printPages(List<String> entries) {
        int numberOfPages = entries.size() / Main.pageLength;
        int counter = 0;
        StringBuilder strBuilder = new StringBuilder();
        List<String> pages = new ArrayList<>();

        for (String str: entries) {
            counter++;
            if(counter == Main.pageLength) {
                strBuilder.append(str);
                counter = 0;
                pages.add(strBuilder.toString());
                strBuilder = new StringBuilder();
            } else {
                strBuilder.append(str + "\n");
            }
        }

        browsePages(numberOfPages, pages);
    }

    private static void browsePages(int numberOfPages, List<String> pages) {
        int pageNum = 1;
        Scanner sc = new Scanner(System.in);
        System.out.println(pages.get(pageNum - 1));
        System.out.printf("---PAGE %d OF %d---\n", pageNum, numberOfPages);

        String action = sc.nextLine();
        while(!"exit".equals(action)) {
            if("prev".equals(action) && pageNum == 1) {
                System.out.println("No more pages");
            } else if("prev".equals(action) && pageNum > 1) {
                pageNum--;
                System.out.println(pages.get(pageNum - 1));
                System.out.printf("---PAGE %d OF %d---\n", pageNum, numberOfPages);
            } else if("next".equals(action) && pageNum == numberOfPages) {
                System.out.println("No more pages");
            } else if("next".equals(action) && pageNum < numberOfPages) {
                pageNum++;
                System.out.println(pages.get(pageNum - 1));
                System.out.printf("---PAGE %d OF %d---\n", pageNum, numberOfPages);
            }
            action = sc.nextLine();
        }
    }
}