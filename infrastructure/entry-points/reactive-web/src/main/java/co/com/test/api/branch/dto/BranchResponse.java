package co.com.test.api.branch.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BranchResponse {
    Long id;
    String name;
    Long franchiseId;
}
