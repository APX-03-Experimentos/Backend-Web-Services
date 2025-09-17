package apx.inc.design_web_services_backend.shared.interfaces.rest.resources;

public record CourseResource(
        Long courseId,
        String title,
        String imageUrl
        //CourseJoinCodeResource joinCode  // ← Join code como resource
) {
    public CourseResource{
        if (courseId == null|| courseId<=0) {
            throw new IllegalArgumentException(" courseId can not be null, zero or negative ");
        }
        if (title==null||title.isBlank()){
            throw new IllegalArgumentException("title can not be null or empty ");
        }
        if(imageUrl==null||imageUrl.isBlank()){
            throw new IllegalArgumentException(" imageUrl can not be null or empty ");
        }
//        if (joinCode==null){
//            throw new IllegalArgumentException("joinCode can not be null or empty ");
//        }
    }
}
