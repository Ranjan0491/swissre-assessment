package com.swissre.app.it;

import com.swissre.app.repositories.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"wiremock.server.httpsPort=-1"})
@AutoConfigureMockMvc
@ActiveProfiles("it-tests")
public class IntegrationTestUtils {
    static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withDatabaseName("demo")
            .withUsername("root")
            .withPassword("pass");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ObjectMapper objectMapper;

    static void initContainers() {
        MY_SQL_CONTAINER.start();
    }

    static void stopContainers() {
        MY_SQL_CONTAINER.stop();
    }

    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    }

    @BeforeAll
    public static void beforeAll() {
        initContainers();
    }

    @AfterAll
    public static void afterAll() {
        stopContainers();
    }

    @DynamicPropertySource
    public static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registerProperties(registry);
    }

    @BeforeEach
    public void beforeEach() {
        this.taskRepository.deleteAll();
    }
}
