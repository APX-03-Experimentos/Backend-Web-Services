package apx.inc.design_web_services_backend.iam.domain.model.aggregates;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories.CourseRepository;
import apx.inc.design_web_services_backend.iam.domain.model.commands.UpdateUserCommand;
import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
public class User extends AuditableAbstractAggregateRoot <User>{

    private String userName;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> userRoles;

    @ManyToMany
    @JoinTable(
            name = "user_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Course> userInCourses;

    protected User(){
        super();
        this.userRoles = new HashSet<>();
        this.userInCourses= new HashSet<>();
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.userRoles = new HashSet<>();
        this.userInCourses= new HashSet<>();
    }

    public User(String userName, String password, List<Role> roles) {
        this(userName, password);
        addRoles(roles);
    }

    //update
    public User updateUserDetails(UpdateUserCommand updateUserCommand){
        this.userName = updateUserCommand.userName();
        this.password = updateUserCommand.password();

        return this;
    }

    //
    public void assignToGroup(Long courseId, CourseRepository courseRepository) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
        this.userInCourses.add(course);
    }

    public void removeFromGroup(Long courseId) {
        this.userInCourses.removeIf(course -> course.getId().equals(courseId));
    }

    public void addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.userRoles.addAll(validatedRoleSet);
    }

}
