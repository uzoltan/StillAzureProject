package eu.mantis.still_rul_app;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "aitiaEntityManagerFactory", transactionManagerRef = "aitiaTransactionManager", basePackages = {
    "eu.mantis.still_rul_app.repository.aitia"})
public class AitiaDbConfig {

  @Primary
  @Bean(name = "aitiaDataSource")
  @ConfigurationProperties(prefix = "primary.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean(name = "aitiaEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean aitiaEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("aitiaDataSource") DataSource dataSource) {
    return builder.dataSource(dataSource).packages("eu.mantis.still_rul_app.model.aitia").persistenceUnit("rul-wear-tracking")
                  .properties(jpaProperties()).build();
  }

  @Primary
  @Bean(name = "aitiaTransactionManager")
  public PlatformTransactionManager aitiaTransactionManager(@Qualifier("aitiaEntityManagerFactory") EntityManagerFactory aitiaEntityManagerFactory) {
    return new JpaTransactionManager(aitiaEntityManagerFactory);
  }

  private Map<String, Object> jpaProperties() {
    Map<String, Object> props = new HashMap<>();
    props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
    props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
    return props;
  }

}