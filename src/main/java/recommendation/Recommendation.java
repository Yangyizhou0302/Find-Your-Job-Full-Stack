package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import db.MySQLClient;
import entity.Item;
import external.GitHubClient;

public class Recommendation {
	
	public List<Item> recommendItems(String userId, double lat, double lon){
		List<Item> recommendedItems = new ArrayList<>();
		
		//step 1, get all favorite itemids
		MySQLClient client = new MySQLClient();
		Set<String> favoritedItemIds = client.getFavoriteItemIds(userId);
		
		//step 2, get all keywords, sort by count
		Map<String, Integer> allKeywords = new HashMap<>();
		for(String itemId : favoritedItemIds) {
			Set<String> keywords = client.getKeywords(itemId);
			for(String keyword : keywords) {
				allKeywords.put(keyword, allKeywords.getOrDefault(keyword, 0)+1);
			}
		}
		client.close();
		
		List<Entry<String, Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
		Collections.sort(keywordList, (Entry<String, Integer> e1, Entry<String, Integer> e2)->{
			return Integer.compare(e2.getValue(), e1.getValue());
		});
		
		//cut down search list only top 3
		if(keywordList.size() > 3) {
			keywordList = keywordList.subList(0, 3);
		}
		
		//step 3, search based on keywords, filter out favorite items
		Set<String> visitedItemIds = new HashSet<>();
		GitHubClient connection = new GitHubClient();
		
		for(Entry<String, Integer> keyword : keywordList) {
			List<Item> items = connection.search(lat, lon, keyword.getKey());
			
			for(Item item : items) {
				if(!favoritedItemIds.contains(item.getItemId()) && !visitedItemIds.contains(item.getItemId())) {
					recommendedItems.add(item);
					visitedItemIds.add(item.getItemId());
				}
			}
		}
		return recommendedItems;
	}
	
}
