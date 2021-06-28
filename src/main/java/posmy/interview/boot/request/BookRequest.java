package posmy.interview.boot.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookRequest {

    @JsonProperty("action")
    private String action;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private String role;

    private String bookStatus;

    @JsonProperty("status")
    private String status;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }
}
