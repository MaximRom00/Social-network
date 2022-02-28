package by.rom.socialnetwork.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "author")
@EqualsAndHashCode(exclude = "author")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Message cannot be empty.")
    @Size(min = 5, max = 2048, message = "Message should be between 5 and 2048 char.")
    private String text;

    @NotBlank(message = "Tag cannot be empty.")
    @Size(min = 3, max = 50, message = "Tag should be between 3 and 50 char.")
    private String tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "file_name")
    private String fileName;


    @OneToMany(mappedBy = "message", cascade = CascadeType.REMOVE)
    public List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "message_like",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    public List<User> likes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "message_dislike",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    public List<User> dislikes = new ArrayList<>();

}
