package io.metadata.students.adapters.output.persistence.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "students")
public class StudentEntity
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
