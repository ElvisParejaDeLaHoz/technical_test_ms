package co.com.test.api.product.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ProductViewResponse {
    Long franchiseId;
    String franchiseName;
    Long branchId;
    String branchName;
    String productName;
    int totalStock;
}
