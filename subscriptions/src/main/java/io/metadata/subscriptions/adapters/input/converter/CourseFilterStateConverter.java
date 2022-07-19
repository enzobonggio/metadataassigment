package io.metadata.subscriptions.adapters.input.converter;

import io.metadata.api.subscriptions.CourseFilterState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CourseFilterStateConverter implements Converter<String, CourseFilterState>
{
    @Override
    public CourseFilterState convert(@NonNull final String source)
    {
        return CourseFilterState.valueOf(source);
    }
}
