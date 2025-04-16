package co.com.test.model.exceptions;

import co.com.test.model.enums.TechnicalMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TechnicalTestException extends RuntimeException {

    private final TechnicalMessage technicalMessage;

    public TechnicalTestException(String message, TechnicalMessage technicalMessage) {
        super(message);
        this.technicalMessage = technicalMessage;
    }

    public TechnicalTestException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause);
        this.technicalMessage = technicalMessage;
    }
}
