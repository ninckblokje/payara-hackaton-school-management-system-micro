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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ninckblokje.payara.hackaton.sms.entity.Student;
import ninckblokje.payara.hackaton.sms.mapping.CourseMapping;
import ninckblokje.payara.hackaton.sms.repository.CourseRepository;
import ninckblokje.payara.hackaton.sms.repository.GradeRepository;
import ninckblokje.payara.hackaton.sms.repository.StudentRepository;

import java.util.function.Function;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_STUDENT;

@Path("/service/student")
@Transactional
@RolesAllowed(ROLE_STUDENT)
public class StudentServiceResource {

    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;

    @Inject
    public StudentServiceResource(CourseRepository courseRepository, GradeRepository gradeRepository, StudentRepository repository) {
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
        this.studentRepository = repository;
    }

    @GET
    @Path("/enrolledcourses")
    @Produces(APPLICATION_JSON)
    public Response getEnrolledCourses(@Context SecurityContext secContext) {
        return retrieveStudentAndDoWork(secContext.getUserPrincipal().getName(), student -> courseRepository.findAllForStudentStream(student)
                .map(CourseMapping.INSTANCE::toStudentCourseDto)
                .collect(Collectors.toList())
        );
    }

    @GET
    @Path("/grades")
    @Produces(APPLICATION_JSON)
    public Response getGrades(@Context SecurityContext secContext) {
        return retrieveStudentAndDoWork(secContext.getUserPrincipal().getName(), student -> gradeRepository.findAllForStudent(student));
    }

    @GET
    @Path("/profile")
    @Produces(APPLICATION_JSON)
    public Response getProfile(@Context SecurityContext secContext) {
        return retrieveStudentAndDoWork(secContext.getUserPrincipal().getName(), student -> student);
    }

    Response retrieveStudentAndDoWork(String name, Function<Student,Object> work) {
        var optStudent = studentRepository.findStudentByEmailAddress(name);
        if (optStudent.isEmpty()) {
            return Response.status(NOT_FOUND)
                    .build();
        } else {
            var outcome = work.apply(optStudent.get());
            return Response.ok(outcome)
                    .build();
        }
    }
}
