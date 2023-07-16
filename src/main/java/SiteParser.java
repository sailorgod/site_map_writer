import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteParser {

    private SiteParser() {
    }


    public static List<String> parse(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        Elements elements = document.getElementsByAttribute("href");
        List<String> results = new ArrayList<>();
        String http = "http";
        String https = "https";
        String startLink = "";

        if (doubleString(url)) {
            return new ArrayList<>();
        }

        if (url.lastIndexOf("/") > 0) {
            startLink = url.substring(0, url.length() - 1);
        } else {
            startLink = url;
        }

        int i = 0;

        for (Element element : elements) {
            if (!element.hasText()) {
                continue;
            }
            String result = startLink + element.attr("href");

            if (!result.contains(http) || !result.contains(https)) {
                continue;
            }

            if (result.indexOf("/", 0) == 0 && startLink.equals("")) {
                startLink = elements.get(i - 1).attr("href");
                results.add(startLink + result);
                i++;
                continue;
            } else if (result.indexOf("/", 0) == 0 && !startLink.equals("")) {
                results.add(startLink + result);
                i++;
                continue;
            } else if (result.indexOf("/", 0) != 0) {
                startLink = "";
            }
            results.add(result);
            i++;
        }

        return results;
    }


    private static boolean doubleString(String url) {
        String[] strings = url.split("[/#]");
        Map<String, Integer> countMap = new HashMap<>();

        for (String string: strings) {
            string = string.replaceAll("[^a-zA-Zа-яА-Я]", "");

            if(countMap.containsKey(string)) {
                int count = countMap.get(string);
                countMap.put(string, count + 1);

                if (count + 1 > 2) {
                    return true;
                }
            }
            else {
                countMap.put(string, 1);
            }
        }

        return false;
    }
}