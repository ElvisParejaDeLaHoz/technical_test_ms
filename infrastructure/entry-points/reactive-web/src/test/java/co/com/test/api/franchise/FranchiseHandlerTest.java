package co.com.test.api.franchise;

import co.com.test.api.RouterRest;
import co.com.test.api.branch.BranchHandler;
import co.com.test.api.franchise.dto.FranchiseRequest;
import co.com.test.api.product.ProductHandler;
import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.TechnicalTestException;
import co.com.test.model.franchise.Franchise;
import co.com.test.usecase.branch.BranchUseCase;
import co.com.test.usecase.franchise.FranchiseUseCase;
import co.com.test.usecase.product.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, BranchHandler.class,
        ProductHandler.class, FranchiseHandler.class})
class FranchiseHandlerTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private BranchUseCase branchUseCase;

    @MockBean
    private ProductUseCase productUseCase;

    @MockBean
    private FranchiseUseCase franchiseUseCase;

    private WebTestClient webTestClient;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .build();
    }

    @Test
    void shouldReturnInvalidParametersErrorCreate() {
        this.webTestClient.post()
                .uri("/franchise")
                .header("Content-Type", "application/json")
                .body(Mono.just(FranchiseRequest.builder().build()), FranchiseRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {
        FranchiseRequest request = FranchiseRequest.builder().name("New Franchise").build();

        given(franchiseUseCase.create(any())).willReturn(Mono.just(buildFranchise()));

        this.webTestClient.post()
                .uri("/franchise")
                .header("Content-Type", "application/json")
                .body(Mono.just(request), FranchiseRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldHandleCreateFranchiseError() {
        FranchiseRequest request = FranchiseRequest.builder().name("New Franchise 1").build();

        given(franchiseUseCase.create(any()))
                .willReturn(Mono.error(new TechnicalTestException(TechnicalMessage.INVALID_INPUT)));

        this.webTestClient.post()
                .uri("/franchise")
                .header("Content-Type", "application/json")
                .body(Mono.just(request), FranchiseRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldUpdateFranchiseSuccessfully() {
        FranchiseRequest updateRequest = FranchiseRequest.builder().name("New Franchise 1").build();

        given(franchiseUseCase.update(any(), eq(1L)))
                .willReturn(Mono.just(buildFranchise()));

        this.webTestClient.put()
                .uri("/franchise/1")
                .header("Content-Type", "application/json")
                .body(Mono.just(updateRequest), FranchiseRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldHandleUpdateFranchiseError() {
        FranchiseRequest updateRequest = FranchiseRequest.builder().name("New Franchise 1").build();

        given(franchiseUseCase.update(any(), eq(1L)))
                .willReturn(Mono.error(new TechnicalTestException(TechnicalMessage.INVALID_INPUT)));

        this.webTestClient.put()
                .uri("/franchise/1")
                .header("Content-Type", "application/json")
                .body(Mono.just(updateRequest), FranchiseRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private Franchise buildFranchise() {
        return Franchise.builder()
                .id(1L)
                .name("Franquicia")
                .build();
    }

}