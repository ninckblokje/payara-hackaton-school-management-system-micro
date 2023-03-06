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

import type { User } from "@/model/user"
import { userStore } from "@/stores/user"

class LoginService {
    login(username: String, password: String) {
        return fetch('https://localhost:8181/api/login', {
            credentials: 'include',
            headers: {
                'Authorization': 'Basic ' + btoa(`${username}:${password}`)
            }
        })
            .then(response => response.json())
            .then(json => json as User)
    }
    logout() {
        return fetch('https://localhost:8181/api/login/logout', {
            credentials: 'include',
            headers: {
                'Authorization': 'Basic ' + userStore().getBasicAuthHeaderValue()
            }
        })
    }
}

export const loginService: LoginService = new LoginService()
