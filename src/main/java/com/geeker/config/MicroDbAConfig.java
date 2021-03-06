package com.geeker.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Author TangZhen
 * @Date 2018/4/12 0012 15:03
 * @Description micro库数据源配置
 */
@Configuration
@MapperScan(basePackages = {"com.geeker.mapper.micro"}, sqlSessionFactoryRef = "microSqlSessionFactory")
public class MicroDbAConfig {

    @Bean(name = "microDataSource")
    @ConfigurationProperties(prefix = "micro.datasource") // prefix值必须是application.properteis中对应属性的前缀
    public DataSource microDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "microTransactionFactory")
    public SpringManagedTransactionFactory transactionFactory() {
        return new SpringManagedTransactionFactory();
    }

    @Bean(name = "microTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("microDataSource") DataSource dataSource){
       return new DataSourceTransactionManager(dataSource);
    }


    @Bean
    public SqlSessionFactory microSqlSessionFactory(@Qualifier("microDataSource") DataSource dataSource, @Qualifier("microTransactionFactory") TransactionFactory transactionFactory) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTransactionFactory(transactionFactory);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mapping/micro/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate microSqlSessionTemplate(@Qualifier("microSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory); // 使用上面配置的Factory
        return template;
    }
}
