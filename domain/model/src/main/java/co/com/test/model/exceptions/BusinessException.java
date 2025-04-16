package co.com.test.model.exceptions;

import co.com.test.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends TechnicalTestException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }

    public BusinessException(String message, TechnicalMessage technicalMessage) {
        super(message, technicalMessage);
    }
}
