package co.com.test.api;

import co.com.test.api.dto.error.APIErrorResponse;
import co.com.test.api.dto.success.APISuccessResponse;
import co.com.test.model.enums.TechnicalMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public abstract class BaseHandler {

    public Mono<ServerResponse> buildSuccessResponse(HttpStatus status, Object response) {
        APISuccessResponse apiSuccessResponse = APISuccessResponse.builder()
                .metadata(buildSuccessMetadata())
                .data(response)
                .build();

        return ServerResponse.status(status).bodyValue(apiSuccessResponse);
    }

    public Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, TechnicalMessage technicalMessage) {
        APIErrorResponse errorResponse = APIErrorResponse.builder()
                .status(httpStatus.value())
                .code(technicalMessage.getCode())
                .message(technicalMessage.getMessage())
                .build();

        return ServerResponse.status(httpStatus).bodyValue(errorResponse);
    }

    private APISuccessResponse.Metadata buildSuccessMetadata() {
        return APISuccessResponse.Metadata.builder()
                .responseDataTime(LocalDateTime.now())
                .build();
    }
}
