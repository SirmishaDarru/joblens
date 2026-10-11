package com.joblens.config;
import org.springframework.context.annotation.*;import org.springframework.scheduling.annotation.EnableAsync;import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;import java.util.concurrent.Executor;
@Configuration @EnableAsync public class AsyncConfig { @Bean(name="resumeExecutor") Executor resumeExecutor(){ThreadPoolTaskExecutor e=new ThreadPoolTaskExecutor();e.setCorePoolSize(2);e.setMaxPoolSize(4);e.setQueueCapacity(20);e.setThreadNamePrefix("resume-");e.initialize();return e;} }
