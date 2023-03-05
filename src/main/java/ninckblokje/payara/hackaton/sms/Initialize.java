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

package ninckblokje.payara.hackaton.sms;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import ninckblokje.payara.hackaton.sms.entity.Course;
import ninckblokje.payara.hackaton.sms.entity.Student;
import ninckblokje.payara.hackaton.sms.entity.Teacher;

import java.time.LocalDate;

@Slf4j
@Startup
@Singleton
public class Initialize {

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    void initializeData() {
        log.info("Initializing data");
        initializeTeachers();
        initializeStudent();
        initializeCourses();
    }

    void initializeCourses() {
        var count = (long) em.createNativeQuery("select count(*) from course").getSingleResult();
        if (count > 0) {
            return;
        }
        log.info("Initializing course data");

        var course1 = Course.builder()
                .name("Jakarta EE Course")
                .build();
        em.persist(course1);

        var course2 = Course.builder()
                .name("Quarkus Course")
                .build();
        em.persist(course2);

        var course3 = Course.builder()
                .name("Spring Course")
                .build();
        em.persist(course3);
    }

    void initializeStudent() {
        var count = (long) em.createNativeQuery("select count(*) from student").getSingleResult();
        if (count > 0) {
            return;
        }
        log.info("Initializing student data");

        var student1 = Student.builder()
                .name("student1")
                .emailAddress("student1@localhost")
                .birthday(LocalDate.of(2000, 1, 1))
                .number("1")
                .build();
        em.persist(student1);

        var student2 = Student.builder()
                .name("student2")
                .emailAddress("student2@localhost")
                .birthday(LocalDate.of(2001, 1, 1))
                .number("2")
                .build();
        em.persist(student2);
    }

    void initializeTeachers() {
        var count = (long) em.createNativeQuery("select count(*) from teacher").getSingleResult();
        if (count > 0) {
            return;
        }
        log.info("Initializing teacher data");

        var teacher1 = Teacher.builder()
                .code("TH1")
                .birthday(LocalDate.of(1980, 1, 1))
                .name("teachter1")
                .emailAddress("teacher1@localhost")
                .build();
        em.persist(teacher1);

        var teacher2 = Teacher.builder()
                .code("TH2")
                .birthday(LocalDate.of(1990, 1, 1))
                .name("teachter2")
                .emailAddress("teacher2@localhost")
                .build();
        em.persist(teacher2);
    }
}
