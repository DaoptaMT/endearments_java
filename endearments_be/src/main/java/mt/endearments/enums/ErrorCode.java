package mt.endearments.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    GET_SUCCESSFUL(1010, "Get successful", HttpStatus.OK),
    ADD_SUCCESSFUL(1011, "Add successful", HttpStatus.OK),
    DELETE_SUCCESSFUL(1012, "Delete successful", HttpStatus.OK),
    UPDATE_SUCCESSFUL(1013, "Update successful", HttpStatus.OK),
    INVALID_DATA(1014, "Invalid data", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(1020, "Review not found", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1021, "Invalid or expired token", HttpStatus.BAD_REQUEST),
    VALUE_MUST_BE_NUMERIC(1022, "Value must be numeric", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1023, "Incorrect username or password", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1024, "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    FILES_NOT_EMPTY(1025, "File list must not be null or empty", HttpStatus.BAD_REQUEST),

    // Auth and User 1***
    EMAIL_INVALID(1100, "Email invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1101, "Password must be not blank", HttpStatus.BAD_REQUEST),
    FULL_NAME_INVALID(1102, "Full name must be not null", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1103, "User not found", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1104, "Email already exists", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(1105, "Username already exists", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_INVALID(1106, "New password invalid", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1107, "Username invalid", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_BLANK(1108, "Token must not be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_FORMAT(1109, "Password must contain at least 8 characters, including uppercase, lowercase, number, and special character", HttpStatus.BAD_REQUEST),

    // Role 2***
    ROLE_INVALID(2000, "Role must be not blank", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(2001, "Role not found", HttpStatus.BAD_REQUEST),
    ROLE_INVALID_TYPE(2002, "Role invalid type", HttpStatus.BAD_REQUEST);

    // Image Medicine 7***
//    IMAGE_NOT_FOUND(7000, "Image not found", HttpStatus.BAD_REQUEST),

    // Batch Import 8***
//    CSV_INVALID(8000, "Please upload a CSV file", HttpStatus.BAD_REQUEST),
//    CSV_ALREADY_PROCESSED(8001, "This file has already been processed", HttpStatus.BAD_REQUEST),
//    CSV_DOWNLOAD_INVALID(8002, "CSV file is invalid or corrupted", HttpStatus.BAD_REQUEST),

    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
