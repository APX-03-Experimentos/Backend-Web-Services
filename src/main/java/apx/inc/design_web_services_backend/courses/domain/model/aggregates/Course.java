package apx.inc.design_web_services_backend.courses.domain.model.aggregates;

import apx.inc.design_web_services_backend.courses.domain.model.commands.CreateCourseCommand;
import apx.inc.design_web_services_backend.courses.domain.model.commands.UpdateCourseCommand;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.Date;
import java.util.Random;

@Getter
@Entity
public class Course extends AuditableAbstractAggregateRoot<Course> {

    private String title;
    private String imageUrl;

    @Embedded
    private CourseJoinCode courseJoinCode;

    private Long teacherId;

    //constructor JPA
    protected Course() {
        //normalmente se deja vacio
    }

    //constructor test
    public Course(String title, String imageUrl,Long teacherId) {
        super();
        this.title = title;
        this.imageUrl = imageUrl;
        this.teacherId=teacherId;
        this.courseJoinCode = null;
    }

    //constructor por comando
    public Course(CreateCourseCommand createCourseCommand){
        this.title=createCourseCommand.title();
        this.imageUrl = generatePicsumImageUrl(createCourseCommand.title());
        this.teacherId=createCourseCommand.teacherId();
        this.courseJoinCode = generateCourseJoinCode();
    }

    public Course setJoinCode(CourseJoinCode courseJoinCode) {
        this.courseJoinCode = courseJoinCode;
        return this;
    }

    public Course resetJoinCode() {
        this.courseJoinCode = null;
        return this;
    }

    public Course updateCourse(UpdateCourseCommand  updateCourseCommand){
        this.title=updateCourseCommand.title();
        this.imageUrl=updateCourseCommand.imageUrl();
        return this;
    }


    private String generatePicsumImageUrl(String courseTitle) {
        // Generar un seed único basado en el título del curso
        int seed = courseTitle != null ? Math.abs(courseTitle.hashCode()) : new Random().nextInt(1000);

        // URL de Picsum con dimensiones y seed único
        return "https://picsum.photos/400/300?random=" + seed;
    }

    private CourseJoinCode generateCourseJoinCode() {
        // Key aleatoria (8 caracteres)
        String key = java.util.UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();

        // Expiración: ahora + 6 meses
        Date expiration = java.sql.Timestamp.valueOf(
                java.time.LocalDateTime.now().plusMonths(6)
        );

        return new CourseJoinCode(key, expiration);
    }


}
