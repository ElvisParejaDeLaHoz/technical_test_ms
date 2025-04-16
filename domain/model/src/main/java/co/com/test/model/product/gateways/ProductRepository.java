package co.com.test.model.product.gateways;

import co.com.test.model.product.Product;
import co.com.test.model.product.ProductParam;
import co.com.test.model.product.ProductView;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductRepository {
    Mono<Product> save(ProductParam productParam);
    Mono<Boolean> delete(Long id);
    Mono<Product> getById(Long id);
    Mono<Boolean> updateStock(Long id, int stock);
    Mono<List<ProductView>> getTopProductsByFranchise(Long franchiseId);

}
