package io.metadata.courses.adapters.input.messageconsumer;

import io.metadata.courses.domain.ports.input.CourseMessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class InMemoryCourseMessageConsumerAdapter implements CourseMessageConsumer
{
    @Override
    public void consumeMessageForSubscriptionCourseCreated(final byte[] event)
    {
    }
}
