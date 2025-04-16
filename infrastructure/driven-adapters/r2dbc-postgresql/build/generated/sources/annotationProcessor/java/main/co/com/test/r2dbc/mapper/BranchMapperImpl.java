package co.com.test.r2dbc.mapper;

import co.com.test.model.branch.Branch;
import co.com.test.model.branch.BranchParam;
import co.com.test.r2dbc.model.BranchEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-15T23:06:52-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 17.0.11 (Oracle Corporation)"
)
public class BranchMapperImpl implements BranchMapper {

    @Override
    public BranchEntity toBranchEntity(BranchParam branch) {
        if ( branch == null ) {
            return null;
        }

        BranchEntity.BranchEntityBuilder branchEntity = BranchEntity.builder();

        branchEntity.name( branch.getName() );
        branchEntity.franchiseId( branch.getFranchiseId() );

        return branchEntity.build();
    }

    @Override
    public BranchEntity toBranchEntity(Branch branch) {
        if ( branch == null ) {
            return null;
        }

        BranchEntity.BranchEntityBuilder branchEntity = BranchEntity.builder();

        branchEntity.id( branch.getId() );
        branchEntity.name( branch.getName() );
        branchEntity.franchiseId( branch.getFranchiseId() );

        return branchEntity.build();
    }

    @Override
    public Branch toBranch(BranchEntity branchEntity) {
        if ( branchEntity == null ) {
            return null;
        }

        Branch.BranchBuilder branch = Branch.builder();

        branch.id( branchEntity.getId() );
        branch.name( branchEntity.getName() );
        branch.franchiseId( branchEntity.getFranchiseId() );

        return branch.build();
    }
}
