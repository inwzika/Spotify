package io.swagger.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.User;
import io.swagger.client.api.AuthApi;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.api.PremiumUsersApi;
import io.swagger.client.api.UsersApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.auth.OAuth;
import io.swagger.client.model.AuthLoginBody;
import io.swagger.client.model.AuthSignupBody;
import io.swagger.client.model.PlaylistsBody;

import java.util.Date;
import java.util.Scanner;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static final String MY_API_KEY = "ed93cf058a0f4c2db6a179e1fe3cac752cec6b26";
    public static ApiClient defaultClient;
    public static DefaultApi defaultApi;
    public static AuthApi authApi;
    public static UsersApi usersApi;
    public static User currentUser;
    public static PremiumUsersApi premiumUsersApi;


    public static long start = 0;
    public static String tracks;
    public static boolean isFirstCall = true;

    public static void authAPIKey() {
        defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey(MY_API_KEY);
        defaultApi = new DefaultApi();
        authApi = new AuthApi(defaultClient);
        usersApi = new UsersApi(defaultClient);
    }

    public static void loginProcess() {
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_GREEN + "Enter username" +ANSI_GREEN);
        String username = input.next();
        System.out.println(ANSI_YELLOW+"Enter password"+ANSI_YELLOW);
        String password = input.next();

        String token = "";
        try {
            AuthLoginBody authLoginBody = new AuthLoginBody();
            authLoginBody.setUsername(username);
            authLoginBody.setPassword(password);
            token = (authApi.login(authLoginBody).getToken());
            currentUser = new User(username, password);
            System.out.println("You Successfully logged in");
            currentUser.token = token;
            defaultClient.setAccessToken(currentUser.token);
            OAuth bearerAuth = (OAuth) defaultClient.getAuthentication("bearerAuth");
            bearerAuth.setAccessToken(currentUser.token);

            userMenuProcess();
            currentUser = new User(username, password);
            currentUser.token = token;
        } catch (ApiException apiException) {
            String errorResponse = apiException.getResponseBody();
            System.out.println(ANSI_RED+errorResponse+ANSI_RED);
            if (errorResponse.contains(ANSI_RED+"invalid username or password"+ANSI_RED)) {
                loginProcess();
            }
        }
    }

    public static void signupProcess() {
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_PURPLE+"Enter username"+ANSI_PURPLE);
        String username = input.next();
        System.out.println(ANSI_PURPLE+"Enter password"+ANSI_PURPLE);
        String password = input.next();



        try {
            currentUser = new User(username,password);
            while (!currentUser.passwordISvalid(password)){
                System.out.println(ANSI_RED+"please enter the correct password"+ANSI_RED);
                password = input.next();
            }
            currentUser.setPassword(password);

            String token = " ";

            AuthSignupBody authSignupBody = new AuthSignupBody();
            authSignupBody.setUsername(username);
            authSignupBody.setPassword(password);
            token = (authApi.signUp(authSignupBody).getToken());
            currentUser = new User(username, password);
            System.out.println(ANSI_BLUE+"You Successfully singed up"+ANSI_BLUE);
            currentUser.token = token;
            defaultClient.setAccessToken(currentUser.token);
            OAuth bearerAuth = (OAuth) defaultClient.getAuthentication("bearerAuth");
            bearerAuth.setAccessToken(currentUser.token);

            userMenuProcess();
            currentUser = new User(username, password);
            currentUser.token = token;
        } catch (ApiException apiException) {
            String errorResponse = apiException.getResponseBody();
            System.out.println(errorResponse);
            if (errorResponse.contains(ANSI_RED+"invalid username or password"+ANSI_RED)){
                signupProcess();
            }
            if (errorResponse.contains(ANSI_RED+ "username already taken" + ANSI_RED)){
                signupProcess();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void userMenuProcess() throws ApiException {
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_YELLOW+"1-Profile\n2-Tracks"+ANSI_YELLOW);
        int choice = input.nextInt();
        if (choice == 1) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - start > 20 || isFirstCall) {
                try {
                    System.out.println(usersApi.getProfileInfo().toString());
                    start = System.currentTimeMillis() / 1000;
                    int exit = input.nextInt();
                    isFirstCall = false;
                    userMenuProcess();
                } catch (ApiException apiException) {
                    System.out.println(apiException.getResponseBody());
                    isFirstCall = false;
                }
            } else {
                //cached data

                System.out.println();
            }
        } else if (choice == 2) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - start > 20) {
                try {
                    tracks = usersApi.getTracksInfo().toString();
                    System.out.println(tracks);
                    start = System.currentTimeMillis() / 1000;
                } catch (ApiException apiException) {
                    System.out.println(apiException.getResponseBody());
                }
            } else {
                //System.out.println(tracks);
                setTracks();
            }
        }
    }
    public static void setTracks()  {
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_PURPLE+"1-make new playlist \n 2-get all play list \n 3-delete playlist \n 4-add song \n 5-remove song"+ANSI_PURPLE);
        int choice = input.nextInt();
        if (choice == 1){
        makeNewPlaylist();
        }
        else if (choice == 2){
        getAllPlaylis();

            }
        else if (choice == 3){
            deleteplaylist();
        }
        else if (choice == 4){
            add_song();
        }
        else if (choice == 5){
            removeTrack();
        }

    }
