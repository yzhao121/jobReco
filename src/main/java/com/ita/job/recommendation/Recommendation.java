package com.ita.job.recommendation;

import com.ita.job.db.MySQLConnection;
import com.ita.job.entity.Item;
import com.ita.job.external.GitHubClient;

import java.util.*;

public class Recommendation {

    public List<Item> recommendedItems(String userId, double lat, double lon) {
        List<Item> recommendedItems = new ArrayList<>();

        // step1: get all favorite item ids
        MySQLConnection connection = new MySQLConnection();
        Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);

        // step2: get all keywords, sort by count
        // {"keyword1": count1, ...}
        Map<String, Integer> allKeywords = new HashMap<>();
        for (String itemId : favoriteItemIds) {
            Set<String> keywords = connection.getKeywords(itemId);
            for (String keyword : keywords) {
                allKeywords.put(keyword, allKeywords.getOrDefault(keyword, 0) + 1);
            }
        }
        connection.close();

        List<Map.Entry<String, Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
        keywordList.sort((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) ->
                Integer.compare(e2.getValue(), e1.getValue()));

        // cut down search list only top 3
        if (keywordList.size() > 3) {
            keywordList.subList(0, 3);
        }

        // step3: search based on keywords, filter out favorite items
        Set<String> visitedItemIds = new HashSet<>();
        GitHubClient client = new GitHubClient();

        for (Map.Entry<String, Integer> keyword : keywordList) {
            List<Item> items = client.search(lat, lon, keyword.getKey());

            for (Item item : items) {
                if (!favoriteItemIds.contains(item.getId()) && !visitedItemIds.contains(item.getId())) {
                    recommendedItems.add(item);
                    visitedItemIds.add(item.getId());
                }
            }
        }
        return recommendedItems;
    }
}
