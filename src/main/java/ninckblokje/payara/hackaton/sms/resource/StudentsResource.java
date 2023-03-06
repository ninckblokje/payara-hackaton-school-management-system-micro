/*
 * Copyright 2023, ninckblokje
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ninckblokje.payara.hackaton.sms.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import ninckblokje.payara.hackaton.sms.entity.Student;
import ninckblokje.payara.hackaton.sms.repository.StudentRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_ADMINISTRATION;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;

@Path("/students")
@Transactional
@RolesAllowed(ROLE_ADMINISTRATION)
public class StudentsResource {

    private final StudentRepository repository;

    @Inject
    public StudentsResource(StudentRepository repository) {
        this.repository = repository;
    }

    @Operation(
            summary = "Retrieve all students"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "All students",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Student.class, type = ARRAY))
    )
    @GET
    @Produces(APPLICATION_JSON)
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Operation(
            summary = "Get a student"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "Student",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Student.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Student not found"
    )
    @Path("/{id}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response get(@PathParam("id") long id) {
        var optStudent = repository.find(id);
        if (optStudent.isPresent()) {
            return Response.ok(optStudent.get())
                    .build();
        } else {
            return Response.status(NOT_FOUND)
                    .build();
        }
    }

    @Operation(
            summary = "Update a student"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "Updated student",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Student.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Student not found"
    )
    @Path("/{id}")
    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response update(@PathParam("id") long id, @Valid Student student) {
        var optStudent = repository.find(id);
        if (optStudent.isEmpty()) {
            return Response.status(NOT_FOUND)
                    .build();
        }

        student.setId(id);
        repository.update(student);

        return Response.ok(student)
                .build();
    }

    @Operation(
            summary = "Create a student"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "201",
            description = "Student created"
    )
    @POST
    @Consumes(APPLICATION_JSON)
    public Response createNew(@Valid Student student) {
        repository.saveNew(student);
        return Response.created(URI.create("/students/%d".formatted(student.getId())))
                .build();
    }
}
