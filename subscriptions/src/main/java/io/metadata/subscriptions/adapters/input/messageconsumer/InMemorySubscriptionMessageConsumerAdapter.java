package io.metadata.subscriptions.adapters.input.messageconsumer;

import io.metadata.subscriptions.domain.ports.input.SubscriptionMessageConsumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemorySubscriptionMessageConsumerAdapter implements SubscriptionMessageConsumer
{

    @Override
    public void consumeMessageForStudentCreated(final byte[] event)
    {

    }

    @Override
    public void consumeMessageForStudentDeleted(final byte[] event)
    {

    }

    @Override
    public void consumeMessageForCourseCreated(final byte[] event)
    {

    }

    @Override
    public void consumeMessageForCourseDeleted(final byte[] event)
    {

    }
}
