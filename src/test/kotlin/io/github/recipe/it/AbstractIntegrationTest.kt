package io.github.recipe.it

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.Locale

@SpringBootTest
@ActiveProfiles("test")
abstract class AbstractIntegrationTest {
    companion object {
        @JvmStatic
        private val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))

        @JvmStatic
        @DynamicPropertySource
        private fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.liquibase.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            Locale.setDefault(Locale.ENGLISH)
            postgres.start()
        }
    }

    protected lateinit var client: WebTestClient

    @BeforeEach
    fun setup(context: ApplicationContext) {
        client = WebTestClient.bindToApplicationContext(context).build()
        client = client.mutate().responseTimeout(Duration.ofMillis(30000)).build()
    }
}
