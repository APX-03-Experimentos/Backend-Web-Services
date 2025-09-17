package apx.inc.design_web_services_backend.courses.domain.model.commands;

public record CreateCourseCommand(
        String title,
        String imageUrl
) {
    public CreateCourseCommand{
        if (title == null){
            throw new IllegalArgumentException("Title cannot be null");
        }
        if (imageUrl == null){
            throw new IllegalArgumentException("Image URL cannot be null");
        }
    }
}
