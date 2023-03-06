# payara-hackaton-school-management-system

This is my solution for the Payara hackaton. Unfortunately I am far from finished :disappointed: :disappointed: :disappointed:

I decided to build a school management system. Payara set the following functional requirements:
- Student Information
  - Personal Information
  - Assignments/Grades
  - Achievements
  - Extracurricular Activities
  - Attendance
- Teacher Information
  - Personal Information
- Class/Courses
- Grade History
- Fee Management
- Admission Management
- School Calendar/Activity Management
- Library Management
- Communication
  - Internal Communication
  - External Communication (SMS, Email)

## Architecture

The backend is based upon Jakarte EE 10 running on Payara Micro 6.2. The frontend is based upon [Vue.js](https://vuejs.org/) with [PrimeVue](https://primevue.org/) written in Typescript. Communication is done using REST API's.

Authentication by the frontend was designed to use cookies, however to use cookies cross site (if the UI is developed locally) the same site attribute should be used. However, Payara does not yet offer this ability (see https://github.com/payara/Payara/pull/6200). For now the frontend uses HTTP basic auth for every request...

Security is implemented on the resource and repository layers. For testing purposes the following users will be automatically created by Payara Micro:
- Administrators:
  - manager
- Students:
  - student1@localhost
  - student2@localhost
- Teachers:
  - teacher1@localhost
  - teacher2@localhost

The super secure password is `Dummy_123`.

## Package structure

The following package structure is used:
- `ninckblokje.payara.hackaton.sms.dto`: Containing specified DTO for usage in REST resources
- `ninckblokje.payara.hackaton.sms.entity`: Containing JPA entities
- `ninckblokje.payara.hackaton.sms.event`: Containing CDI events
- `ninckblokje.payara.hackaton.sms.mapping`: Containing mapping interfaces (using [MapStruct](https://mapstruct.org/)) between DTO's and entities
- `ninckblokje.payara.hackaton.sms.repository`: Containing data access repositories
- `ninckblokje.payara.hackaton.sms.resource`: Container the REST resources
- `ninckblokje.payara.hackaton.sms.service`: Containing business services

## Status

Most is not ready and the UI is lacking:
- REST
  - Student & teacher information (including creation)
  - Courses
  - Grade a student for a course
  - Enroll student in a course
- UI
  - Show profile teacher & student
  - For student show enrolled courses
  - For student show grades
  - For teacher show all courses

Other points:
- The CORS configuration is not yet 100% correct for Firefox.
- The CDI event `StudentGradedNotificationEvent` was to be used to notify students for example using web sockets or other communiation channels. But the implementation for the notification part has not yet been written.
- Entities should not be returned by resources, but separate DTO's

## Building

To build the project, run the following command: `mvn clean verify`

Or to build the project with Docker: `mvn clean verify jib:build`

The frontend is automatically build and added to the WAR during the build process.

## Running

To run the project locally from the checkout: `mvn package payara-micro:start`

To run the project from a Docker container: `docker run -p 8080:8080 -p 8181:8181 ninckblokje/school-management-system-micro`

The UI is either availabe on http://localhost:8080 (use Chrome) or a development version can be started in the directory `src/main/frontend` with the command: `npm run dev`

The OpenAPI spec is available on https://localhost:8181/openapi
