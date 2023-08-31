package net.ringo.ringoSwap.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.ringo.ringoSwap.domain.Feed;

public class FeedSort {
	//중복없이 feed 배열을 병합하는 메서드
	public static ArrayList<Feed> FeedMerge(ArrayList<Feed> feedLeft, ArrayList<Feed> feedRight) {
		Map<Integer, Feed> DupliCheck = new HashMap<>();
		ArrayList<Feed> MergeList = new ArrayList<>();
		
		for (Feed feed : feedLeft) {
			DupliCheck.put(feed.getFeed_num(), feed);
        }
		for (Feed feed : feedRight) {
			DupliCheck.put(feed.getFeed_num(), feed);
		}

		MergeList.addAll(DupliCheck.values());
		return MergeList;
	}
	
	//feed 배열을 inputdate를 기준으로 정렬하는 메서드
	public static ArrayList<Feed> feedSortWithInputdate(ArrayList<Feed> feedList) {
		Collections.sort(feedList,  new Comparator<Feed>() {
			@Override
			public int compare(Feed feed1, Feed feed2) {
				return feed2.getInputdate().compareTo(feed1.getInputdate());
			}
		});
		return feedList;
	}
	//feed 배열을 like_count를 기준으로 정렬하는 메서드
	public static ArrayList<Feed> feedSortWithLikeCount(ArrayList<Feed> feedList) {
		Collections.sort(feedList,  new Comparator<Feed>() {
			@Override
			public int compare(Feed feed1, Feed feed2) {
				return feed2.getInputdate().compareTo(feed1.getInputdate());
			};
		});
	return feedList;
	}
	
}

