package diplom.work.diplombackend;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({"diplom.work.diplombackend.repository"})
@EntityScan({"diplom.work.diplombackend.model"})
public class JpaTestConfig extends JpaBaseConfiguration {
	public JpaTestConfig(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
		super(dataSource, properties, jtaTransactionManager);
	}

	@Configuration
	static class DataSourceConfig {

		@Bean
		public com.opentable.db.postgres.embedded.EmbeddedPostgres embeddedPostgres() throws Exception {
			return com.opentable.db.postgres.embedded.EmbeddedPostgres.start();
		}

		@Bean
		public DataSource dataSource(EmbeddedPostgres postgres) {
			String url = postgres.getJdbcUrl("postgres", "postgres");
			HikariDataSource dataSource = new HikariDataSource();
			dataSource.setJdbcUrl(url);
			return dataSource;
		}
	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(false);
		adapter.setDatabase(Database.POSTGRESQL);
		return adapter;
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		Map<String, Object> jpaProperties = new HashMap<>();
		jpaProperties.put("hibernate.hbm2ddl.auto", "update");
		jpaProperties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class);
		return jpaProperties;
	}

}