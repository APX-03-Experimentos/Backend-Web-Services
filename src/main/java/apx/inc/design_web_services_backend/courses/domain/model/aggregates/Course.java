package apx.inc.design_web_services_backend.courses.domain.model.aggregates;

import apx.inc.design_web_services_backend.courses.domain.model.commands.CreateCourseCommand;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

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
        this.imageUrl=createCourseCommand.imageUrl();
        this.teacherId=createCourseCommand.teacherId();
        this.courseJoinCode=null;
    }

    public Course setJoinCode(CourseJoinCode courseJoinCode) {
        this.courseJoinCode = courseJoinCode;
        return this;
    }

    public Course resetJoinCode() {
        this.courseJoinCode = null;
        return this;
    }


}
