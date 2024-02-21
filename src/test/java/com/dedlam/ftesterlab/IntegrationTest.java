package com.dedlam.ftesterlab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.UseMainMethod.ALWAYS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(useMainMethod = ALWAYS, webEnvironment = DEFINED_PORT)
@ActiveProfiles("integrationTest")
class IntegrationTest {
  private final JdbcTemplate jdbcTemplate;
  private final String baseUrl;

  private final Gson gson = new GsonBuilder().create();

  private final HttpClient httpClient = HttpClient
    .newBuilder()
    .connectTimeout(Duration.ofSeconds(1))
    .build();

  @Autowired
  public IntegrationTest(
    JdbcTemplate jdbcTemplate,
    @Value("${baseUrl}") String baseUrl
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.baseUrl = baseUrl;
  }

  @Test
  void configuring() {
    initAdmin();
    String adminAccessToken = loginAdmin();

    registerStudent(adminAccessToken);
  }

  private void initAdmin() {
    var sql = "insert into users(dtype, id, username, password) values('ADMIN', '" + UUID.randomUUID() + "', 'admin', 'admin_pwd');";
    jdbcTemplate.execute(sql);
  }

  private String loginAdmin() {
    var loginRequestData = new LoginRequest("admin", "admin_pwd");
    var loginResponse = doRequest("POST", "/auth/login", loginRequestData);

    var accessToken = gson.fromJson(loginResponse.body().toString(), LoginResponse.class).accessToken;

    return accessToken;
  }

  private void registerStudent(String accessToken) {
    String body = gson.toJson(new RegisterUserRequest("student", "student_pwd"));
    var bodyPublisher = HttpRequest.BodyPublishers.ofString(body);

    var request = HttpRequest.newBuilder()
      .method("POST", bodyPublisher)
      .uri(uri("/admin/registration/students"))
      .timeout(Duration.ofSeconds(5))
      .headers(
        "Accept", "*/*",
        "Content-Type", "application/json",
        "Authorization", "Bearer " + accessToken
      )
      .build();

    var requestBodyHandler = HttpResponse.BodyHandlers.ofString();

    try {
      httpClient.send(request, requestBodyHandler);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  private HttpResponse<?> doRequest(String method, String path, Object data) {
    String body = gson.toJson(data);
    var bodyPublisher = HttpRequest.BodyPublishers.ofString(body);

    var request = HttpRequest.newBuilder()
      .method(method, bodyPublisher)
      .uri(uri(path))
      .timeout(Duration.ofSeconds(5))
      .headers(
        "Accept", "*/*",
        "Content-Type", "application/json"
      )
      .build();

    var requestBodyHandler = HttpResponse.BodyHandlers.ofString();

    try {
      return httpClient.send(request, requestBodyHandler);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  private URI uri(String path) {
    try {
      return new URI(baseUrl + path);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  private record LoginRequest(
    String login,
    String password
  ) {
  }

  private record LoginResponse(
    String accessToken,
    String refreshToken
  ) {
  }

  private record RegisterUserRequest(
    String username,
    String password
  ) {
  }
}
