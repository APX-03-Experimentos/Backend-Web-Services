package apx.inc.design_web_services_backend.notifications.interfaces.rest;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetAllNotificationsQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationByIdQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationsByUserIdQuery;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationCommandService;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationQueryService;
import apx.inc.design_web_services_backend.notifications.interfaces.rest.resources.CreateNotificationResource;
import apx.inc.design_web_services_backend.notifications.interfaces.rest.resources.NotificationResource;
import apx.inc.design_web_services_backend.notifications.interfaces.rest.transform.CreateNotificationCommandFromResourceAssembler;
import apx.inc.design_web_services_backend.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Endpoints for managing notifications")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;

    @Operation(summary = "Get all notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<NotificationResource>> getAllNotifications() {
        try {
            var query = new GetAllNotificationsQuery();
            var notifications = notificationQueryService.handle(query);
            var resources = notifications.stream()
                    .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get notification by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification found"),
            @ApiResponse(responseCode = "404", description = "Notification not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResource> getNotificationById(@PathVariable Long id) {
        try {
            var query = new GetNotificationByIdQuery(id);
            Optional<Notification> notification = notificationQueryService.handle(query);

            return notification.map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get notifications by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResource>> getNotificationsByUserId(@PathVariable Long userId) {
        try {
            var query = new GetNotificationsByUserIdQuery(userId);
            var notifications = notificationQueryService.handle(query);
            var resources = notifications.stream()
                    .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}