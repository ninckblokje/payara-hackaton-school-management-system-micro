<!--
  ~ Copyright 2023, ninckblokje
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->

<script lang="ts">
import { loginService } from '@/services/LoginService';
import { userStore } from '@/stores/user';
import { defineComponent } from 'vue';

export default defineComponent({
    data() {
        return {
            username: '',
            password: ''
        }
    },
    methods: {
        login(e: Event) {
            e.preventDefault()

            userStore().login(this.username, this.password)
        }
    }
})

</script>

<template>
    <div class="flex justify-content-center flex-wrap">
        <form v-on:submit="login">
            <div class="flex align-items-center justify-content-center">
                <p class="text-color-secondary font-bold">Student or teacher, please login</p>
            </div>
            <div class="flex col align-items-center justify-content-center">
                <InputText type="email" placeholder="E-mail address" v-model="username" />
            </div>
            <div class="flex col align-items-center justify-content-center">
                <InputText type="password" placeholder="Password" v-model="password" />
            </div>
            <div class="flex col align-items-center justify-content-center">
                <Button type="submit" label="Login" />
            </div>
        </form>
    </div>
</template>

<style scoped></style>