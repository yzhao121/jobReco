package com.ita.job.external;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GitHubClient {
    private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
    private static final String DEFAULT_KEYWORD = "developer";

    public String search(double lat, double lon, String keyword) {
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
        ResponseHandler<String> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                return "";
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "";
            }
            return EntityUtils.toString(entity);
        };

        try {
            return httpclient.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
