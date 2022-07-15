package io.metadata.api.commons;

public interface ConsumeKafkaMessageUseCase
{

    void create(byte[] message);

}
