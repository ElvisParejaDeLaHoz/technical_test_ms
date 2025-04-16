package co.com.test.r2dbc.franchise;

import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.exceptions.TechnicalException;
import co.com.test.model.franchise.Franchise;
import co.com.test.model.franchise.FranchiseParam;
import co.com.test.r2dbc.model.FranchiseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FranchiseAdapterTest {

    @Mock
    public MyFranchiseRepository myFranchiseRepository;
    public FranchiseAdapter adapter;

    @BeforeEach
    void beforeAll() {
        adapter = new FranchiseAdapter(myFranchiseRepository);
    }

    @Test
    void shouldSavedSuccessfully() {
        FranchiseParam franchiseParam = FranchiseParam.builder()
                .name("Franquicia")
                .build();

        given(myFranchiseRepository.save(any(FranchiseEntity.class)))
                .willReturn(Mono.just(FranchiseEntity.builder()
                        .id(1L)
                        .name("Franquicia")
                        .build()));

        Mono<Franchise> saved = adapter.save(franchiseParam);

        StepVerifier.create(saved)
                .expectNext(buildFranchise())
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenSevingByDuplicity() {
        FranchiseParam franchiseParam = FranchiseParam.builder()
                .name("Franquicia")
                .build();

        given(myFranchiseRepository.save(any(FranchiseEntity.class)))
                .willReturn(Mono.error(new DuplicateKeyException("An error has been occurred")));

        Mono<Franchise> saved = adapter.save(franchiseParam);

        StepVerifier.create(saved)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenSevingByTransientDataAccessException() {
        FranchiseParam franchiseParam = FranchiseParam.builder()
                .name("Franquicia")
                .build();

        given(myFranchiseRepository.save(any(FranchiseEntity.class)))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Franchise> saved = adapter.save(franchiseParam);

        StepVerifier.create(saved)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldReturnFranchise() {
        given(myFranchiseRepository.findById(1L))
                .willReturn(Mono.just(FranchiseEntity.builder()
                        .id(1L)
                        .name("Franquicia")
                        .build()));

        Mono<Franchise> saved = adapter.findById(1L);

        StepVerifier.create(saved)
                .expectNext(buildFranchise())
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenGetByTransientDataAccessException() {

        given(myFranchiseRepository.findById(1L))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Franchise> saved = adapter.findById(1L);

        StepVerifier.create(saved)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldUpdatedSuccessfully() {
        Franchise franchise = buildFranchise()
                .toBuilder()
                .name("Franquicia 1")
                .build();

        given(myFranchiseRepository.save(any(FranchiseEntity.class)))
                .willReturn(Mono.just(FranchiseEntity.builder()
                        .id(1L)
                        .name("Franquicia 1")
                        .build()));

        Mono<Franchise> updated = adapter.update(franchise);

        StepVerifier.create(updated)
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenUpdatingByDuplicity() {
        Franchise franchise = buildFranchise()
                .toBuilder()
                .name("Franquicia 1")
                .build();

        given(myFranchiseRepository.save(any(FranchiseEntity.class)))
                .willReturn(Mono.error(new DuplicateKeyException("An error has been occurred")));

        Mono<Franchise> update = adapter.update(franchise);

        StepVerifier.create(update)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenUpdatingByTransientDataAccessException() {
        Franchise franchise = buildFranchise()
                .toBuilder()
                .name("Franquicia 1")
                .build();

        given(myFranchiseRepository.save(any(FranchiseEntity.class)))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Franchise> update = adapter.update(franchise);

        StepVerifier.create(update)
                .expectError(TechnicalException.class)
                .verify();
    }

    private Franchise buildFranchise() {
        return Franchise.builder()
                .id(1L)
                .name("Franquicia")
                .build();
    }
}