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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ninckblokje.payara.hackaton.sms.entity.Teacher;
import ninckblokje.payara.hackaton.sms.repository.TeacherRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.util.function.Function;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_TEACHER;

@Path("/service/teacher")
@RolesAllowed(ROLE_TEACHER)
public class TeacherServiceResource {

    private final TeacherRepository teacherRepository;

    @Inject
    public TeacherServiceResource(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Operation(
            summary = "Retrieve profile for the logged in teacher"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "Teacher profile",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Teacher.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Teacher not found"
    )
    @GET
    @Path("/profile")
    @Produces(APPLICATION_JSON)
    public Response getProfile(@Context SecurityContext secContext) {
        return retrieveTeacherAndDoWork(secContext.getUserPrincipal().getName(), teacher -> teacher);
    }

    Response retrieveTeacherAndDoWork(String name, Function<Teacher, Object> work) {
        var optTeacher = teacherRepository.findTeacherByEmailAddress(name);
        if (optTeacher.isEmpty()) {
            return Response.status(NOT_FOUND)
                    .build();
        } else {
            var outcome = work.apply(optTeacher.get());
            return Response.ok(outcome)
                    .build();
        }

    }
}
