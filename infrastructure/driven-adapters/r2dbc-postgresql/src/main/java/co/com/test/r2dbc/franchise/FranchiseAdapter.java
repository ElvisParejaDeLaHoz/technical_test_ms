package co.com.test.r2dbc.franchise;

import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.exceptions.TechnicalException;
import co.com.test.model.franchise.Franchise;
import co.com.test.model.franchise.FranchiseParam;
import co.com.test.model.franchise.gateways.FranchiseRepository;
import co.com.test.r2dbc.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.kv;


@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseAdapter implements FranchiseRepository {

    private final MyFranchiseRepository myFranchiseRepository;

    @Override
    public Mono<Franchise> save(FranchiseParam franchiseParam) {
        return myFranchiseRepository
                .save(FranchiseMapper.INSTANCE.toFranchiseEntity(franchiseParam))
                .map(FranchiseMapper.INSTANCE::toFranchise)
                .doOnSubscribe(subscription -> log.info("Save franchise request", kv("saveFranchiseRequest", franchiseParam)))
                .doOnSuccess(franchise -> log.info("Saved franchise response", kv("savedFranchiseResponse", franchise)))
                .doOnError(throwable -> log.error("Save franchise error", throwable))
                .onErrorMap(DuplicateKeyException.class, error ->
                        new BusinessException(TechnicalMessage.FRANCHISE_ALREADY_EXISTS))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.FRANCHISE_CREATION_FAILED));
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return myFranchiseRepository.findById(id)
                .map(FranchiseMapper.INSTANCE::toFranchise)
                .doOnSubscribe(subscription -> log.info("Get by Id franchise request", kv("getFranchiseRequest", Map.of("id", id))))
                .doOnSuccess(franchise -> log.info("Get by Id franchise response", kv("getFranchiseResponse", franchise)))
                .doOnError(throwable -> log.error("Get by Id franchise error", throwable))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.FRANCHISE_GET_FAILED));
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return myFranchiseRepository
                .save(FranchiseMapper.INSTANCE.toFranchiseEntity(franchise))
                .map(FranchiseMapper.INSTANCE::toFranchise)
                .doOnSubscribe(subscription -> log.info("Update franchise request", kv("updateFranchiseRequest", franchise)))
                .doOnSuccess(franchiseResponse -> log.info("Updated franchise response", kv("updatedFranchiseResponse", franchiseResponse)))
                .doOnError(throwable -> log.error("Update franchise error", throwable))
                .onErrorMap(DuplicateKeyException.class, error ->
                        new BusinessException(TechnicalMessage.FRANCHISE_ALREADY_EXISTS_WITH_THIS_NAME))
                .onErrorMap(TransientDataAccessException.class, error ->
                        new TechnicalException(error, TechnicalMessage.FRANCHISE_UPDATE_FAILED));
    }
}
