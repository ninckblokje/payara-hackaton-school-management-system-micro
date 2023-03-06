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
import ninckblokje.payara.hackaton.sms.entity.Teacher;

import java.util.List;
import java.util.Optional;

import static ninckblokje.payara.hackaton.sms.Constants.*;

@RequestScoped
@RolesAllowed(ROLE_ADMINISTRATION)
public class TeacherRepository {

    @PersistenceContext
    EntityManager em;

    public Optional<Teacher> find(long id) {
        var teacher = em.find(Teacher.class, id);
        return Optional.ofNullable(teacher);
    }

    public List<Teacher> findAll() {
        return em.createQuery("Select t from Teacher t", Teacher.class).getResultList();
    }

    @RolesAllowed({ROLE_ADMINISTRATION, ROLE_TEACHER})
    public Optional<Teacher> findTeacherByEmailAddress(String emailAddress) {
        var teachers = em.createQuery("Select t from Teacher t where t.emailAddress = :emailAddress", Teacher.class)
                .setParameter("emailAddress", emailAddress)
                .getResultList();
        if (teachers.size() == 1) {
            return Optional.of(teachers.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void saveNew(Teacher teacher) {
        em.persist(teacher);
    }

    public Teacher update(Teacher teacher) {
        return em.merge(teacher);
    }
}
