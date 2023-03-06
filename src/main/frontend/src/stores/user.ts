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

import type { User } from "@/model/user";
import { defineStore } from "pinia";
import { loginService } from "@/services/LoginService";

export const userStore = defineStore('user', {
    state: () => {
        return {
            user: undefined as User | undefined,

            // TODO Wait for https://github.com/payara/Payara/pull/6200 to use cookies in Payara
            username: undefined as unknown as string | undefined,
            password: undefined as unknown as string | undefined
        }
    },
    actions: {
        login(username: string, password: string) {
            this.user = undefined

            loginService.login(username, password)
                .then(user => {
                    this.user = user
                    this.username = username
                    this.password = password
                })
                .catch(error => console.log(error))
        },
        logout() {
            loginService.logout()
                .then(_ => {
                    this.user = undefined
                    this.username = undefined
                    this.password = undefined
                })
                .catch(error => console.log)
        }
    },
    getters: {
        getBasicAuthHeaderValue: (state) => {
            return () => btoa(`${state.username}:${state.password}`)
        },
        isLoggedOff: (state) => state.user == undefined,
        isInRole: (state) => {
            return (role: string):boolean => state.user != undefined && state.user.roles.includes(role)
        }
    }
})
