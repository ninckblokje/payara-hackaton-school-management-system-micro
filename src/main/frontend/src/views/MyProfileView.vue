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
import type { Student } from '@/model/student';
import type { Teacher } from '@/model/teacher';
import { studentService } from '@/services/StudentService';
import { teacherService } from '@/services/TeacherService';
import { userStore } from '@/stores/user';
import { defineComponent } from 'vue';

export default defineComponent({
    data() {
        return {
            profile: undefined as unknown as Student | Teacher | undefined,
        }
    },
    computed: {
        birthday() {
            return (this.profile == undefined) ? undefined : this.profile.birthday
        },
        emailAddress() {
            return (this.profile == undefined) ? undefined : this.profile.emailAddress
        },
        name() {
            return (this.profile == undefined) ? undefined : this.profile.name
        }
    },
    mounted() {
        this.profile = undefined
        if (userStore().isInRole('student')) {
            studentService.getProfile()
                .then(student => this.profile = student)
                .catch(error => console.log(error))
        } else if (userStore().isInRole('teacher')) {
            teacherService.getProfile()
                .then(teacher => this.profile = teacher)
                .catch(error => console.log(error))
        }
    }
})
</script>

<template>
    <div class="flex justify-content-center flex-wrap">
        <form>
        <div class="flex col align-items-center justify-content-center">
            <label for="nameInput" class="col">Name</label>
            <InputText id="nameInput" readonly="true" class="col" v-model="name"/>
        </div>
        <div class="flex col align-items-center justify-content-center">
            <label id="emailAddressInput" class="col">E-mail address</label>
            <InputText id="emailAddressInput" readonly="true" class="col" v-model="emailAddress"/>
        </div>
        <div class="flex col align-items-center justify-content-center">
            <label for="birtydayInput" class="col">Birthday</label>
            <InputText id="birthdayInput" readonly="true" class="col" v-model="birthday"/>
        </div>
    </form>
    </div>
</template>

<style scoped></style>