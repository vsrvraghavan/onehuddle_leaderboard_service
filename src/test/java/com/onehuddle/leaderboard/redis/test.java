/**
 * 
 */
package com.onehuddle.leaderboard.redis;

import java.util.ArrayList;
import java.util.List;

import com.onehuddle.leaderboard.pojo.LeaderData;
import com.onehuddle.leaderboard.util.HttpUtils;

/**
 * @author ragha
 *
 */
public class test {

	/**
	 * 
	 */
	public test() {
		// TODO Auto-generated constructor stub
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
    	
		/*
		String[] str = new String[1];
		str[0] = "company_1_game_2_leaderboard";
		
		Jedis _jedis = new Jedis("localhost", 6379);
		_jedis.zunionstore("company_1_leaderboard", str);
		
		//callMe2("Tese", str);
		 * 
		 */
		/*
		Leaderboard lb = new Leaderboard("localhost", 6379);
		List<LeaderData> leaderList = new ArrayList<LeaderData>();
		Jedis _jedis = new Jedis("localhost", 6379);
		ZParams params = new ZParams();
		params.weights(0,1);
		long result = _jedis.zinterstore("baz", params, "foo", "bar");
		
		Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores("baz", 0, -1);
		
		lb._leaderboardName = "baz";
		
		System.out.println(rawLeaderData.size());
		
		
		leaderList = lb.massageLeaderData("baz", rawLeaderData, false);
		
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println(mapper.writeValueAsString(leaderList));
		
		*/
		
		
		List<LeaderData> leaderList1 = new ArrayList<LeaderData>();
		
		LeaderData ld = new LeaderData("Raga", 1000, 3, "GAME1");
		leaderList1.add(ld);
		ld = new LeaderData("Nirmalya", 1003, 2, "GAME1");
		leaderList1.add(ld);
		ld = new LeaderData("Andy", 1010, 1, "GAME1");
		leaderList1.add(ld);
		
		HttpUtils httputils = new HttpUtils();
		httputils.updateAdminPanel(leaderList1, "GAME1");
		
		
		
	}
	
	
	public static void callMe2(String test, String... args) {
	    System.out.println(args.getClass() == String[].class);
	    for (String s : args) {
	        System.out.println(s);
	    }
	}

}
