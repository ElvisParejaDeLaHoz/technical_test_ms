package co.com.test.usecase.product;

import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.product.Product;
import co.com.test.model.product.ProductParam;
import co.com.test.model.product.ProductView;
import co.com.test.model.product.gateways.ProductRepository;
import co.com.test.usecase.branch.BranchUseCase;
import co.com.test.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchUseCase branchUseCase;
    private final FranchiseUseCase franchiseUseCase;

    public Mono<Product> create(ProductParam productParam) {
        return branchUseCase.get(productParam.getBranchId())
                .flatMap(branch -> productRepository.save(productParam))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_DOES_NOT_EXIST)));
    }

    public Mono<Boolean> delete(Long id) {
        return getById(id)
                .flatMap(product -> productRepository.delete(id)
                        .flatMap(isValid -> {
                            if (isValid) {
                                return Mono.just(true);
                            }
                            return Mono.error(new BusinessException(TechnicalMessage.PRODUCT_DELETE_FAILED));
                        }))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_DOES_NOT_EXIST)));
    }

    public Mono<Product> updateStock(Long id, int stock) {
        return getById(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_DOES_NOT_EXIST)))
                .flatMap(product -> productRepository.updateStock(id, stock)
                        .filter(isValid -> isValid)
                        .flatMap(isValid -> getById(id))
                        .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_UPDATE_STOCK_FAILED))));
    }

    public Mono<Product> getById(Long id) {
        return productRepository.getById(id);
    }

    public Mono<List<ProductView>> getTopStockProductsByFranchise(Long id) {
        return franchiseUseCase.getById(id)
                .flatMap(franchise -> productRepository.getTopProductsByFranchise(id))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_DOES_NOT_EXIST)));
    }
}
