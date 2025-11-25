package com.tasktracker.controllers;

import com.configurations.JavaRateLimiterClientConfiguration;
import com.services.SlidingWindowRateLimiter;
import com.services.TokenBucketRateLimiter;
import com.strategyobjects.SlidingWindow;
import com.strategyobjects.TokenBucket;
import com.tasktracker.config.RedisRateLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisRateLimiterConfig redisRateLimiterConfig;

    @GetMapping
    public String test() {
        JavaRateLimiterClientConfiguration config = new JavaRateLimiterClientConfiguration();
        config.setRedisHost(redisRateLimiterConfig.getHost());
        config.setRedisPort(redisRateLimiterConfig.getPort());
        config.setRedisUseSsl(redisRateLimiterConfig.isSsl());

//        TokenBucket   tokenBucket = new TokenBucket();
//        tokenBucket.setCapacity(1);
//        tokenBucket.setTokenCount(1);
//        tokenBucket.setLastRefillTimestamp(System.currentTimeMillis());
//        tokenBucket.setRefillRate(1); // tokens per second
//
//        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(config, tokenBucket);

        SlidingWindow slidingWindow = new SlidingWindow();
        slidingWindow.setMaxRequests(3);
        slidingWindow.setWindowSizeinMilliseconds(600);
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(config, slidingWindow);
        return rateLimiter.allowRequest("client1")? "Task Tracker is running!": "Rate limit exceeded";
    }
}
