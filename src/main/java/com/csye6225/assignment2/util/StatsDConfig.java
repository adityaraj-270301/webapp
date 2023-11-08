package com.csye6225.assignment2.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Configuration
public class StatsDConfig {
    
    @Bean
    public StatsDClient statsDClient(
            @Value("${metrics.statsd.host:localhost}") String host,
            @Value("${metrics.statsd.port:8125}") int port,
            @Value("${metrics.prefix:webapp}") String prefix
    ) {
        return new NonBlockingStatsDClient(prefix, host, port);
    }
}
