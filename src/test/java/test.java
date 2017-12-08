/**
 * 
 */


import org.springframework.boot.SpringApplication;

import redis.clients.jedis.Jedis;

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
		int pageSize = 2;
		pageSize = (pageSize < 1) ? 25 : pageSize;
		System.out.println("pageSize : " + pageSize);
		
	}
	
	
	public static void callMe2(String test, String... args) {
	    System.out.println(args.getClass() == String[].class);
	    for (String s : args) {
	        System.out.println(s);
	    }
	}

}
