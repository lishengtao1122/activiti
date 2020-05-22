package com.lesent.activiti.business.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ExecutorConfig {
	private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

	@Bean("sysExcutor")
	@Primary
	public Executor asyncServiceExecutor() {
		logger.info("start asyncServiceExecutor");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//配置核心线程数
		executor.setCorePoolSize(20);
		//配置最大线程数
		executor.setMaxPoolSize(20);
		//配置队列大小
		executor.setQueueCapacity(99999);
		//配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix("async-service-");

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		//1. CallerRunsPolicy ：不在新线程中执行任务，而是有调用者所在的线程来执行, 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
		//2. AbortPolicy ：对拒绝任务抛弃处理，并且抛出异常。
		//3. DiscardPolicy ：对拒绝任务直接无声抛弃，没有异常信息。
		//4. DiscardOldestPolicy ：对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		//执行初始化
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor asyncServiceExecutor4Sign() {
		logger.info("start asyncServiceExecutor4Sign");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//配置核心线程数
		executor.setCorePoolSize(20);
		//配置最大线程数
		executor.setMaxPoolSize(20);
		//配置队列大小
		executor.setQueueCapacity(99999);
		//配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix("async-service-4sign");

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		//1. CallerRunsPolicy ：不在新线程中执行任务，而是有调用者所在的线程来执行, 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
		//2. AbortPolicy ：对拒绝任务抛弃处理，并且抛出异常。
		//3. DiscardPolicy ：对拒绝任务直接无声抛弃，没有异常信息。
		//4. DiscardOldestPolicy ：对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		//执行初始化
		executor.initialize();
		return executor;
	}

	@Bean("activiti")
	public Executor asyncActivitiProcessControl() {
		logger.info("start async actviti callback processControl");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//配置核心线程数
		executor.setCorePoolSize(2);
		//配置最大线程数
		executor.setMaxPoolSize(4);
		//配置队列大小
		executor.setQueueCapacity(0);
		//配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix("async-activiti-processControl");

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		//1. CallerRunsPolicy ：不在新线程中执行任务，而是有调用者所在的线程来执行, 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
		//2. AbortPolicy ：对拒绝任务抛弃处理，并且抛出异常。
		//3. DiscardPolicy ：对拒绝任务直接无声抛弃，没有异常信息。
		//4. DiscardOldestPolicy ：对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		//执行初始化
		executor.initialize();
		return executor;
	}

}
