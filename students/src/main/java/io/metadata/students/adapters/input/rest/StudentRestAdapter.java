package io.metadata.students.adapters.input.rest;

import io.metadata.api.students.StudentRequest;
import io.metadata.api.students.StudentResponse;
import io.metadata.students.adapters.input.rest.mapper.RestMapper;
import io.metadata.students.domain.ports.input.CreateStudentUseCase;
import io.metadata.students.domain.ports.input.DeleteStudentUseCase;
import io.metadata.students.domain.ports.input.FetchStudentUseCase;
import io.metadata.students.domain.ports.input.GetStudentUseCase;
import io.metadata.students.domain.ports.input.UpdateStudentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("students")
@RequiredArgsConstructor
public class StudentRestAdapter
{
    private final GetStudentUseCase getStudentUseCase;
    private final CreateStudentUseCase createStudentUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final FetchStudentUseCase fetchStudentUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;
    private final RestMapper mapper;

    @Operation(summary = "Get a student by its id")
    @GetMapping("{studentId}")
    public ResponseEntity<StudentResponse> get(
        @Parameter(description = "id of the student", required = true) @PathVariable("studentId") long studentId)
    {
        val student = getStudentUseCase.getById(studentId);
        return ResponseEntity.ok(student);
    }

    @Operation(summary = "Fetch students")
    @GetMapping
    public ResponseEntity<Collection<StudentResponse>> fetch()
    {
        val students = fetchStudentUseCase.fetch();
        return ResponseEntity.ok(students);
    }

    @Operation(summary = "Create a student")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody StudentRequest request, UriComponentsBuilder builder)
    {
        val command = mapper.requestToCreateCommand(request);
        val id = createStudentUseCase.create(command);
        val location = builder.replacePath("/students/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Modify a student")
    @PutMapping("{studentId}")
    public ResponseEntity<StudentResponse> update(
        @Parameter(description = "id of the student", required = true) @PathVariable("studentId") long studentId, @RequestBody StudentRequest request)
    {
        val command = mapper.requestToUpdateCommand(studentId, request);

        val student = updateStudentUseCase.update(command);
        return ResponseEntity.ok(student);
    }

    @Operation(summary = "Delete a student by its id")
    @DeleteMapping("{studentId}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "id of the student", required = true) @PathVariable("studentId") long studentId)
    {
        val command = mapper.requestToDeleteCommand(studentId);

        deleteStudentUseCase.delete(command);
        return ResponseEntity.noContent().build();
    }
}
