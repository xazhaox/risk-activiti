package com.xazhao.config;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 使用tkmapper导致Activiti使用JPA模式，这里指定成jdbc模式
 * 另外需要在启动类中把JpaProcessEngineAutoConfiguration.class排除
 *
 * @Description Created on 2024/03/22.
 * @Author xaZhao
 */

@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class ActivitiProcessEngineAutoConfiguration {

    @Configuration
    @EnableConfigurationProperties(ActivitiProperties.class)
    public static class DataSourceProcessEngineConfiguration extends AbstractProcessEngineAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        @ConditionalOnMissingBean
        public SpringProcessEngineConfiguration springProcessEngineConfiguration(
                DataSource dataSource,
                PlatformTransactionManager transactionManager,
                SpringAsyncExecutor springAsyncExecutor) throws IOException {

            return this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
        }
    }

}
