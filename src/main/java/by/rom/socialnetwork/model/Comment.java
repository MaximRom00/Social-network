package by.rom.socialnetwork.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "message")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
}
