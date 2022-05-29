package com.softwarelabs.springbootresilience4j.payment;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.softwarelabs.springbootresilience4j.WireMockInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
public class PaymentControllerInt {
    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private Integer port;

    @AfterEach
    public void afterEach() {
        this.wireMockServer.resetAll();
    }

    @Test
    void shouldReturnPaymentTransactionFromClient() {
        String paymentId = "1";
        this.wireMockServer.stubFor(
                WireMock.get("/api/transactions/1")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"id\": \"879a5029-825f-4e09-83c8-254eb99b70fe\" ,\"amount\": \"3.0\"}"))
        );

        this.webTestClient
                .get()
                .uri("http://localhost:" + port + "/payments/" + paymentId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("879a5029-825f-4e09-83c8-254eb99b70fe");
    }


    @Test
    void shouldRateLimitPaymentTransactionRequests() {
        String paymentId = "1";
        this.wireMockServer.stubFor(
                WireMock.get("/api/transactions/1")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"id\": \"879a5029-825f-4e09-83c8-254eb99b70fe\" ,\"amount\": \"3.0\"}"))
        );

        IntStream.range(0, 10).forEach(i -> {
            this.webTestClient
                    .get()
                    .uri("http://localhost:" + port + "/payments/" + paymentId)
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .expectBody()
                    .jsonPath("$.id")
                    .isEqualTo("879a5029-825f-4e09-83c8-254eb99b70fe");
        });
    }
}
