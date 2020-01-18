package su.svn.config;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackages = {"su.svn.db.front"},
        entityManagerFactoryRef = "entityManagerFactoryFront",
        transactionManagerRef = "transactionManagerFront")
@EnableTransactionManagement
public class FrontDbConfig {

    @Value("classpath:front-drop.sql")
    private Resource dropReopsitoryTables;

    @Value("classpath:front-create.sql")
    private Resource dataReopsitorySchema;

    @Primary
    @Bean(name = "jpaPropertiesFront")
    @ConfigurationProperties(prefix = "db.front.jpa.properties")
    public Properties getJpaProperties() {
        return new Properties();
    }

    @Primary
    @Bean(name = "dataSourceFront")
    @ConfigurationProperties(prefix = "db.front.datasource")
    public DataSource dataSourceFront() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactoryFront")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryFront(
            Properties jpaPropertiesFront,
            @NotNull @Qualifier("dataSourceFront") DataSource frontDataSource) {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(frontDataSource);

        Properties props = new Properties();
        props.putAll(ImmutableMap.copyOf(jpaPropertiesFront));
        props.put("hibernate.jdbc.batch_size", 25);
        // props.put("hibernate.ejb.interceptor", auditJpaInterceptor);
        factoryBean.setJpaProperties(props);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPackagesToScan("su.svn.db.front.*");
        factoryBean.setPersistenceUnitName("FrontDb");

        return factoryBean;
    }

    @Primary
    @Bean(name = "transactionManagerFront")
    public PlatformTransactionManager transactionManagerFront(
            @NotNull @Qualifier("entityManagerFactoryFront") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer1(@NotNull @Qualifier("dataSourceFront") DataSource dataSource) {
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
