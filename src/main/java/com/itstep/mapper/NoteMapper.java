package com.itstep.mapper;

import com.itstep.dto.NoteDto;
import com.itstep.entity.Expense;
import com.itstep.entity.Note;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.repository.ExpenseRepository;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static com.itstep.exception.ConstantsUtility.EXPENSE_WITH_SUCH_ID_NOT_FOUND;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface NoteMapper {
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserNameToUserEntity")
    @Mapping(source = "expenseId", target = "expense", qualifiedByName = "mapExpenseIdToExpenseEntity")
    Note toEntity(NoteDto noteDto, @Context ExpenseRepository expenseRepository, @Context UserRepository userRepository);

    @Named("mapExpenseIdToExpenseEntity")
    default Expense toEntity(Integer expenseId, @Context ExpenseRepository expenseRepository) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFound(EXPENSE_WITH_SUCH_ID_NOT_FOUND + expenseId));
    }

    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserEntityToUserName")
    @Mapping(source = "expense", target = "expenseId", qualifiedByName = "mapExpenseEntityToExpenseDto")
    NoteDto toDto(Note note);

    @Named("mapExpenseEntityToExpenseDto")
    default Integer toDto(Expense expense) {
        return expense.getId();
    }
}
