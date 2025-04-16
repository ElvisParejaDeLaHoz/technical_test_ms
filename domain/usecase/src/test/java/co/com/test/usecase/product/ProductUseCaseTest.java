package co.com.test.usecase.product;

import co.com.test.model.branch.Branch;
import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.franchise.Franchise;
import co.com.test.model.product.Product;
import co.com.test.model.product.ProductParam;
import co.com.test.model.product.ProductView;
import co.com.test.model.product.gateways.ProductRepository;
import co.com.test.usecase.branch.BranchUseCase;
import co.com.test.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchUseCase branchUseCase;

    @Mock
    private FranchiseUseCase franchiseUseCase;

    private ProductUseCase productUseCase;

    @BeforeEach
    void setUp() {
        productUseCase = new ProductUseCase(productRepository, branchUseCase, franchiseUseCase);
    }

    @Test
    void shouldCreateProduct() {
        ProductParam productParam = ProductParam.builder()
                .stock(1)
                .branchId(1L)
                .name("Producto")
                .build();

        given(branchUseCase.get(1L))
                .willReturn(Mono.just(buildBranch()));

        given(productRepository.save(productParam))
                .willReturn(Mono.just(buildProduct()));

        Mono<Product> productResponse = productUseCase.create(productParam);

        StepVerifier.create(productResponse)
                .expectNext(buildProduct())
                .verifyComplete();

        verify(productRepository, times(1))
                .save(productParam);

        verify(branchUseCase, times(1))
                .get(1L);
    }

    @Test
    void shouldReturnErrorCreateProduct() {
        ProductParam productParam = ProductParam.builder()
                .stock(1)
                .branchId(1L)
                .name("Producto")
                .build();

        given(branchUseCase.get(1L))
                .willReturn(Mono.empty());

        Mono<Product> productResponse = productUseCase.create(productParam);

        StepVerifier.create(productResponse)
                .expectError(BusinessException.class)
                .verify();

        verify(branchUseCase, times(1))
                .get(1L);
    }

    @Test
    void shouldDeleteProduct() {
        given(productRepository.getById(1L))
                .willReturn(Mono.just(buildProduct()));

        given(productRepository.delete(1L))
                .willReturn(Mono.just(Boolean.TRUE));

        Mono<Boolean> productResponse = productUseCase.delete(1L);

        StepVerifier.create(productResponse)
                .expectNext(Boolean.TRUE)
                .verifyComplete();

        verify(productRepository, times(1))
                .delete(1L);

        verify(productRepository, times(1))
                .getById(1L);
    }

    @Test
    void shouldErrorDeleteProduct() {
        given(productRepository.getById(1L))
                .willReturn(Mono.just(buildProduct()));

        given(productRepository.delete(1L))
                .willReturn(Mono.just(Boolean.FALSE));

        Mono<Boolean> productResponse = productUseCase.delete(1L);

        StepVerifier.create(productResponse)
                .expectError(BusinessException.class)
                .verify();

        verify(productRepository, times(1))
                .getById(1L);
    }

    @Test
    void shouldErrorDeleteProductById() {
        given(productRepository.getById(1L))
                .willReturn(Mono.empty());

        Mono<Boolean> productResponse = productUseCase.delete(1L);

        StepVerifier.create(productResponse)
                .expectError(BusinessException.class)
                .verify();

        verify(productRepository, times(1))
                .getById(1L);
    }

    @Test
    void shouldUpdateStockProduct() {

        given(productRepository.getById(1L))
                .willReturn(Mono.just(buildProduct()));

        given(productRepository.updateStock(1L, 10))
                .willReturn(Mono.just(Boolean.TRUE));

        Product product = buildProduct().toBuilder().stock(10).build();

        given(productRepository.getById(1L))
                .willReturn(Mono.just(product));

        Mono<Product> productResponse = productUseCase.updateStock(1L, 10);

        StepVerifier.create(productResponse)
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(2))
                .getById(1L);
    }

    @Test
    void shouldErrorUpdateStockProduct() {
        given(productRepository.getById(1L))
                .willReturn(Mono.just(buildProduct()));

        given(productRepository.updateStock(1L, 10))
                .willReturn(Mono.just(Boolean.FALSE));

        Mono<Product> productResponse = productUseCase.updateStock(1L, 10);

        StepVerifier.create(productResponse)
                .expectErrorMatches(throwable -> {
                    BusinessException businessException = (BusinessException) throwable;
                    Assertions.assertEquals(TechnicalMessage.PRODUCT_UPDATE_STOCK_FAILED,
                            businessException.getTechnicalMessage());
                    return true;
                })
                .verify();

        verify(productRepository, times(1))
                .getById(1L);
    }

    @Test
    void shouldErrorUpdateStockProductById() {
        given(productRepository.getById(1L))
                .willReturn(Mono.empty());

        Mono<Product> productResponse = productUseCase.updateStock(1L, 10);

        StepVerifier.create(productResponse)
                .expectErrorMatches(throwable -> {
                    BusinessException businessException = (BusinessException) throwable;
                    Assertions.assertEquals(TechnicalMessage.PRODUCT_DOES_NOT_EXIST,
                            businessException.getTechnicalMessage());
                    return true;
                })
                .verify();

        verify(productRepository, times(1))
                .getById(1L);
    }

    @Test
    void shouldTopStockProduct() {
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
        given(franchiseUseCase.getById(FRANCHISE_ID))
                .willReturn(Mono.just(buildFranchise()));

        given(productRepository.getTopProductsByFranchise(FRANCHISE_ID))
                .willReturn(Mono.just(productViews));

        Mono<List<ProductView>> Products = productUseCase.getTopStockProductsByFranchise(FRANCHISE_ID);

        StepVerifier.create(Products)
                .expectNext(productViews)
                .verifyComplete();

        verify(franchiseUseCase, times(1))
                .getById(FRANCHISE_ID);

        verify(productRepository, times(1))
                .getTopProductsByFranchise(FRANCHISE_ID);
    }

    @Test
    void shouldErrorTopStockProduct() {
        Long FRANCHISE_ID = 1L;
        given(franchiseUseCase.getById(FRANCHISE_ID))
                .willReturn(Mono.empty());

        Mono<List<ProductView>> Products = productUseCase.getTopStockProductsByFranchise(FRANCHISE_ID);

        StepVerifier.create(Products)
                .expectErrorMatches(throwable -> {
                    BusinessException businessException = (BusinessException) throwable;
                    Assertions.assertEquals(TechnicalMessage.FRANCHISE_DOES_NOT_EXIST,
                            businessException.getTechnicalMessage());
                    return true;
                })
                .verify();

        verify(franchiseUseCase, times(1))
                .getById(FRANCHISE_ID);
    }

    private Product buildProduct() {
        return Product.builder()
                .id(1L)
                .name("Producto")
                .stock(1)
                .branchId(1L)
                .build();
    }

    private Branch buildBranch() {
        return Branch.builder()
                .id(1L)
                .name("Sucursal")
                .franchiseId(1L)
                .build();
    }

    private Franchise buildFranchise() {
        return Franchise.builder()
                .id(1L)
                .name("Franquicia")
                .build();
    }

}