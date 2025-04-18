package co.com.test.api.branch;

import co.com.test.api.branch.dto.BranchRequest;
import co.com.test.api.branch.dto.BranchUpdateRequest;
import co.com.test.api.mapper.HandlerMapper;
import co.com.test.api.BaseHandler;
import co.com.test.api.validator.UtilValidate;
import co.com.test.api.validator.BranchValidate;
import co.com.test.model.enums.TechnicalMessage;
import co.com.test.model.exceptions.TechnicalTestException;
import co.com.test.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class BranchHandler extends BaseHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchRequest.class)
                .flatMap(branchRequest -> {

                    Errors errors = new BeanPropertyBindingResult(branchRequest, BranchRequest.class.getName());

                    Validator validator = new BranchValidate();
                    validator.validate(branchRequest, errors);

                    if (errors.getErrorCount() > 0) {
                        return buildErrorResponse(HttpStatus.BAD_REQUEST, TechnicalMessage.INVALID_INPUT);
                    } else {
                        return branchUseCase.create(HandlerMapper.MAPPER.toBranchParam(branchRequest))
                                .flatMap(branch -> buildSuccessResponse(HttpStatus.OK,
                                        HandlerMapper.MAPPER.toBranchResponse(branch)))
                                .onErrorResume(TechnicalTestException.class, error ->
                                        buildErrorResponse(HttpStatus.BAD_REQUEST, error.getTechnicalMessage()))
                                .onErrorResume(throwable -> buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        TechnicalMessage.INTERNAL_ERROR));
                    }

                });
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Long branchId = Long.valueOf(serverRequest.pathVariable("id"));

        return serverRequest.bodyToMono(BranchUpdateRequest.class)
                .flatMap(branchUpdateRequest ->
                        UtilValidate.validateBranchUpdateRequest(branchUpdateRequest, branchId)
                                .flatMap(errors -> buildErrorResponse(HttpStatus.BAD_REQUEST, TechnicalMessage.INVALID_INPUT))
                                .switchIfEmpty(Mono.defer(() -> branchUseCase.updateName(branchId, branchUpdateRequest.getName())
                                        .flatMap(branch -> buildSuccessResponse(HttpStatus.OK, HandlerMapper.MAPPER.toBranchResponse(branch)))
                                        .onErrorResume(TechnicalTestException.class, error ->
                                                buildErrorResponse(HttpStatus.BAD_REQUEST, error.getTechnicalMessage()))
                                        .onErrorResume(throwable -> buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                                TechnicalMessage.INTERNAL_ERROR)))));
    }

}
