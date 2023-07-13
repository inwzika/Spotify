package io.swagger.client;

public class User {
    public   String username;
    public String password;
    public Playlist[] playlists;
    public String token;


    public User(String username , String password){
        this.username = username;
        this.password = password;
    }
public boolean passwordISvalid(String password) throws Exception{
    char[] passwordValid = password.toCharArray();
            String str;
            int valid1 = 0, valid2 = 0, valid3 = 0;

            for (int i = 0; i < password.length(); i++) {
                str = String.valueOf(passwordValid[i]);
                if (str.matches("[A-Z]")) {
                    valid1++;
                }
                if (str.matches("[a-z]")) {
                    valid2++;
                }
                if (str.matches("[0-9]")) {
                    valid3++;
                }
            }
            int sum = valid1 + valid2 + valid3;
            if (sum >= 8 && valid1 != 0 && valid2 != 0 && valid3 != 0) {
               return true;
            }
            return false;

}
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPlaylists(Playlist[] playlists) {
        this.playlists = playlists;
    }

    public Playlist[] getPlaylists() {
        return playlists;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
