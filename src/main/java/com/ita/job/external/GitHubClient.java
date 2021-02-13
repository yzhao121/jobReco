package com.ita.job.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ita.job.entity.Item;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class GitHubClient {
    private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
    private static final String DEFAULT_KEYWORD = "developer";

    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }
        // “hello world” => “hello%20world”
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(URL_TEMPLATE, keyword, lat, lon);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        // Create a custom response handler
        ResponseHandler<List<Item>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return Collections.emptyList();
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Item> items = Arrays.asList(mapper.readValue(entity.getContent(), Item[].class));
            extractKeyWords(items);
            return items;
        };

        try {
            return httpclient.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private void extractKeyWords(List<Item> items) {
        MonkeyLearnClient monkeyLearnClient = new MonkeyLearnClient();
        List<String> descriptions = new ArrayList<>();
        //List<String> titles = new ArrayList<>();
        for (Item item : items) {
            descriptions.add(item.getDescription().replace("·", " "));
            //titles.add(item.getTitle());
        }


        List<Set<String>> keywordList = monkeyLearnClient.extract(descriptions);
        // if we can not extract keywords through description, we will use titles as the keywords
//        if (keywordList.isEmpty()) {
//            keywordList = monkeyLearnClient.extract(titles);
//        }
        for (int i = 0; i < Math.min(items.size(), keywordList.size()); i++) {
            items.get(i).setKeywords(keywordList.get(i));
        }

    }
}
