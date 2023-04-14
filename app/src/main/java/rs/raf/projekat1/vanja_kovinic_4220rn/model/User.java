package rs.raf.projekat1.vanja_kovinic_4220rn.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String email;
    private String username;
    private String password;

    private String url;

    public User(String email, String username, String password, String url) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.url = url;
    }


}
