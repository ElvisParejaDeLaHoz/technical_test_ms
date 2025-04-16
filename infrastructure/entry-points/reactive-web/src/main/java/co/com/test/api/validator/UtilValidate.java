package co.com.test.api.validator;

import co.com.test.api.branch.dto.BranchUpdateRequest;
import co.com.test.api.product.dto.ProductUpdateStockRequest;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@UtilityClass
public class UtilValidate {

    public static Mono<Boolean> validateRequestUpdateStock(ProductUpdateStockRequest productUpdateStockRequest, Long id) {
        boolean hasErrors = productUpdateStockRequest == null || id == null || id <= 0;
        return hasErrors ? Mono.just(true) : Mono.empty();
    }

    public static Mono<Boolean> validateRequestById(Long id) {
        boolean hasErrors = isHasErrors(id);
        return hasErrors ? Mono.just(true) : Mono.empty();
    }

    public static Mono<Errors> validateBranchUpdateRequest(BranchUpdateRequest branchUpdateRequest, Long id) {

        Errors errors = new BeanPropertyBindingResult(branchUpdateRequest, BranchUpdateRequest.class.getName());

        Validator validator = new BranchUpdateRequestValidate();
        validator.validate(branchUpdateRequest, errors);
        boolean hasErrors = isHasErrors(id);

        if (hasErrors) {
            errors.rejectValue("FranchiseId", "Campo obligatorio y debe ser mayor que 0");
        }

        if (hasErrors || errors.getErrorCount() > 0) {
            return Mono.just(errors);
        }

        return Mono.empty();
    }

    private static boolean isHasErrors(Long id) {
        return id == null || id <= 0;
    }
}
