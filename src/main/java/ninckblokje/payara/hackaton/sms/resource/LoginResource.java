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

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ninckblokje.payara.hackaton.sms.dto.UserDTO;
import ninckblokje.payara.hackaton.sms.service.UserService;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static ninckblokje.payara.hackaton.sms.Constants.*;

@Path("/login")
@RolesAllowed({ROLE_ADMINISTRATION, ROLE_STUDENT, ROLE_TEACHTER})
public class LoginResource {

    private final UserService service;

    @Inject
    public LoginResource(UserService service) {
        this.service = service;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public UserDTO login(@Context SecurityContext secContext) {
        return service.getUserInformation(secContext);
    }

    @GET
    @Path("/logout")
    public void logout(@Context HttpServletRequest request) throws ServletException {
        request.getSession().invalidate();
        request.logout();
    }

    @OPTIONS
    @PermitAll
    public void options() {}
}
