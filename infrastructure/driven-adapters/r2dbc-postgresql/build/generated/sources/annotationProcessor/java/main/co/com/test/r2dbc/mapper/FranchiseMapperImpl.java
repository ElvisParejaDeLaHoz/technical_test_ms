package co.com.test.r2dbc.mapper;

import co.com.test.model.franchise.Franchise;
import co.com.test.model.franchise.FranchiseParam;
import co.com.test.r2dbc.model.FranchiseEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-16T10:21:31-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
public class FranchiseMapperImpl implements FranchiseMapper {

    @Override
    public FranchiseEntity toFranchiseEntity(FranchiseParam franchiseParam) {
        if ( franchiseParam == null ) {
            return null;
        }

        FranchiseEntity.FranchiseEntityBuilder franchiseEntity = FranchiseEntity.builder();

        franchiseEntity.name( franchiseParam.getName() );

        return franchiseEntity.build();
    }

    @Override
    public Franchise toFranchise(FranchiseEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Franchise.FranchiseBuilder franchise = Franchise.builder();

        franchise.id( entity.getId() );
        franchise.name( entity.getName() );

        return franchise.build();
    }

    @Override
    public FranchiseEntity toFranchiseEntity(Franchise franchise) {
        if ( franchise == null ) {
            return null;
        }

        FranchiseEntity.FranchiseEntityBuilder franchiseEntity = FranchiseEntity.builder();

        franchiseEntity.id( franchise.getId() );
        franchiseEntity.name( franchise.getName() );

        return franchiseEntity.build();
    }
}
