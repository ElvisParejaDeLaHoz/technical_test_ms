package co.com.test.api.branch;

import co.com.test.api.RouterRest;
import co.com.test.api.branch.dto.BranchRequest;
import co.com.test.api.branch.dto.BranchUpdateRequest;
import co.com.test.api.franchise.FranchiseHandler;
import co.com.test.api.product.ProductHandler;
import co.com.test.model.branch.Branch;
import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.TechnicalTestException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, BranchHandler.class,
        ProductHandler.class, FranchiseHandler.class})
class BranchHandlerTest {

    public static final String SUCURSAL = "Sucursal";
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
                .uri("/branch")
                .header("Content-Type", "application/json")
                .body(Mono.just(BranchRequest.builder().build()), BranchRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldCreateBranchSuccessfully() {
        BranchRequest request = BranchRequest.builder().name(SUCURSAL).franchiseId(1L).build();
        given(branchUseCase.create(any())).willReturn(Mono.just(buildBranch()));

        this.webTestClient.post()
                .uri("/branch")
                .header("Content-Type", "application/json")
                .body(Mono.just(request), BranchRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void shouldHandleCreateBranchError() {
        BranchRequest request = BranchRequest.builder().name("Invalid Branch").build();

        given(branchUseCase.create(any())).willReturn(Mono.error(new TechnicalTestException(TechnicalMessage.INTERNAL_ERROR)));

        this.webTestClient.post()
                .uri("/branch")
                .header("Content-Type", "application/json")
                .body(Mono.just(request), BranchRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldUpdateBranchSuccessfully() {
        BranchUpdateRequest updateRequest = BranchUpdateRequest.builder().name("Updated Branch").build();

        given(branchUseCase.updateName(eq(1L), anyString()))
                .willReturn(Mono.just(buildBranch()));

        this.webTestClient.put()
                .uri("/branch/1")
                .header("Content-Type", "application/json")
                .body(Mono.just(updateRequest), BranchUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldHandleUpdateBranchError() {
        BranchUpdateRequest updateRequest = BranchUpdateRequest.builder().name("Invalid Update").build();

        given(branchUseCase.updateName(eq(1L), anyString()))
                .willReturn(Mono.error(new TechnicalTestException(TechnicalMessage.INVALID_INPUT)));

        this.webTestClient.put()
                .uri("/branch/1")
                .header("Content-Type", "application/json")
                .body(Mono.just(updateRequest), BranchUpdateRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private Branch buildBranch() {
        return Branch.builder()
                .id(1L)
                .name(SUCURSAL)
                .franchiseId(1L)
                .build();
    }

}