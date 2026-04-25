package com.softwarelabs.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
public class HeaderSizeTest {
    private static final Long productId = 1L;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void returnProductWithHttpStatusCode413_ifHeaderSizeMoreThanServerMaxHeaderSize() throws IOException {
        String header =
                new String(getClass().getClassLoader().getResourceAsStream("request/http_header_size_11KB.txt").readAllBytes());
        HttpHeaders headers = new HttpHeaders();
        header = "Bearer " + header;
        headers.set(HttpHeaders.AUTHORIZATION, header);
        HttpEntity httpEntity = new HttpEntity("body", headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/v1/product/" + productId, HttpMethod.GET,
                        httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().value() == HttpStatus.BAD_REQUEST.value());
    }
}
