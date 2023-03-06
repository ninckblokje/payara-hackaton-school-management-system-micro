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

import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LogoutView from '../views/LogoutView.vue'
import MyCources from '../views/MyCourses.vue'
import MyGrades from '../views/MyGrades.vue'
import MyProfileView from '../views/MyProfileView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/mycourses',
      name: 'mycourses',
      component: MyCources
    },
    {
      path: '/mygrades',
      name: 'mygrades',
      component: MyGrades
    },
    {
      path: '/myprofile',
      name: 'myprofile',
      component: MyProfileView
    },
    {
      path: '/logout',
      name: 'logout',
      component: LogoutView
    }
  ]
})

export default router