public static void add_song(){
            try {
                Scanner input = new Scanner(System.in);
                int playlistid = input.nextInt();
                String trackid = input.next();
            System.out.println(usersApi.addTrackToPlaylist(playlistid, trackid));
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }
}
    public static void makeNewPlaylist(){
        try {
            Scanner input = new Scanner(System.in);
            System.out.println(ANSI_BLUE+"what is your playlist's name ?"+ANSI_BLUE);
            String playname = input.next();
            PlaylistsBody playlistsBody = new PlaylistsBody();
            playlistsBody.setName(ANSI_PURPLE+"My Favorite Songs"+ANSI_PURPLE);
            System.out.println(ANSI_YELLOW+usersApi.createPlaylist(playlistsBody).getId()+ANSI_YELLOW);
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }
    }
    public static void getAllPlaylis(){
        try {
            System.out.println(usersApi.getPlaylistsInfo());
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }
    }
    public static void deleteplaylist() {
        try {
            System.out.println(usersApi.deletePlaylist(1));
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());

        }
    }
    public static void removeTrack(){
        try {
            Scanner input = new Scanner(System.in);
            System.out.println(ANSI_GREEN+"Enter PlayList ID"+ANSI_GREEN);
            int playlistid = input.nextInt();
            System.out.println(ANSI_GREEN+"Enter Track ID"+ANSI_GREEN);
            String trackid = input.next();
            System.out.println(usersApi.removeTrackFromPlaylist(playlistid, trackid));
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }

    }

    public static void Upgrade_Premium() {
        try {
            System.out.println(usersApi.upgradeToPremium());
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());

        }
    }
    public static void listOFfriends(){
                try {
            System.out.println(premiumUsersApi.getFriends());
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }

    }
    public static void addNewfriend(){
                try {
                    Scanner input = new Scanner(System.in);
                    String FriendsUserName = input.next();
            System.out.println(premiumUsersApi.addFriend(FriendsUserName));
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }

    }
    public static void Upgrade_100Percent_Premium(){
                try {
            System.out.println(usersApi.upgradeToPremiumTest());
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }

    }
    public static void FriendRequests_List(){
        try {
            System.out.println(premiumUsersApi.getFriendRequests());
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }

    }
    public static void Accept_Sb_Friends_Req(){
                try {
                    Scanner input = new Scanner(System.in);
                    String friendUsername = input.next();
            System.out.println(premiumUsersApi.addFriend(friendUsername));
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }

    }
    public static void Get_Friends_Playlist(){
        try {
            Scanner input = new Scanner(System.in);
            String friendUsername = input.next();
            System.out.println(premiumUsersApi.getFriendPlaylists(friendUsername));
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }
    }


    public static void main(String[] args) {
        authAPIKey();
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_PURPLE+"Welcome\n1-Login\n2-Signup"+ANSI_PURPLE);

        int choice = input.nextInt();
        if (choice == 1) loginProcess();
        if (choice == 2) signupProcess();

    }
}