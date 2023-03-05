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
import ninckblokje.payara.hackaton.sms.entity.Teacher;
import ninckblokje.payara.hackaton.sms.repository.TeacherRepository;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_ADMINISTRATION;

@Path("/teachers")
@RolesAllowed(ROLE_ADMINISTRATION)
public class TeachersResource {

    private final TeacherRepository repository;

    @Inject
    public TeachersResource(TeacherRepository repository) {
        this.repository = repository;
    }

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

    @GET
    @Produces(APPLICATION_JSON)
    public List<Teacher> getAll() {
        return repository.findAll();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createNew(@Valid Teacher teacher) {
        repository.saveNew(teacher);
        return Response.created(URI.create("/teachers/" + teacher.getId()))
                .build();
    }
}
