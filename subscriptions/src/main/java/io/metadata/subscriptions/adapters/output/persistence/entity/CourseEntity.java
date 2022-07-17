package io.metadata.subscriptions.adapters.output.persistence.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "courses")
public class CourseEntity implements Persistable<Long>
{
    @Id
    Long id;
    Timestamp createdAt;

    @Override
    public boolean isNew()
    {
        return true;
    }

}
