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

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_ADMINISTRATION;

@Path("/students")
@Transactional
@RolesAllowed(ROLE_ADMINISTRATION)
public class StudentsResource {

    private final StudentRepository repository;

    @Inject
    public StudentsResource(StudentRepository repository) {
        this.repository = repository;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<Student> getAll() {
        return repository.findAll();
    }

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

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createNew(@Valid Student student) {
        repository.saveNew(student);
        return Response.created(URI.create("/students/%d".formatted(student.getId())))
                .build();
    }
}
