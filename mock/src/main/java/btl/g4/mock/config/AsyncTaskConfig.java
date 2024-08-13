package btl.g4.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncTaskConfig {

    public static final String BEAN_ASYNC_EXECUTOR = "APPLICATION_ASYNC_EXECUTOR";

    private static final int DEFAULT_POOL_SIZE = 25;

    @Bean(BEAN_ASYNC_EXECUTOR)
    public AsyncTaskExecutor initTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(DEFAULT_POOL_SIZE);
        pool.setMaxPoolSize(Integer.MAX_VALUE);
        pool.setQueueCapacity(Integer.MAX_VALUE);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.setThreadNamePrefix("task-");

        return pool;
    }
}
