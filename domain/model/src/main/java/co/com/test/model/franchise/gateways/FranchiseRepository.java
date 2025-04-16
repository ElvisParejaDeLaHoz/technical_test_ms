package co.com.test.model.franchise.gateways;

import co.com.test.model.franchise.Franchise;
import co.com.test.model.franchise.FranchiseParam;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> save(FranchiseParam franchiseParam);
    Mono<Franchise> findById(Long id);
    Mono<Franchise> update(Franchise franchise);
}
