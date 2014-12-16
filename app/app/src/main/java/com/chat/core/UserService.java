package com.chat.core;


import retrofit.http.GET;

public interface UserService {
    @GET(Constants.Http.URL_USERS_FRAG)
    UsersWrapper getUsers();
}
