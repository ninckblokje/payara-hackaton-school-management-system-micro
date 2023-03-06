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
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import ninckblokje.payara.hackaton.sms.dto.CourseGradeDTO;
import ninckblokje.payara.hackaton.sms.entity.Course;
import ninckblokje.payara.hackaton.sms.entity.Grade;
import ninckblokje.payara.hackaton.sms.event.StudentGradedNotification;
import ninckblokje.payara.hackaton.sms.event.StudentGradedNotificationEvent;
import ninckblokje.payara.hackaton.sms.mapping.GradeMapping;
import ninckblokje.payara.hackaton.sms.repository.CourseRepository;
import ninckblokje.payara.hackaton.sms.repository.GradeRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_STUDENT;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_TEACHER;

@Path("/courses")
@Transactional
public class CoursesResource {

    @Inject
    @StudentGradedNotification
    Event<StudentGradedNotificationEvent> notificationEvent;

    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;

    @Inject
    public CoursesResource(CourseRepository courseRepository, GradeRepository gradeRepository) {
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
    }

    @Operation(
            summary = "Retrieve all coures"
    )
    @SecurityRequirement(name = "basicAuth")
    @GET
    @Produces(APPLICATION_JSON)
    @RolesAllowed({ROLE_STUDENT, ROLE_TEACHER})
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Operation(
            summary = "Retrieve grades for a course"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "200",
            description = "Grades",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = CourseGradeDTO.class, type = SchemaType.ARRAY)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Course not found"
    )
    @GET
    @Path("/{id}/grades")
    @Produces(APPLICATION_JSON)
    @RolesAllowed(ROLE_TEACHER)
    public Response getGrades(@PathParam("id") long id) {
        var optCourse = courseRepository.find(id);
        if (optCourse.isEmpty()) {
            return Response.status(NOT_FOUND)
                    .build();
        } else {
            var grades = gradeRepository.findAllForCourseStream(optCourse.get())
                    .map(GradeMapping.INSTANCE::toCourseGradeDTO)
                    .collect(Collectors.toList());
            return Response.ok(grades)
                    .build();
        }
    }

    @Operation(
            summary = "Retrieve grades for a course"
    )
    @SecurityRequirement(name = "basicAuth")
    @APIResponse(
            responseCode = "201",
            description = "Grades added"
    )
    @APIResponse(
            responseCode = "404",
            description = "Course not found"
    )
    @POST
    @Path("/{id}/grades")
    @RolesAllowed(ROLE_TEACHER)
    public Response gradeStudents(@PathParam("id") long id, @Valid List<CourseGradeDTO> grades) {
        var optCourse = courseRepository.find(id);
        if (optCourse.isEmpty()) {
            return Response.status(NOT_FOUND)
                    .build();
        } else {
            gradeStudents(optCourse.get(), grades);
            return Response.created(URI.create("/%d/grades".formatted(id)))
                    .build();
        }
    }

    void gradeStudents(Course course, List<CourseGradeDTO> grades) {
        grades.stream()
                .map(courseGradeDTO -> GradeMapping.INSTANCE.fromCourseGradeDTO(course, courseGradeDTO))
                .forEach(this::gradeStudent);
    }

    void gradeStudent(Grade grade) {
        gradeRepository.saveNew(grade);
        notificationEvent.fire(new StudentGradedNotificationEvent(grade.getCourse().getName(), grade.getGrade()));
    }
}
