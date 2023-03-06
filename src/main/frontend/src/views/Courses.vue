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
import type { Course } from '@/model/course'
import { defineComponent } from 'vue'
import { courseService } from '@/services/CourseService';

export default defineComponent({
    data() {
        return {
            courses: undefined as Course[] | undefined
        }
    },
    mounted() {
        courseService.getAllCourses()
            .then(courses => this.courses = courses)
            .catch(error => console.log(error))
    }
})
</script>

<template>
    <DataTable :value="courses">
        <Column field="name" header="Course"/>
        <Column field="id" headers="Actions">
            <template #body="slotProps">
                <p>{{ slotProps.data.id }}</p>
            </template>
        </Column>
    </DataTable>
</template>

<style scoped></style>
