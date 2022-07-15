package io.metadata.subscriptions.adapters.output.persistence.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "SUBSCRIPTIONS")
public class SubscriptionEntity
{
    @Id
    long id;
    long courseId;
    long studentId;
    Timestamp createdAt;
}
