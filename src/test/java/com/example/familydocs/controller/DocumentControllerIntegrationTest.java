package com.example.familydocs.controller;

import com.example.familydocs.model.User;
import com.example.familydocs.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.example.familydocs.config.SecurityConfig.passwordEncoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class DocumentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @SuppressWarnings("resource") //lifecycle of container is handled by JUnit
    @Container
    public static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("familydocs_test")
            .withUsername("familydocs_test_user")
            .withPassword("test_password");

    @Container
    public static final MinIOContainer minioContainer = new MinIOContainer("minio/minio:latest")
            .withExposedPorts(9000) // MinIO default port
            .withEnv("MINIO_ROOT_USER", "test_minio")
            .withEnv("MINIO_ROOT_PASSWORD", "test_minio123")
            .withCommand("server /data");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", () -> String.format("jdbc:mysql://%s:%d/familydocs_test",
                mysqlContainer.getHost(),
                mysqlContainer.getFirstMappedPort()));
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        registry.add("minio.url", () -> "http://localhost:" + minioContainer.getMappedPort(9000));
        registry.add("minio.access.name", () -> "test_minio");
        registry.add("minio.access.secret", () -> "test_minio123");
    }

    @BeforeEach
    public void setUp() {

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword(passwordEncoder().encode("testPassword"));
        userService.addUser(testUser);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void getAllDocumentsForUser_whenNoDocumentExists_shouldReturnEmptySet() throws Exception {

        mockMvc.perform(get("/document"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[]"));
    }
}
