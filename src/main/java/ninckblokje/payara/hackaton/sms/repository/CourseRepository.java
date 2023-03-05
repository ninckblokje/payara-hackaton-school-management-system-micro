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

package ninckblokje.payara.hackaton.sms.repository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ninckblokje.payara.hackaton.sms.entity.Course;
import ninckblokje.payara.hackaton.sms.entity.Grade;
import ninckblokje.payara.hackaton.sms.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ninckblokje.payara.hackaton.sms.Constants.ROLE_STUDENT;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_TEACHTER;

@RequestScoped
public class CourseRepository {

    @PersistenceContext
    EntityManager em;

    @RolesAllowed({ROLE_STUDENT, ROLE_TEACHTER})
    public Optional<Course> find(long id) {
        var course = em.find(Course.class, id);
        return Optional.ofNullable(course);
    }

    @RolesAllowed({ROLE_STUDENT, ROLE_TEACHTER})
    public List<Course> findAll() {
        return em.createQuery("Select c from Course c", Course.class).getResultList();
    }

    @RolesAllowed(ROLE_STUDENT)
    public Stream<Course> findAllForStudentStream(Student student) {
        return em.createQuery("Select c from Course c where c.enrolledStudents = :student", Course.class)
                .setParameter("student", student)
                .getResultStream();
    }

    @RolesAllowed(ROLE_TEACHTER)
    public void saveNew(Course course) {
        em.persist(course);
    }

    @RolesAllowed(ROLE_TEACHTER)
    public Course update(Course course) {
        return em.merge(course);
    }
}
