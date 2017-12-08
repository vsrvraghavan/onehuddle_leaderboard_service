/**
 * 
 */
package com.onehuddle.leaderboard;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import com.onehuddle.leaderboard.redis.Leaderboard;

@RestController
@EnableAutoConfiguration
public class Main {

	@RequestMapping(value="/", method = RequestMethod.GET)
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
    	
    		Object[] sources = {Main.class, LeaderBoard.class};
        SpringApplication.run(sources, args);
        
        Leaderboard.DEFAULT_REDIS_HOST = args[0];
        	Leaderboard.DEFAULT_REDIS_PORT = Integer.valueOf(args[1]);
        
        
    }

}