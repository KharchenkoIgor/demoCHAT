package project.demoChat.DTO;

public class ServerDTO {
    private Long id;
    private String name;
    private String type; // Добавили поле type

    public ServerDTO() {}

    public ServerDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}