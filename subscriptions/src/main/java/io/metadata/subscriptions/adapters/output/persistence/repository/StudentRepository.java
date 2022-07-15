package io.metadata.subscriptions.adapters.output.persistence.repository;

import io.metadata.subscriptions.adapters.output.persistence.entity.StudentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long>
{
}
