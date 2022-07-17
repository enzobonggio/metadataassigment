package io.metadata.courses.adapters.output.persistence.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "courses")
public class CourseEntity
{
    @Id
    @With
    long id;
    @With
    String name;
    @With
    String state;
    Timestamp createdAt;
    Timestamp lastModifiedAt;
}
