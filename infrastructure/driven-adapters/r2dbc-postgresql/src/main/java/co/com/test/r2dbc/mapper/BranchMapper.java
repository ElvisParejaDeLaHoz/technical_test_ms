package co.com.test.r2dbc.mapper;

import co.com.test.model.branch.Branch;
import co.com.test.model.branch.BranchParam;
import co.com.test.r2dbc.model.BranchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BranchMapper {

    BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);

    @Mapping(target = "id", ignore = true)
    BranchEntity toBranchEntity(BranchParam branch);
    BranchEntity toBranchEntity(Branch branch);
    Branch toBranch(BranchEntity branchEntity);
}
