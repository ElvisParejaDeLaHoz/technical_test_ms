package co.com.test.usecase.franchise;

import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.franchise.Franchise;
import co.com.test.model.franchise.FranchiseParam;
import co.com.test.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> create(FranchiseParam franchiseParam) {
        return franchiseRepository.save(franchiseParam);
    }

    public Mono<Franchise> getById(Long id) {
        return franchiseRepository.findById(id);
    }

    public Mono<Franchise> update(FranchiseParam franchiseParam, Long id) {
        return getById(id)
                .flatMap(franchise -> franchiseRepository.update(franchise.toBuilder()
                        .name(franchiseParam.getName()).build()))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_DOES_NOT_EXIST)));
    }
}
