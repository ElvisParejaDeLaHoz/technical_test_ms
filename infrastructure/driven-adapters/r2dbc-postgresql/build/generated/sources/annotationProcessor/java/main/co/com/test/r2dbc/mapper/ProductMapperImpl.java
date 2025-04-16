package co.com.test.r2dbc.mapper;

import co.com.test.model.product.Product;
import co.com.test.model.product.ProductParam;
import co.com.test.model.product.ProductView;
import co.com.test.r2dbc.model.ProductEntity;
import co.com.test.r2dbc.model.dto.ProductViewDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-15T23:06:52-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 17.0.11 (Oracle Corporation)"
)
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductEntity toProductEntity(ProductParam productParam) {
        if ( productParam == null ) {
            return null;
        }

        ProductEntity.ProductEntityBuilder productEntity = ProductEntity.builder();

        productEntity.name( productParam.getName() );
        productEntity.stock( productParam.getStock() );
        productEntity.branchId( productParam.getBranchId() );

        return productEntity.build();
    }

    @Override
    public Product toProduct(ProductEntity productEntity) {
        if ( productEntity == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( productEntity.getId() );
        product.name( productEntity.getName() );
        product.stock( productEntity.getStock() );
        product.branchId( productEntity.getBranchId() );

        return product.build();
    }

    @Override
    public ProductView toProductView(ProductViewDTO productViewDTO) {
        if ( productViewDTO == null ) {
            return null;
        }

        ProductView.ProductViewBuilder productView = ProductView.builder();

        productView.franchiseId( productViewDTO.getFranchiseId() );
        productView.franchiseName( productViewDTO.getFranchiseName() );
        productView.branchId( productViewDTO.getBranchId() );
        productView.branchName( productViewDTO.getBranchName() );
        productView.productName( productViewDTO.getProductName() );
        productView.totalStock( productViewDTO.getTotalStock() );

        return productView.build();
    }
}
