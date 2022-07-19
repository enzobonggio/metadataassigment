package io.metadata.subscriptions.adapters.input.converter;

import io.metadata.api.subscriptions.StudentFilterState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StudentFilterStateConverter implements Converter<String, StudentFilterState>
{
    @Override
    public StudentFilterState convert(@NonNull final String source)
    {
        return StudentFilterState.valueOf(source);
    }
}
