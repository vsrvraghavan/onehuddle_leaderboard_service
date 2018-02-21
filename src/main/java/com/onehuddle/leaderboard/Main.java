/**
 * 
 */
package com.onehuddle.leaderboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.onehuddle.leaderboard.redis.EventSubscriber;
import com.onehuddle.leaderboard.redis.Leaderboard;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class Main {

	
	@RequestMapping(value="/", method = RequestMethod.GET)
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {    	
    		Object[] sources = {Main.class, LeaderBoard.class};
    		ApplicationContext applicationContext =  SpringApplication.run(sources, args);        
        Leaderboard.DEFAULT_REDIS_HOST = args[0];
        	Leaderboard.DEFAULT_REDIS_PORT = Integer.valueOf(args[1]);        	
        	Thread subscriber = new Thread(applicationContext.getBean(EventSubscriber.class));        	
        	subscriber.start();
        	
    }

}