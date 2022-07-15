package io.metadata.students.adapters.output.persistence.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "COURSES")
public class CourseEntity
{
    @Id
    long id;
    String name;
    String state;
    Timestamp createdAt;
    Timestamp lastModifiedAt;
}
