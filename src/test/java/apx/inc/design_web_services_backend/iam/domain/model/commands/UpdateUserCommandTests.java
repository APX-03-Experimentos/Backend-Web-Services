package apx.inc.design_web_services_backend.iam.domain.model.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateUserCommandTests {

    // Test creating command with blank username throws exception
    @Test
    void createCommand_ShouldThrowExceptionIfUsernameIsBlank() {
        assertThatThrownBy(() -> new UpdateUserCommand("", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be empty");
    }

    // Test creating command with blank password throws exception
    @Test
    void createCommand_ShouldThrowExceptionIfPasswordIsBlank() {
        assertThatThrownBy(() -> new UpdateUserCommand("user", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password cannot be empty");
    }

    // Test creating command with valid username and password
    @Test
    void createCommand_ShouldCreateSuccessfullyWithValidValues() {
        UpdateUserCommand command = new UpdateUserCommand("user", "pass");
    }
}
