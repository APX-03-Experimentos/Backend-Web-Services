package apx.inc.design_web_services_backend.notifications.application.internal.outboundservices.acl;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.interfaces.acl.IamContextFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

   public Optional<User> fetchUserById(Long userId){
       try{
           return iamContextFacade.fetchUserById(userId);
       } catch (Exception e) {
           throw new IllegalArgumentException( "Failed to fetch user with ID: " + userId, e);
       }
   }

   public List<Long> getStudentsByCourseId(Long courseId){
       try{
           return iamContextFacade.getStudentsByCourseId(courseId);
       } catch (Exception e) {
           throw new IllegalArgumentException( "Failed to fetch students with ID: " + courseId, e);
       }
   }
}
