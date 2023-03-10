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
import java.util.stream.Stream;

import static ninckblokje.payara.hackaton.sms.Constants.ROLE_STUDENT;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_TEACHER;

@RequestScoped
public class GradeRepository {

    @PersistenceContext
    EntityManager em;

    @RolesAllowed(ROLE_TEACHER)
    public Stream<Grade> findAllForCourseStream(Course course) {
        return em.createQuery("Select g from Grade g where g.course = :course", Grade.class)
                .setParameter("course", course)
                .getResultStream();
    }

    @RolesAllowed(ROLE_TEACHER)
    public List<Grade> findAllForCourse(Course course) {
        return em.createQuery("Select g from Grade g where g.course = :course", Grade.class)
                .setParameter("course", course)
                .getResultList();
    }

    @RolesAllowed(ROLE_STUDENT)
    public Stream<Grade> findAllForStudentStream(Student student) {
        return em.createQuery("Select g from Grade g where g.student = :student", Grade.class)
                .setParameter("student", student)
                .getResultStream();
    }

    @RolesAllowed(ROLE_STUDENT)
    public List<Grade> findAllForStudent(Student student) {
        return em.createQuery("Select g from Grade g where g.student = :student", Grade.class)
                .setParameter("student", student)
                .getResultList();
    }

    @RolesAllowed(ROLE_TEACHER)
    public void saveNew(Grade grade) {
        em.persist(grade);
    }
}
