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

package ninckblokje.payara.hackaton.sms.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.SecurityContext;
import ninckblokje.payara.hackaton.sms.dto.UserDTO;

import java.util.List;

import static ninckblokje.payara.hackaton.sms.Constants.*;

@ApplicationScoped
@RolesAllowed({ROLE_ADMINISTRATION, ROLE_STUDENT, ROLE_TEACHTER})
public class UserService {

    private final List<String> applicationRoles = List.of(ROLE_ADMINISTRATION, ROLE_STUDENT, ROLE_TEACHTER);

    public UserDTO getUserInformation(SecurityContext securityContext) {
        var user = new UserDTO();

        user.setName(securityContext.getUserPrincipal().getName());

        applicationRoles.stream()
                .filter(securityContext::isUserInRole)
                .forEach(user.getRoles()::add);

        return user;
    }
}
