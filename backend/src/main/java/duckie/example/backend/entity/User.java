package duckie.example.backend.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email", unique = true)
})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullname;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public User() {}


    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public String getFullname() { 
        return fullname; 
    }
    public void setFullname(String fullname) { 
        this.fullname = fullname; 
    }
    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }
    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }
    public Role getRole() { 
        return role; 
    }
    public void setRole(Role role) { 
        this.role = role; 
    }
    public UserStatus getStatus() { 
        return status; 
    }
    public void setStatus(UserStatus status) { 
        this.status = status; 
    }


    public static UserBuilder builder() { 
        return new UserBuilder(); 
    }

    public static final class UserBuilder {
        private Long id;
        private String fullname;
        private String username;
        private String email;
        private String password;
        private Role role;
        private UserStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        public UserBuilder id(Long id) { 
            this.id = id; return this; 
        }
        public UserBuilder fullname(String fullname) { 
            this.fullname = fullname; return this; 
        }
        public UserBuilder username(String username) { 
            this.username = username; return this; 
        }
        public UserBuilder email(String email) { 
            this.email = email; return this; 
        }
        public UserBuilder password(String password) { 
            this.password = password; return this; 
        }
        public UserBuilder role(Role role) { 
            this.role = role; return this; 
        }
        public UserBuilder status(UserStatus status) { 
            this.status = status; return this; 
        }
        public UserBuilder createdAt(Instant createdAt) { 
            this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(Instant updatedAt) { 
            this.updatedAt = updatedAt; return this; }

        public User build() {
            User user = new User();
            user.setId(this.id);
            user.setFullname(this.fullname);
            user.setUsername(this.username);
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setRole(this.role);
            user.setStatus(this.status);
            user.setCreatedAt(this.createdAt);
            user.setUpdatedAt(this.updatedAt);
            return user;
        }
    }
}