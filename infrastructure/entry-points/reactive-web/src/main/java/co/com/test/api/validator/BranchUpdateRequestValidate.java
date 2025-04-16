package co.com.test.api.validator;

import co.com.test.api.branch.dto.BranchUpdateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

public class BranchUpdateRequestValidate implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BranchUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BranchUpdateRequest branchRequest = (BranchUpdateRequest) target;

        if (Objects.isNull(branchRequest.getName()) || branchRequest.getName().isEmpty()) {
            errors.rejectValue("name", "Campo obligatorio");
        }
    }
}
