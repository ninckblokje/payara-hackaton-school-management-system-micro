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
import type { Course, Grade, StudentCourse } from '@/model/course'
import { defineComponent } from 'vue'
import { studentService } from '@/services/StudentService';

export default defineComponent({
    data() {
        return {
            courses: undefined as StudentCourse[] | undefined
        }
    },
    mounted() {
        studentService.getEnrolledCourses()
            .then(courses => this.courses = courses)
            .catch(error => console.log(error))
    }
})
</script>

<template>
    <DataTable :value="courses">
        <Column field="name" header="Course"/>
    </DataTable>
</template>

<style scoped></style>
