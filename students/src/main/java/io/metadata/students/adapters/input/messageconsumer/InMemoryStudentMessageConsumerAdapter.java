package io.metadata.students.adapters.input.messageconsumer;

import io.metadata.students.domain.ports.input.StudentMessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class InMemoryStudentMessageConsumerAdapter implements StudentMessageConsumer
{
    @Override
    public void consumeMessageForSubscriptionStudentCreated(final byte[] event)
    {
    }
}
