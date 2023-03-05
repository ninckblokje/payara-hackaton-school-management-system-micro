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

package ninckblokje.payara.hackaton.sms.mapping;

import ninckblokje.payara.hackaton.sms.dto.CourseGradeDTO;
import ninckblokje.payara.hackaton.sms.entity.Course;
import ninckblokje.payara.hackaton.sms.entity.Grade;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GradeMapping {

    GradeMapping INSTANCE = Mappers.getMapper(GradeMapping.class);

    @Mapping(source = "course.id", target = "course.id")
    @Mapping(source = "courseGradeDTO.date", target = "date")
    @Mapping(source = "courseGradeDTO.grade", target = "grade")
    @Mapping(source = "courseGradeDTO.student.id", target = "student.id")
    Grade fromCourseGradeDTO(Course course, CourseGradeDTO courseGradeDTO);

    CourseGradeDTO toCourseGradeDTO(Grade grade);
}
