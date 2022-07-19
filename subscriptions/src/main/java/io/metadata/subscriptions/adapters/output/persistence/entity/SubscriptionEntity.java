package io.metadata.subscriptions.adapters.output.persistence.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "subscriptions")
public class SubscriptionEntity
{
    @Id
    @With
    long id;
    long courseId;
    long studentId;
    Timestamp createdAt;
}
