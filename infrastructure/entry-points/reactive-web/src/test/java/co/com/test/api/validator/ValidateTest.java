package co.com.test.api.validator;

import co.com.test.api.branch.dto.BranchRequest;
import co.com.test.api.branch.dto.BranchUpdateRequest;
import co.com.test.api.franchise.dto.FranchiseRequest;
import co.com.test.api.product.dto.ProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class ValidateTest {

    private BranchUpdateRequestValidate branchUpdateRequestValidate = new BranchUpdateRequestValidate();
    private BranchValidate branchValidate = new BranchValidate();
    private FranchiseValidate franchiseValidate = new FranchiseValidate();
    private ProductValidate productValidate = new ProductValidate();

    @Test
    void shouldCheckSupportsFunction() {

        boolean branchUpdateRequestSupports = branchUpdateRequestValidate
                .supports(BranchUpdateRequest.class);
        boolean genericBranchUpdateRequestSupport = branchUpdateRequestValidate
                .supports(String.class);

        boolean branchRequestSupports = branchValidate
                .supports(BranchRequest.class);
        boolean genericBranchRequestSupport = branchUpdateRequestValidate
                .supports(String.class);

        boolean franchiseRequestSupports = franchiseValidate
                .supports(FranchiseRequest.class);
        boolean genericFranchiseRequestSupport = franchiseValidate
                .supports(String.class);

        boolean productRequestRequestSupports = productValidate
                .supports(ProductRequest.class);
        boolean genericProductRequestSupport = productValidate
                .supports(String.class);

        Assertions.assertTrue(branchUpdateRequestSupports);
        Assertions.assertFalse(genericBranchUpdateRequestSupport);
        Assertions.assertTrue(branchRequestSupports);
        Assertions.assertFalse(genericBranchRequestSupport);
        Assertions.assertTrue(franchiseRequestSupports);
        Assertions.assertFalse(genericFranchiseRequestSupport);
        Assertions.assertTrue(productRequestRequestSupports);
        Assertions.assertFalse(genericProductRequestSupport);

    }

    @Test
    void shouldValidateArguments() {

        BranchUpdateRequest branchUpdateRequest = BranchUpdateRequest
                .builder().name("Sucursal").build();
        Errors errorsBranchUpdateRequest = new BeanPropertyBindingResult(
                branchUpdateRequest, BranchUpdateRequest.class.getName());
        branchUpdateRequestValidate.validate(branchUpdateRequest, errorsBranchUpdateRequest);

        BranchRequest branchRequest = BranchRequest
                .builder().name("Sucursal").franchiseId(1L).build();
        Errors errorsBranchRequest = new BeanPropertyBindingResult(
                branchRequest, BranchRequest.class.getName());
        branchValidate.validate(branchRequest, errorsBranchRequest);

        FranchiseRequest franchiseRequest = FranchiseRequest
                .builder().name("Franquicia").build();
        Errors errorsFranchiseRequest = new BeanPropertyBindingResult(
                franchiseRequest, FranchiseRequest.class.getName());
        franchiseValidate.validate(franchiseRequest, errorsFranchiseRequest);

        ProductRequest productRequest = ProductRequest
                .builder().name("producto").stock(1).branchId(1L).build();
        Errors errorsProductRequest = new BeanPropertyBindingResult(
                productRequest, ProductRequest.class.getName());
        productValidate.validate(productRequest, errorsProductRequest);


        Assertions.assertEquals(0, errorsBranchUpdateRequest.getErrorCount());
        Assertions.assertEquals(0, errorsBranchRequest.getErrorCount());
        Assertions.assertEquals(0, errorsFranchiseRequest.getErrorCount());
        Assertions.assertEquals(0, errorsProductRequest.getErrorCount());
    }

    @Test
    void shouldInValidateArguments() {
        BranchUpdateRequest branchUpdateRequest = BranchUpdateRequest.builder().build();
        Errors errorsBranchUpdateRequest = new BeanPropertyBindingResult(
                branchUpdateRequest, BranchUpdateRequest.class.getName());

        branchUpdateRequestValidate.validate(branchUpdateRequest, errorsBranchUpdateRequest);

        BranchRequest branchRequest = BranchRequest.builder().build();
        Errors errorsBranchRequest = new BeanPropertyBindingResult(
                branchRequest, BranchRequest.class.getName());
        branchValidate.validate(branchRequest, errorsBranchRequest);

        FranchiseRequest franchiseRequest = FranchiseRequest
                .builder().build();
        Errors errorsFranchiseRequest = new BeanPropertyBindingResult(
                franchiseRequest, FranchiseRequest.class.getName());
        franchiseValidate.validate(franchiseRequest, errorsFranchiseRequest);

        ProductRequest productRequest = ProductRequest
                .builder().build();
        Errors errorsProductRequest = new BeanPropertyBindingResult(
                productRequest, ProductRequest.class.getName());
        productValidate.validate(productRequest, errorsProductRequest);

        Assertions.assertEquals(1, errorsBranchUpdateRequest.getErrorCount());
        Assertions.assertEquals(2, errorsBranchRequest.getErrorCount());
        Assertions.assertEquals(1, errorsFranchiseRequest.getErrorCount());
        Assertions.assertEquals(2, errorsProductRequest.getErrorCount());
    }

}