package co.com.test.r2dbc.franchise;

import co.com.test.r2dbc.model.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MyFranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {

}
