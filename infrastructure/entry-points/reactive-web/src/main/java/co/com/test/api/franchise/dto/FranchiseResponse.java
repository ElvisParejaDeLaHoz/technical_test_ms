package co.com.test.api.franchise.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FranchiseResponse {
    Long id;
    String name;
}
