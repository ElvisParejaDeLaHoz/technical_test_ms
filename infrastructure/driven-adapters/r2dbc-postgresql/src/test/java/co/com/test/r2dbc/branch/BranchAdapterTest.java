package co.com.test.r2dbc.branch;

import co.com.test.model.branch.Branch;
import co.com.test.model.branch.BranchParam;
import co.com.test.model.exceptions.BusinessException;
import co.com.test.model.exceptions.TechnicalException;
import co.com.test.r2dbc.model.BranchEntity;
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
class BranchAdapterTest {

    @Mock
    public MyBranchRepository myBranchRepository;
    public BranchAdapter branchAdapter;

    @BeforeEach
    void beforeAll() {
        branchAdapter = new BranchAdapter(myBranchRepository);
    }

    @Test
    void shouldSavedSuccessfully() {
        BranchParam branchParam = BranchParam.builder()
                .name("Sucursal")
                .franchiseId(1L)
                .build();

        given(myBranchRepository.save(any(BranchEntity.class)))
                .willReturn(Mono.just(BranchEntity.builder()
                        .id(1L)
                        .name("Sucursal")
                        .franchiseId(1L)
                        .build()));

        Mono<Branch> saved = branchAdapter.save(branchParam);

        StepVerifier.create(saved)
                .expectNext(buildBranch())
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenSevingByDuplicity() {
        BranchParam branchParam = BranchParam.builder()
                .name("Sucursal")
                .franchiseId(1L)
                .build();

        given(myBranchRepository.save(any(BranchEntity.class)))
                .willReturn(Mono.error(new DuplicateKeyException("An error has been occurred")));

        Mono<Branch> saved = branchAdapter.save(branchParam);

        StepVerifier.create(saved)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenSevingByTransientDataAccessException() {
        BranchParam branchParam = BranchParam.builder()
                .name("Sucursal")
                .franchiseId(1L)
                .build();

        given(myBranchRepository.save(any(BranchEntity.class)))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));
        Mono<Branch> saved = branchAdapter.save(branchParam);

        StepVerifier.create(saved)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldReturnBranch() {
        given(myBranchRepository.findById(1L))
                .willReturn(Mono.just(BranchEntity.builder()
                        .id(1L)
                        .name("Sucursal")
                        .franchiseId(1L)
                        .build()));

        Mono<Branch> branchResponse = branchAdapter.get(1L);

            StepVerifier.create(branchResponse)
                    .expectNext(buildBranch())
                    .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenGetByTransientDataAccessException() {

        given(myBranchRepository.findById(1L))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Branch> saved = branchAdapter.get(1L);

        StepVerifier.create(saved)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void shouldUpdatedSuccessfully() {
        Branch branch = buildBranch()
                .toBuilder()
                .name("Sucursal 1")
                .build();

        given(myBranchRepository.save(any(BranchEntity.class)))
                .willReturn(Mono.just(BranchEntity.builder()
                        .id(1L)
                        .name("Sucursal 1")
                        .franchiseId(1L)
                        .build()));

        Mono<Branch> updated = branchAdapter.update(branch);

        StepVerifier.create(updated)
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenUpdatingByDuplicity() {
        Branch branch = buildBranch()
                .toBuilder()
                .name("Sucursal 1")
                .build();

        given(myBranchRepository.save(any(BranchEntity.class)))
                .willReturn(Mono.error(new DuplicateKeyException("An error has been occurred")));

        Mono<Branch> update = branchAdapter.update(branch);

        StepVerifier.create(update)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenUpdatingByTransientDataAccessException() {
        Branch branch = buildBranch()
                .toBuilder()
                .name("Sucursal 1")
                .build();

        given(myBranchRepository.save(any(BranchEntity.class)))
                .willReturn(Mono.error(new TransientDataAccessException("An error has been occurred") {
                }));

        Mono<Branch> update = branchAdapter.update(branch);

        StepVerifier.create(update)
                .expectError(TechnicalException.class)
                .verify();
    }

    private Branch buildBranch() {
        return Branch.builder()
                .id(1L)
                .name("Sucursal")
                .franchiseId(1L)
                .build();
    }

}