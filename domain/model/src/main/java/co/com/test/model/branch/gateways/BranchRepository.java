package co.com.test.model.branch.gateways;

import co.com.test.model.branch.Branch;
import co.com.test.model.branch.BranchParam;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> save(BranchParam branchParam);
    Mono<Branch> get(Long id);
    Mono<Branch> update(Branch branch);
}
