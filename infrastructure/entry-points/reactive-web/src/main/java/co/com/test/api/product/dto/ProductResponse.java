package co.com.test.api.product.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ProductResponse {
    Long id;
    String name;
    int stock;
    Long branchId;
}
