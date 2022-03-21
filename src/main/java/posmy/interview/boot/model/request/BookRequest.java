package posmy.interview.boot.model.request;

public class BookRequest {

    private String id;
    private String title;
    private String genre;
    private String status;

    public BookRequest() {
    }

    public BookRequest(String title, String genre, String status) {
        this.title = title;
        this.genre = genre;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
