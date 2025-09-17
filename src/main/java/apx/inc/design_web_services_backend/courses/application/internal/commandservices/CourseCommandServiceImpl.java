package apx.inc.design_web_services_backend.courses.application.internal.commandservices;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.commands.*;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.courses.domain.services.CourseCommandService;
import apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories.CourseRepository;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Date;
import java.util.Optional;

public class CourseCommandServiceImpl implements CourseCommandService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseCommandServiceImpl(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Long handle(CreateCourseCommand createCourseCommand) {
        //1. Asegurarnos que el usuario que creo el curso exista
        var teacherOptional = userRepository.findById(createCourseCommand.teacherId());
        if (teacherOptional.isEmpty()) {
            throw new IllegalArgumentException("Teacher with ID " + createCourseCommand.teacherId() + " not found");
        }

        //2. Verificamos que tenga el rol de teacher
        var teacher = teacherOptional.get();

        boolean isteacher = teacher.getUserRoles().stream()
                .anyMatch(role -> role.getName() == Roles.ROLE_TEACHER);

        if (!isteacher) {
            throw new IllegalArgumentException("Only teachers can create courses");
        }

        // ✅ Crear y guardar el curso
        var group=new Course(createCourseCommand);
        courseRepository.save(group);

        return group.getId();
    }

    @Override
    public void handle(DeleteCourseCommand deleteCourseCommand) {
        if (!courseRepository.existsById(deleteCourseCommand.courseId())) {
            throw new IllegalArgumentException("Course with ID " + deleteCourseCommand.courseId() + " not found");
        }
        try{
            //1. Buscar todos los usuarios
            var allUsers = userRepository.findAll();

            //2. Remover del curso a cada usuario
            allUsers.forEach(user -> {
                user.removeFromCourse(deleteCourseCommand.courseId());
                userRepository.save(user);
            });
            //3. Con el curso limpio eliminar el curso
            courseRepository.deleteById(deleteCourseCommand.courseId());

        } catch (Exception e){
            throw new RuntimeException("Error while deleting course",e);
        }
    }

    @Override
    public Optional<Course> handle(JoinByJoinCodeCommand joinByJoinCodeCommand) {
        //1. Buscar el grupo con ese joinCode
        var courseOptional = courseRepository.findAll().stream()
                .filter(course -> course.getCourseJoinCode().key().equals(joinByJoinCodeCommand.joinCode()))
                .findFirst();

        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + joinByJoinCodeCommand.joinCode() + " not found");
        }

        //2. Validar expiration
        var course = courseOptional.get();

        if (course.getCourseJoinCode().expiration().before(new Date())){
            throw new IllegalArgumentException("Course with ID " + joinByJoinCodeCommand.joinCode() + " has expired");
        }

        //3. Buscar al usuario para agregarlo al grupo

        var userOptional=userRepository.findById(joinByJoinCodeCommand.studentId());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + joinByJoinCodeCommand.studentId() + " not found");
        }

        var user=userOptional.get();

        //4. Verificar si ya esta unido

        boolean alreadyInCourse=user.getStudentInCourses().stream()
                .anyMatch(c->c.getId().equals(course.getId()));

        if (!alreadyInCourse) {
            user.assignToCourse(course);
            userRepository.save(user);
        }

        return Optional.of(course);
    }

    @Override
    public void handle(KickStudentCommand kickStudentCommand, Long teacherId) {
        //2. Verificar que el course existe

        var optionalCourse=courseRepository.findById(kickStudentCommand.courseId());

        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + kickStudentCommand.courseId() + " not found");
        }

        var course=optionalCourse.get();

        //3. Verificar que el student existe

        var optionalUser=userRepository.findById(kickStudentCommand.studentId());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + kickStudentCommand.courseId() + " not found");
        }

        var student=optionalUser.get();

        //4. Verificar que el profesor es el owner del course
        //- existencia del teacher
        var optionalTeacher=userRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " not found");
        }

        var teacher=optionalTeacher.get();
        // es owner ?
        boolean isOwner= teacher.getStudentInCourses().stream().anyMatch(c->c.getId().equals(course.getId()));

        if (!isOwner) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " is not the owner of this course");
        }

        //5. Verificar que el student esta en el course

        boolean isEnrolled = student.getStudentInCourses().stream().anyMatch(c->c.getId().equals(course.getId()));

        if (!isEnrolled) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " is not enrolled in this course");
        }

        //6. Verificado el student y teacher que son del cousr
        student.removeFromCourse(kickStudentCommand.courseId());

        //7. Guardar los cambios en el repo
        userRepository.save(student);

    }

    @Override
    public Optional<Course> handle(ResetJoinCodeCommand resetJoinCodeCommand) {
        //1. Verificar que el course existe

        var optionalCourse=courseRepository.findById(resetJoinCodeCommand.courseId());

        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + resetJoinCodeCommand.courseId() + " not found");
        }

        var course=optionalCourse.get();

        //2. Resetear el joinCode

        course.resetJoinCode();

        //3. Guardar los cambios en elr epo

        courseRepository.save(course);

        return  Optional.of(course);
    }

    @Override
    public Optional<CourseJoinCode> handle(SetJoinCodeCommand setJoinCodeCommand) {
        //1. Verificar si existe el grupo
        var optionalCourse=courseRepository.findById(setJoinCodeCommand.courseId());

        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + setJoinCodeCommand.courseId() + " not found");
        }

        var course=optionalCourse.get();

        //2. Verificar si el nuevo key esta asignado a otro course




    }

    @Override
    public Optional<Course> handle(UpdateCourseCommand updateCourseCommand) {
        return Optional.empty();
    }
}
