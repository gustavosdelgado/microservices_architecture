package io.github.gustavosdelgado.microorder.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gustavosdelgado.microorder.domain.order.OrderRequest;
import io.github.gustavosdelgado.microorder.domain.order.OrderResponse;
import io.github.gustavosdelgado.microorder.it.base.AuthTokenService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderControllerIT {

    private static final String ROLE_CONSUMER = "ROLE_CONSUMER";
    private static final int EXPIRATION_TIME = 60;

    @Autowired
    private AuthTokenService tokenService;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private RabbitAdmin rabbitAdmin;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Value("${api.security.token.secret}")
    private String secret;

    @Test
    public void givenValidTokenWhenCallToOrderThenSucceeds() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest("orderId", "restaurantId"), headers);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
    }

    @Test
    public void givenRepeatedOrderWhenCallToOrderThenFails() throws Exception {
        givenValidTokenWhenCallToOrderThenSucceeds();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest("orderId", "restaurantId"), headers);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
    }

    @Test
    public void givenInvalidTokenWhenCallToOrderThenFails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken("invalidSecret", EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest("orderId", "restaurantId"), headers);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
    }

    @Test
    public void givenExpiredTokenWhenCallToOrderThenFails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, 1, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest("orderId", "restaurantId"), headers);

        Thread.sleep(5000);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
    }

    @Test
    public void givenInvalidRoleWhenCallToOrderThenFails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, 60, "invalidRole"));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest("orderId", "restaurantId"), headers);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
    }

    @Test
    public void givenNullOrderIdWhenCallToOrderThenFails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest(null, "restaurantId"), headers);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
    }

    @Test
    public void givenNullRestaurantIdWhenCallToOrderThenFails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest("orderId", null), headers);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
    }

    @Test
    public void givenNullRestaurantIdAndNullOrderIdWhenCallToOrderThenFails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(new OrderRequest(null, null), headers);

        ResponseEntity<String> exchange = restTemplate.exchange("/order", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
    }

    @Test
    public void givenValidTokenWhenGetOrderThenSucceeds() throws Exception {
        givenValidTokenWhenCallToOrderThenSucceeds();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(headers);

        ResponseEntity<OrderResponse> exchange = restTemplate.exchange("/order/orderId", HttpMethod.GET, request,
                OrderResponse.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals("restaurantId", exchange.getBody().restaurantId());
    }

    @Test
    public void givenInvalidTokenWhenGetOrderThenFails() throws Exception {
        givenValidTokenWhenCallToOrderThenSucceeds();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken("invalidSecret", EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(headers);

        ResponseEntity<OrderResponse> exchange = restTemplate.exchange("/order/orderId", HttpMethod.GET, request,
                OrderResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
    }

    @Test
    public void givenExpiredTokenWhenGetOrderThenFails() throws Exception {
        givenValidTokenWhenCallToOrderThenSucceeds();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, 1, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(headers);

        Thread.sleep(5000);

        ResponseEntity<OrderResponse> exchange = restTemplate.exchange("/order/orderId", HttpMethod.GET, request,
                OrderResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
    }

    @Test
    public void givenInvalidRoleWhenGetOrderThenFails() throws Exception {
        givenValidTokenWhenCallToOrderThenSucceeds();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, "invalidRole"));
        HttpEntity<OrderRequest> request = new HttpEntity<>(headers);

        ResponseEntity<OrderResponse> exchange = restTemplate.exchange("/order/orderId", HttpMethod.GET, request,
                OrderResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
    }

    @Test
    public void givenInvalidOrderIdWhenGetOrderThenFails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.generateToken(secret, EXPIRATION_TIME, ROLE_CONSUMER));
        HttpEntity<OrderRequest> request = new HttpEntity<>(headers);

        ResponseEntity<OrderResponse> exchange = restTemplate.exchange("/order/notFound", HttpMethod.GET, request,
                OrderResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
    }

}
