package io.metadata.subscriptions.adapters.output.persistence.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "students")
public class StudentEntity implements Persistable<Long>
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
