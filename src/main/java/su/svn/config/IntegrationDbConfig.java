package su.svn.config;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = {"su.svn.db.integration"},
        entityManagerFactoryRef = "entityManagerFactoryIntegration",
        transactionManagerRef = "transactionManagerIntegration")
@EnableTransactionManagement
public class IntegrationDbConfig {

    @Value("classpath:integration-drop.sql")
    private Resource dropReopsitoryTables;

    @Value("classpath:integration-create.sql")
    private Resource dataReopsitorySchema;

    @Bean(name = "jpaPropertiesIntegration")
    @ConfigurationProperties(prefix = "db.integration.jpa.properties")
    public Properties getJpaProperties() {
        return new Properties();
    }

    @Bean(name = "dataSourceIntegration")
    @ConfigurationProperties(prefix = "db.integration.datasource")
    public DataSource dataSourceIntegration() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "entityManagerFactoryIntegration")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryIntegration(
            Properties jpaPropertiesIntegration,
            @NotNull @Qualifier("dataSourceIntegration") DataSource integrationDataSource) {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(integrationDataSource);

        Properties props = new Properties();
        props.putAll(ImmutableMap.copyOf(jpaPropertiesIntegration));
        props.put("hibernate.jdbc.batch_size", 25);
        // props.put("hibernate.ejb.interceptor", auditJpaInterceptor);
        factoryBean.setJpaProperties(props);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPackagesToScan("su.svn.db.integration.*");
        factoryBean.setPersistenceUnitName("IntegrationDb");

        return factoryBean;
    }

    @Bean(name = "transactionManagerIntegration")
    public PlatformTransactionManager transactionManagerIntegration(
            @NotNull @Qualifier("entityManagerFactoryIntegration") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer2(@NotNull @Qualifier("dataSourceIntegration") DataSource dataSource) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

        databasePopulator.addScript(dropReopsitoryTables);
        databasePopulator.addScript(dataReopsitorySchema);
        databasePopulator.setIgnoreFailedDrops(true);

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);

        return initializer;
    }
}
