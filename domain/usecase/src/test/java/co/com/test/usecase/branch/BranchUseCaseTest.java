package co.com.test.usecase.branch;

import co.com.test.model.branch.Branch;
import co.com.test.model.branch.BranchParam;
import co.com.test.model.branch.gateways.BranchRepository;
import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.franchise.Franchise;
import co.com.test.usecase.franchise.FranchiseUseCase;
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
class BranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseUseCase franchiseUseCase;

    private BranchUseCase branchUseCase;

    @BeforeEach
    void setUp() {
        branchUseCase = new BranchUseCase(branchRepository, franchiseUseCase);
    }

    @Test
    void shouldCreateBranch() {
        BranchParam branchParam = BranchParam.builder()
                .name("Sucursal")
                .franchiseId(1L)
                .build();

        given(franchiseUseCase.getById(1L))
                .willReturn(Mono.just(buildFranchise()));

        given(branchRepository.save(branchParam))
                .willReturn(Mono.just(buildBranch()));

        Mono<Branch> branchResponse = branchUseCase.create(branchParam);

        StepVerifier.create(branchResponse)
                .expectNext(buildBranch())
                .verifyComplete();

        verify(branchRepository, times(1))
                .save(branchParam);

        verify(franchiseUseCase, times(1))
                .getById(1L);
    }

    @Test
    void shouldReturnErrorCreateBranch() {
        BranchParam branchParam = BranchParam.builder()
                .name("Sucursal")
                .franchiseId(1L)
                .build();

        given(franchiseUseCase.getById(1L))
                .willReturn(Mono.empty());

        Mono<Branch> branchResponse = branchUseCase.create(branchParam);

        StepVerifier.create(branchResponse)
                .expectError(BusinessException.class)
                .verify();

        verify(franchiseUseCase, times(1))
                .getById(1L);
    }

    @Test
    void shouldUpdateBranch() {
        BranchParam branchParam = BranchParam.builder()
                .name("Sucursal 1")
                .franchiseId(1L)
                .build();

        Branch branch = buildBranch()
                .toBuilder()
                .name("Sucursal 1")
                .build();

        given(branchUseCase.get(1L))
                .willReturn(Mono.just(buildBranch()));

        given(branchRepository.update(any(Branch.class)))
                .willReturn(Mono.just(branch));

        Mono<Branch> branchResponse = branchUseCase.updateName(1L, branchParam.getName());

        StepVerifier.create(branchResponse)
                .expectNext(branch)
                .verifyComplete();

        verify(branchRepository, times(1))
                .update(any(Branch.class));

        verify(branchRepository, times(1))
                .get(1L);
    }

    @Test
    void shouldReturnErrorUpdateBranch() {
        BranchParam branchParam = BranchParam.builder()
                .name("Sucursal 1")
                .franchiseId(1L)
                .build();

        given(branchUseCase.get(1L))
                .willReturn(Mono.empty());

        Mono<Branch> branchResponse = branchUseCase.updateName(1L, branchParam.getName());

        StepVerifier.create(branchResponse)
                .expectError(BusinessException.class)
                .verify();

        verify(branchRepository, times(1))
                .get(1L);
    }

    private Franchise buildFranchise() {
        return Franchise.builder()
                .id(1L)
                .name("Franquicia")
                .build();
    }

    private Branch buildBranch() {
        return Branch.builder()
                .id(1L)
                .name("Sucursal")
                .franchiseId(1L)
                .build();
    }

}