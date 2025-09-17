package apx.inc.design_web_services_backend.courses.interfaces.rest.resources;

public record CreateCourseResource(
        String title,
        String imageUrl
) {
    public CreateCourseResource{
        if(title==null||title.isBlank()){
            throw new IllegalArgumentException(" title can not be null or empty ");
        }
        if(imageUrl==null||imageUrl.isBlank()){
            throw new IllegalArgumentException(" imageUrl can not be null or empty ");
        }

    }
}
