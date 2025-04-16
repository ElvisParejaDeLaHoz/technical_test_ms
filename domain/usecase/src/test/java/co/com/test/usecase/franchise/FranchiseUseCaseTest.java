package co.com.test.usecase.franchise;

import co.com.test.model.franchise.Franchise;
import co.com.test.model.franchise.FranchiseParam;
import co.com.test.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;
    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setUp() {
        franchiseUseCase = new FranchiseUseCase(franchiseRepository);
    }

    @Test
    void shouldReturnFranchise() {
        given(franchiseRepository.findById(1L))
                .willReturn(Mono.just(buildFranchise()));

        Mono<Franchise> franchiseResponse = franchiseUseCase.getById(1L);

        StepVerifier.create(franchiseResponse)
                .expectNext(buildFranchise())
                .verifyComplete();

        verify(franchiseRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldCreateFranchise() {
        FranchiseParam franchiseParam = FranchiseParam.builder()
                .name("Franquicia")
                .build();

        given(franchiseRepository.save(franchiseParam))
                .willReturn(Mono.just(buildFranchise()));

        Mono<Franchise> franchiseResponse = franchiseUseCase.create(franchiseParam);

        StepVerifier.create(franchiseResponse)
                .expectNext(buildFranchise())
                .verifyComplete();

        verify(franchiseRepository, times(1))
                .save(franchiseParam);
    }

    @Test
    void shouldUpdateFranchise() {
        FranchiseParam franchiseParam = FranchiseParam.builder()
                .name("Franquicia 2")
                .build();

        Franchise franchise = buildFranchise().toBuilder()
                .name("Franquicia 2").build();

        given(franchiseRepository.findById(1L))
                .willReturn(Mono.just(buildFranchise()));

        given(franchiseRepository.update(any(Franchise.class)))
                .willReturn(Mono.just(franchise));

        Mono<Franchise> franchiseResponse = franchiseUseCase.update(franchiseParam, 1L);

        StepVerifier.create(franchiseResponse)
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository, times(1))
                .findById(1L);

        verify(franchiseRepository, times(1))
                .update(buildFranchise().toBuilder()
                        .name("Franquicia 2").build());

    }

    private Franchise buildFranchise() {
        return Franchise.builder()
                .id(1L)
                .name("Franquicia")
                .build();
    }

}