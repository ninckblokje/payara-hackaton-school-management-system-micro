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
import ninckblokje.payara.hackaton.sms.entity.Student;

import java.util.List;
import java.util.Optional;

import static ninckblokje.payara.hackaton.sms.Constants.ROLE_ADMINISTRATION;
import static ninckblokje.payara.hackaton.sms.Constants.ROLE_STUDENT;

@RequestScoped
@RolesAllowed(ROLE_ADMINISTRATION)
public class StudentRepository {

    @PersistenceContext
    EntityManager em;

    public Optional<Student> find(long id) {
        var student = em.find(Student.class, id);
        return Optional.ofNullable(student);
    }

    public List<Student> findAll() {
        return em.createQuery("Select s from Student s", Student.class).getResultList();
    }

    @RolesAllowed({ROLE_ADMINISTRATION, ROLE_STUDENT})
    public Optional<Student> findStudentByEmailAddress(String emailAddress) {
        var students = em.createQuery("Select s from Student s where s.emailAddress = :emailAddress", Student.class)
                .setParameter("emailAddress", emailAddress)
                .getResultList();
        if (students.size() == 1) {
            return Optional.of(students.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void saveNew(Student student) {
        em.persist(student);
    }

    public Student update(Student student) {
        return em.merge(student);
    }
}
