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
import { defineComponent } from 'vue';
import { RouterView } from 'vue-router'
import { userStore } from './stores/user';
import LoginView from './views/LoginView.vue';

export default defineComponent({
    data() {
        return {
            administrationMenuItems: [
              {label: 'Home', icon: 'pi pi-fw pi-home', to: '/'},
              {label: 'Logout', icon: 'pi pi-fw pi-sign-out', to: '/logout'}
            ],
            studentMenuItems: [
              {label: 'Home', icon: 'pi pi-fw pi-home', to: '/home'},
              {label: 'My courses', icon: 'pi pi-fw pi-calendar', to: '/mycourses'},
              {label: 'My grades', icon: 'pi pi-fw pi-pencil', to: '/mygrades'},
              {label: 'Profile', icon: 'pi pi-fw pi-user', to: '/myprofile'},
              {label: 'Logout', icon: 'pi pi-fw pi-sign-out', to: '/logout'}
            ],
            teacherMenuItems: [
              {label: 'Home', icon: 'pi pi-fw pi-home', to: '/home'},
              {label: 'Logout', icon: 'pi pi-fw pi-sign-out', to: '/logout'}
            ]
        }
    },
    components: {
      LoginView
    },
    computed: {
      isLoggedOff() { return userStore().isLoggedOff },
      menuItems() {
        if (userStore().isInRole('administration')) {
          return this.administrationMenuItems
        } else if (userStore().isInRole('student')) {
          return this.studentMenuItems
        } else if (userStore().isInRole('teacher')) {
          return this.teacherMenuItems
        } else {
          return []
        }
      }
    }
})
</script>

<template>
  <header>
  </header>
  <body>
  <div v-if="isLoggedOff">
    <LoginView/>
  </div>
  <div v-else>
    <TabMenu :model="menuItems" />
    <RouterView />
  </div>
  </body>
</template>

<style scoped>
</style>
