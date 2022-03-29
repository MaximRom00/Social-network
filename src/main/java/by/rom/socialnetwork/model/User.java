package by.rom.socialnetwork.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"subscribers", "subscriptions", "messages", "senderMessages", "receiverMessages"})
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 3, max = 20, message = "Name should be between 3 and 20 char.")
    private String name;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message="Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;

    @Transient
    private String rpassword;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private boolean isEnabled;

    private String avatarName;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToMany(mappedBy = "sender")
    private List<PrivateMessage> senderMessages;

    @OneToMany(mappedBy = "receiver")
    private List<PrivateMessage> receiverMessages;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subscriptions", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")})
    private List<User> subscribers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subscriptions", joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> subscriptions = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singleton(authority);
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
