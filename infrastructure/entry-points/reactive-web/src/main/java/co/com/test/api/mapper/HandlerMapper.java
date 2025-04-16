package co.com.test.api.mapper;

import co.com.test.api.branch.dto.BranchRequest;
import co.com.test.api.branch.dto.BranchResponse;
import co.com.test.api.franchise.dto.FranchiseRequest;
import co.com.test.api.franchise.dto.FranchiseResponse;
import co.com.test.api.product.dto.ProductRequest;
import co.com.test.api.product.dto.ProductResponse;
import co.com.test.api.product.dto.ProductViewResponse;
import co.com.test.model.branch.Branch;
import co.com.test.model.branch.BranchParam;
import co.com.test.model.franchise.Franchise;
import co.com.test.model.franchise.FranchiseParam;
import co.com.test.model.product.Product;
import co.com.test.model.product.ProductParam;
import co.com.test.model.product.ProductView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HandlerMapper {
    HandlerMapper MAPPER = Mappers.getMapper(HandlerMapper.class);

    FranchiseParam toFranchiseParam(FranchiseRequest request);
    FranchiseResponse toFranchiseResponse(Franchise franchise);
    BranchParam toBranchParam(BranchRequest branchRequest);
    BranchResponse toBranchResponse(Branch branch);
    ProductParam toProductParam(ProductRequest productRequest);
    ProductResponse toProductResponse(Product product);
    List<ProductViewResponse> toProductViewResponse(List<ProductView> productViews);
}
