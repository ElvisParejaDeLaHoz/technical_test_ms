package co.com.test.model.exceptions;

import co.com.test.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends TechnicalTestException {

    public TechnicalException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }

    public TechnicalException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause, technicalMessage);
    }
}
