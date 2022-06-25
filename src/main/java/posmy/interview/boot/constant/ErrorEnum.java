package posmy.interview.boot.constant;

/**
 * @author Hafiz
 * @version 0.01
 */
public enum ErrorEnum {
    SAVING_UNSUCCESSFUL(
            "Saving unsuccessful",
            "Data received failed to be inserted into database"
    ),
    ELEMENT_FORMAT_INVALID(
            "Data element not in the required format",
            "Name of invalid element(s); For example, length or non-numeric format"
    ),
    REQUIRED_ELEMENT_MISSING(
            "Data element(s) issues",
            "Data element(s) missing"
    ),
    VALIDATION_FAILED(
            "Validation failed",
            "Data element(s) does not meet the criteria"
    ),
    RECORD_NOT_FOUND(
            "No record found",
            "No existing record in database"
    ),
    DATETIME_PARSING_FAILED(
            "Datetime parsing failed",
            "Date format invalid"
    ),
    JSON_CONVERSION_FAILED(
            "JSON conversion failed",
            "Conversion process to JSON failed"
    ),
    UNAUTHORIZED_ACCESS(
            "Unauthorized access",
            "Illegal unauthorized Access"
    ),
    BAD_REQUEST(
            "Bad Request",
            "Request Not Found"
    ),
    ACCESS_DENIED_INVALID_ENDPOINT(
            "Access denied or invalid endpoint",
            "Access failure"
    ),
    RESPONSE_RECEIVED_WITH_ERROR(
            "Response received with error",
            "Request does not meet the criteria"
    ),
    DELETION_UNSUCCESSFUL(
            "Deletion unsuccessful",
            "Data received failed to be deleted from database"
    );

    private ErrorEnum(String description, String detail){
        this.description = description;
        this.detail = detail;
    }

    private String description;
    private String detail;

    public String getDescription() {
        return description;
    }

    public String getDetail() {
        return detail;
    }
}
