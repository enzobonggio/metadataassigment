package io.metadata.students.adapters.input.messageconsumer.mapper;

import io.metadata.api.Subscriptions;
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MessageConsumerMapper
{
    @Mapping(target = "state", expression = "java(io.metadata.api.commons.State.CREATED)")
    UpdateStudentStateUseCase.Command messageToCommand(Subscriptions.StudentMessage message);

}
