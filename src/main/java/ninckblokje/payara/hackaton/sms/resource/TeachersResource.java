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
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import ninckblokje.payara.hackaton.sms.entity.Student;
import ninckblokje.payara.hackaton.sms.entity.Teacher;
import ninckblokje.payara.hackaton.sms.repository.TeacherRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
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

@Path("/teachers")
@RolesAllowed(ROLE_ADMINISTRATION)
public class TeachersResource {

    private final TeacherRepository repository;

    @Inject
    public TeachersResource(TeacherRepository repository) {
        this.repository = repository;
    }

    @Operation(
            summary = "Get a teacher"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "Teacher",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Teacher.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Teacher not found"
    )
    @Path("/{id}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response get(@PathParam("id") long id) {
        var optTeacher = repository.find(id);
        if (optTeacher.isPresent()) {
            return Response.ok(optTeacher.get())
                    .build();
        } else {
            return Response.status(NOT_FOUND)
                    .build();
        }
    }

    @Operation(
            summary = "Update a teacher"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "Updated teacher",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Teacher.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Teacher not found"
    )
    @Path("/{id}")
    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response update(@PathParam("id") long id, @Valid Teacher teacher) {
        var optTeacher = repository.find(id);
        if (optTeacher.isEmpty()) {
            return Response.status(NOT_FOUND)
                    .build();
        }

        teacher.setId(id);
        repository.update(teacher);

        return Response.ok(teacher)
                .build();
    }

    @Operation(
            summary = "Get all teachers"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "All teachers",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Teacher.class, type = ARRAY))
    )
    @GET
    @Produces(APPLICATION_JSON)
    public List<Teacher> getAll() {
        return repository.findAll();
    }

    @Operation(
            summary = "Create a teacher"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "201",
            description = "Teacher created"
    )
    @POST
    @Consumes(APPLICATION_JSON)
    public Response createNew(@Valid Teacher teacher) {
        repository.saveNew(teacher);
        return Response.created(URI.create("/teachers/" + teacher.getId()))
                .build();
    }
}
