package com.jyujyu.dayonetest;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import com.redis.testcontainers.RedisContainer;

@Ignore
@Transactional
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {
	static DockerComposeContainer rdbms;
	static RedisContainer redis;

	static {
		rdbms = new DockerComposeContainer(new File("infra/test/docker-compose.yaml"))
			.withExposedService(
				"local-db",
				3306,
				Wait.forLogMessage(".*ready for connections.*", 1)
					.withStartupTimeout(Duration.ofSeconds(300))
			)
			.withExposedService(
				"local-db-migrate",
				0,
				Wait.forLogMessage("(.*Successfully validated.*)|(.*Successfully applied.*)", 1)
					.withStartupTimeout(Duration.ofSeconds(300))
			);

		rdbms.start();

		redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("6"));
		redis.start();
	}

	static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			Map<String, String> properties = new HashMap<>();

			var rdbmsHost = rdbms.getServiceHost("local-db", 3306);
			var rdbmsPort = rdbms.getServicePort("local-db", 3306);

			properties.put("spring.datasource.url", "jdbc:mysql://" + rdbmsHost + ":" + rdbmsPort + "/score");

			var redisHost = redis.getHost();
			var redisPort = redis.getFirstMappedPort();

			properties.put("spring.data.redis.host", redisHost);
			properties.put("spring.data.redis.port", redisPort.toString());


			TestPropertyValues.of(properties)
				.applyTo(applicationContext);
		}
	}
}
