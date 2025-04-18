package co.com.test.r2dbc.product;

import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.exceptions.TechnicalException;
import co.com.test.model.product.Product;
import co.com.test.model.product.ProductParam;
import co.com.test.model.product.ProductView;
import co.com.test.model.product.gateways.ProductRepository;
import co.com.test.r2dbc.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.kv;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProductAdapter implements ProductRepository {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DELETE = "DELETE";
    private final MyProductRepository myProductRepository;

    @Override
    public Mono<Product> save(ProductParam productParam) {
        return myProductRepository.save(ProductMapper.INSTANCE.toProductEntity(productParam))
                .map(ProductMapper.INSTANCE::toProduct)
                .doOnSubscribe(subscription -> log.info("Save product request", kv("saveProductRequest", productParam)))
                .doOnSuccess(product -> log.info("Saved product response", kv("savedProductResponse", product)))
                .onErrorResume(DuplicateKeyException.class, error -> recoverDeletedRecord(productParam))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.PRODUCT_CREATION_FAILED))
                .doOnError(throwable -> log.error("Save product error", throwable));
    }

    @Override
    public Mono<Boolean> delete(Long id) {
        return myProductRepository.updateStatus(STATUS_DELETE, id)
                .doOnSubscribe(subscription -> log.info("Delete product request",
                        kv("deleteProductRequest", Map.of("id", id))))
                .doOnSuccess(aBoolean -> log.info("Deleted product response",
                        kv("deletedProductResponse", aBoolean)))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.PRODUCT_DELETE_FAILED))
                .doOnError(throwable -> log.error("Delete product error", throwable));
    }

    @Override
    public Mono<Product> getById(Long id) {
        return myProductRepository.findByIdAndStatus(id, STATUS_ACTIVE)
                .map(ProductMapper.INSTANCE::toProduct)
                .doOnSubscribe(subscription -> log.info("Get product request",
                        kv("getProductRequest", Map.of("id", id))))
                .doOnSuccess(product -> log.info("Get product response", kv("getProductResponse", product)))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.PRODUCT_GET_FAILED))
                .doOnError(throwable -> log.error("Get product error", throwable));
    }

    @Override
    public Mono<Boolean> updateStock(Long id, int stock) {
        return myProductRepository.updateStock(stock, id)
                .doOnSubscribe(subscription -> log.info("Update Stock product request",
                        kv("updateStockProductRequest", Map.of("id", id, "stock", stock))))
                .doOnSuccess(updated -> log.info("Updated Stock product response",
                        kv("updatedStockProductResponse", updated)))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.PRODUCT_UPDATE_STOCK_FAILED))
                .doOnError(throwable -> log.error("Update Stock product error", throwable));
    }

    @Override
    public Mono<List<ProductView>> getTopProductsByFranchise(Long franchiseId) {
        return myProductRepository.findTopStockProductsByFranchise(franchiseId, STATUS_ACTIVE)
                .map(ProductMapper.INSTANCE::toProductView)
                .collectList()
                .doOnSubscribe(subscription -> log.info("Get top products request",
                        kv("getTopProductsRequest", Map.of("franchiseId", franchiseId))))
                .doOnSuccess(product -> log.info("Get top products response", kv("getTopProductsResponse", product)))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.PRODUCT_GET_TOP_FAILED))
                .doOnError(throwable -> log.error("Get top products error", throwable));
    }

    private Mono<Product> recoverDeletedRecord(ProductParam productParam) {
        return myProductRepository.findByNameAndBranchIdAndStatus(productParam.getName(),
                        productParam.getBranchId(), STATUS_DELETE)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS)))
                .flatMap(productEntity ->
                        updateStockAndStatus(productEntity.getId(), STATUS_ACTIVE, productEntity.getStock())
                                .filter(updated -> updated.equals(Boolean.TRUE))
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_DOES_NOT_EXIST)))
                        .flatMap(updated -> myProductRepository.findById(productEntity.getId())
                                .flatMap(productEntityUpdate ->
                                        Mono.just(ProductMapper.INSTANCE.toProduct(productEntityUpdate)))
                                .doOnSuccess(product -> log.info("Product Recovery response", kv("productRecoveryResponse", product))))
                );
    }

    private Mono<Boolean> updateStockAndStatus(Long id,String status, int stock) {
        return myProductRepository.updateStockAndStatus(stock, status, id)
                .doOnSubscribe(subscription -> log.info("Update Stock product request",
                        kv("updateStockAndStatusProductRequest", Map.of("id", id, "status",
                                status,"stock", stock))))
                .doOnSuccess(updated -> log.info("Updated Stock product response",
                        kv("updatedStockAndStatusProductResponse", updated)))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.PRODUCT_UPDATE_STOCK_AND_STATUS_FAILED))
                .doOnError(throwable -> log.error("Update Stock And Status product error", throwable));
    }
}
