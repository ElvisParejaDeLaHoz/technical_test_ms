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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-16T10:21:32-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
public class HandlerMapperImpl implements HandlerMapper {

    @Override
    public FranchiseParam toFranchiseParam(FranchiseRequest request) {
        if ( request == null ) {
            return null;
        }

        FranchiseParam.FranchiseParamBuilder franchiseParam = FranchiseParam.builder();

        franchiseParam.name( request.getName() );

        return franchiseParam.build();
    }

    @Override
    public FranchiseResponse toFranchiseResponse(Franchise franchise) {
        if ( franchise == null ) {
            return null;
        }

        FranchiseResponse.FranchiseResponseBuilder franchiseResponse = FranchiseResponse.builder();

        franchiseResponse.id( franchise.getId() );
        franchiseResponse.name( franchise.getName() );

        return franchiseResponse.build();
    }

    @Override
    public BranchParam toBranchParam(BranchRequest branchRequest) {
        if ( branchRequest == null ) {
            return null;
        }

        BranchParam.BranchParamBuilder branchParam = BranchParam.builder();

        branchParam.name( branchRequest.getName() );
        branchParam.franchiseId( branchRequest.getFranchiseId() );

        return branchParam.build();
    }

    @Override
    public BranchResponse toBranchResponse(Branch branch) {
        if ( branch == null ) {
            return null;
        }

        BranchResponse.BranchResponseBuilder branchResponse = BranchResponse.builder();

        branchResponse.id( branch.getId() );
        branchResponse.name( branch.getName() );
        branchResponse.franchiseId( branch.getFranchiseId() );

        return branchResponse.build();
    }

    @Override
    public ProductParam toProductParam(ProductRequest productRequest) {
        if ( productRequest == null ) {
            return null;
        }

        ProductParam.ProductParamBuilder productParam = ProductParam.builder();

        productParam.branchId( productRequest.getBranchId() );
        productParam.name( productRequest.getName() );
        productParam.stock( productRequest.getStock() );

        return productParam.build();
    }

    @Override
    public ProductResponse toProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( product.getId() );
        productResponse.name( product.getName() );
        productResponse.stock( product.getStock() );
        productResponse.branchId( product.getBranchId() );

        return productResponse.build();
    }

    @Override
    public List<ProductViewResponse> toProductViewResponse(List<ProductView> productViews) {
        if ( productViews == null ) {
            return null;
        }

        List<ProductViewResponse> list = new ArrayList<ProductViewResponse>( productViews.size() );
        for ( ProductView productView : productViews ) {
            list.add( productViewToProductViewResponse( productView ) );
        }

        return list;
    }

    protected ProductViewResponse productViewToProductViewResponse(ProductView productView) {
        if ( productView == null ) {
            return null;
        }

        ProductViewResponse.ProductViewResponseBuilder productViewResponse = ProductViewResponse.builder();

        productViewResponse.franchiseId( productView.getFranchiseId() );
        productViewResponse.franchiseName( productView.getFranchiseName() );
        productViewResponse.branchId( productView.getBranchId() );
        productViewResponse.branchName( productView.getBranchName() );
        productViewResponse.productName( productView.getProductName() );
        productViewResponse.totalStock( productView.getTotalStock() );

        return productViewResponse.build();
    }
}
