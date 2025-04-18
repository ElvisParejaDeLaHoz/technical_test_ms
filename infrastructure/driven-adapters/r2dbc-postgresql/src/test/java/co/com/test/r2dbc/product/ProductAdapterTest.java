package co.com.test.r2dbc.product;

import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.exceptions.TechnicalException;
import co.com.test.model.product.Product;
import co.com.test.model.product.ProductParam;
import co.com.test.model.product.ProductView;
import co.com.test.r2dbc.model.ProductEntity;
import co.com.test.r2dbc.model.dto.ProductViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    @Mock
    public MyProductRepository productRepository;
    public ProductAdapter productAdapter;
    private final static String STATUS_ACTIVE = "ACTIVE";

    @BeforeEach
    void setUp() {
        productAdapter = new ProductAdapter(productRepository);
    }

    @Test
    void shouldSaveProduct() {
        ProductParam productParam = ProductParam.builder()
                .stock(1)
                .branchId(1L)
                .name("Producto")
                .build();

        given(productRepository.save(any(ProductEntity.class)))
                .willReturn(Mono.just(ProductEntity.builder()
                        .id(1L)
                        .name("Producto")
                        .stock(1)
                        .branchId(1L)
                        .build()));

        Mono<Product> saved = productAdapter.save(productParam);

        StepVerifier.create(saved)
                .expectNext(buildProduct())
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenSevingByDuplicity() {
        ProductParam productParam = ProductParam.builder()
                .stock(1)
                .branchId(1L)
                .name("Producto")
                .build();

        given(productRepository.save(any(ProductEntity.class)))
                .willReturn(Mono.error(new DuplicateKeyException("An error has been occurred")));

        given(productRepository.findByNameAndBranchIdAndStatus(any(), any(), any()))
                .willReturn(Mono.empty());

        Mono<Product> saved = productAdapter.save(productParam);

        StepVerifier.create(saved)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldCreateAProductByRecovery() {
        ProductParam productParam = ProductParam.builder()
                .stock(1)
                .branchId(1L)
                .name("Producto")
                .build();

        given(productRepository.save(any(ProductEntity.class)))
                .willReturn(Mono.error(new DuplicateKeyException("An error has been occurred")));

        given(productRepository.findByNameAndBranchIdAndStatus(any(), any(), any()))
                .willReturn(Mono.just(ProductEntity.builder()
                        .id(1L)
                        .name("Producto")
                        .stock(1)
                        .branchId(1L)
                        .build()));

        given(productRepository.updateStockAndStatus(anyInt(), anyString(), anyLong()))
                .willReturn(Mono.just(Boolean.TRUE));

        given(productRepository.findById(anyLong()))
                .willReturn(Mono.just(ProductEntity.builder()
                        .id(1L)
                        .name("Producto")
                        .stock(1)
                        .branchId(1L)
                        .build()));

        Mono<Product> productResponse = productAdapter.save(productParam);

        StepVerifier.create(productResponse)
                .expectNext(buildProduct())
                .verifyComplete();
    }

    @Test
    void shouldReturnAnErrorCreatingAProductByRecovery() {
        ProductParam productParam = ProductParam.builder()
                .stock(1)
                .branchId(1L)
                .name("Producto")
                .build();

        given(productRepository.save(any(ProductEntity.class)))
                .willReturn(Mono.error(new DuplicateKeyException("An error has been occurred")));

        given(productRepository.findByNameAndBranchIdAndStatus(any(), any(), any()))
                .willReturn(Mono.just(ProductEntity.builder()
                        .id(1L)
                        .name("Producto")
                        .stock(1)
                        .branchId(1L)
                        .build()));

        given(productRepository.updateStockAndStatus(anyInt(), anyString(), anyLong()))
                .willReturn(Mono.just(Boolean.FALSE));

        Mono<Product> productResponse = productAdapter.save(productParam);

        StepVerifier.create(productResponse)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenSevingByTransientDataAccessException() {
        ProductParam productParam = ProductParam.builder()
                .stock(1)
                .branchId(1L)
                .name("Producto")
                .build();

        given(productRepository.save(any(ProductEntity.class)))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {}));

        Mono<Product> saved = productAdapter.save(productParam);

        StepVerifier.create(saved)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldReturnProduct() {
        given(productRepository.findByIdAndStatus(1L, STATUS_ACTIVE))
                .willReturn(Mono.just(ProductEntity.builder()
                        .id(1L)
                        .name("Producto")
                        .stock(1)
                        .branchId(1L)
                        .build()));

        Mono<Product> productResponse = productAdapter.getById(1L);

        StepVerifier.create(productResponse)
                .expectNext(buildProduct())
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenGetByTransientDataAccessException() {
        given(productRepository.findByIdAndStatus(1L, STATUS_ACTIVE))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Product> productResponse = productAdapter.getById(1L);

        StepVerifier.create(productResponse)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldUpdatedStockSuccessfully() {
        Product product = buildProduct().toBuilder()
                .stock(10)
                .build();

        given(productRepository.updateStock(product.getStock(), product.getId()))
                .willReturn(Mono.just(Boolean.TRUE));

        Mono<Boolean> updated = productAdapter.updateStock(product.getId(), product.getStock());

        StepVerifier.create(updated)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenUpdateStockByTransientDataAccessException() {
        Product product = buildProduct().toBuilder()
                .stock(10)
                .build();

        given(productRepository.updateStock(product.getStock(), product.getId()))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Boolean> updated = productAdapter.updateStock(product.getId(), product.getStock());

        StepVerifier.create(updated)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenDeleteByTransientDataAccessException() {
        Product product = buildProduct();

        given(productRepository.updateStatus("DELETE", product.getId()))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Boolean> deleted = productAdapter.delete(product.getId());

        StepVerifier.create(deleted)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldDeletedStockSuccessfully() {
        Product product = buildProduct();

        given(productRepository.updateStatus("DELETE", product.getId()))
                .willReturn(Mono.just(Boolean.TRUE));

        Mono<Boolean> deleted = productAdapter.delete(product.getId());

        StepVerifier.create(deleted)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldGetTopStockSuccessfully() {
        Long FRANCHISE_ID = 1L;
        String FRANCHISE_NAME = "MC BONO";
        List<ProductViewDTO> productViewDTOS = List.of(
                ProductViewDTO.builder()
                        .franchiseId(FRANCHISE_ID)
                        .franchiseName(FRANCHISE_NAME)
                        .branchId(4L)
                        .branchName("LA 46")
                        .productName("GASEOSA")
                        .totalStock(2000)
                        .build(),
                ProductViewDTO.builder()
                        .franchiseId(FRANCHISE_ID)
                        .franchiseName(FRANCHISE_NAME)
                        .branchId(4L)
                        .branchName("LA 46")
                        .productName("GASEOSA COCA COLA 1.5L")
                        .totalStock(2000)
                        .build(),
                ProductViewDTO.builder()
                        .franchiseId(FRANCHISE_ID)
                        .franchiseName(FRANCHISE_NAME)
                        .branchId(5L)
                        .branchName("LA 14")
                        .productName("GASEOSA COCA COLA 1.5L")
                        .totalStock(2000)
                        .build()
        );

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
        Product product = buildProduct();

        given(productRepository.findTopStockProductsByFranchise(product.getId(), STATUS_ACTIVE))
                .willReturn(Flux.fromIterable(productViewDTOS));

        Mono<List<ProductView>> top = productAdapter
                .getTopProductsByFranchise(product.getId());

        StepVerifier.create(top)
                .expectNext(productViews)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenGetTopStockByTransientDataAccessException() {;
        Product product = buildProduct();

        given(productRepository.findTopStockProductsByFranchise(product.getId(), STATUS_ACTIVE))
                .willReturn(Flux.error(new TransientDataAccessException("An error has been occurred") {}));

        Mono<List<ProductView>> top = productAdapter
                .getTopProductsByFranchise(product.getId());

        StepVerifier.create(top)
                .expectError(TechnicalException.class)
                .verify();
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