package co.com.test.api.product;

import co.com.test.api.RouterRest;
import co.com.test.api.branch.BranchHandler;
import co.com.test.api.franchise.FranchiseHandler;
import co.com.test.api.product.dto.ProductRequest;
import co.com.test.api.product.dto.ProductUpdateStockRequest;
import co.com.test.model.product.Product;
import co.com.test.model.product.ProductView;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, BranchHandler.class,
        ProductHandler.class, FranchiseHandler.class})
class ProductHandlerTest {

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
                .uri("/product")
                .header("Content-Type", "application/json")
                .body(Mono.just(ProductRequest.builder().build()), ProductRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldCreateProductSuccessfully() {
        ProductRequest request = ProductRequest.builder().branchId(1L).stock(1).name("Producto").build();

        given(productUseCase.create(any())).willReturn(Mono.just(buildProduct()));

        this.webTestClient.post()
                .uri("/product")
                .header("Content-Type", "application/json")
                .body(Mono.just(request), ProductRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        given(productUseCase.delete(1L)).willReturn(Mono.just(true));

        this.webTestClient.delete()
                .uri("/product/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        ProductUpdateStockRequest updateStockRequest = ProductUpdateStockRequest.builder().stock(20).build();

        given(productUseCase.updateStock(1L, 20))
                .willReturn(Mono.just(buildProduct().toBuilder().stock(20).build()));

        this.webTestClient.put()
                .uri("/product/1/stock")
                .header("Content-Type", "application/json")
                .body(Mono.just(updateStockRequest), ProductUpdateStockRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldGetTopProductsSuccessfully() {
        Long FRANCHISE_ID = 1L;
        String FRANCHISE_NAME = "MC BONO";

        List<ProductView> productViews = List.of(
                ProductView.builder()
                        .franchiseId(FRANCHISE_ID)
                        .franchiseName(FRANCHISE_NAME)
                        .branchId(4L)
                        .branchName("LA 46")
                        .productName("GASEOSA")
                        .totalStock(2000)
                        .build(),
                ProductView.builder()
                        .franchiseId(FRANCHISE_ID)
                        .franchiseName(FRANCHISE_NAME)
                        .branchId(4L)
                        .branchName("LA 46")
                        .productName("GASEOSA COCA COLA 1.5L")
                        .totalStock(2000)
                        .build(),
                ProductView.builder()
                        .franchiseId(FRANCHISE_ID)
                        .franchiseName(FRANCHISE_NAME)
                        .branchId(5L)
                        .branchName("LA 14")
                        .productName("GASEOSA COCA COLA 1.5L")
                        .totalStock(2000)
                        .build()
        );

        given(productUseCase.getTopStockProductsByFranchise(1L)).willReturn(Mono.just(productViews));

        this.webTestClient.get()
                .uri("/product/{franchiseId}/top", FRANCHISE_ID)
                .exchange()
                .expectStatus().isOk();
    }

    private Product buildProduct() {
        return Product.builder()
                .id(1L)
                .name("Producto")
                .stock(1)
                .branchId(1L)
                .build();
    }

}