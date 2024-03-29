package com.eip.red.caritathelp.Presenters.Home;

import android.content.Context;

import com.eip.red.caritathelp.Models.Network;
import com.eip.red.caritathelp.Models.News.News;
import com.eip.red.caritathelp.Models.News.NewsJson;
import com.eip.red.caritathelp.Models.News.NewsListJson;
import com.eip.red.caritathelp.Models.User.User;
import com.eip.red.caritathelp.R;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by pierr on 08/08/2016.
 */

public class HomeInteractor {

    private Context context;
    private User    user;

    public HomeInteractor(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    public void getNews(final IOnHomeFinishedListener listener) {
        Ion.with(context)
                .load("GET", Network.API_LOCATION + Network.API_REQUEST_NEWS)
                .setHeader("access-token", user.getToken())
                .setHeader("client", user.getClient())
                .setHeader("uid", user.getUid())
                .as(new TypeToken<NewsListJson>(){})
                .setCallback(new FutureCallback<NewsListJson>() {
                    @Override
                    public void onCompleted(Exception error, NewsListJson result) {
                        if (error == null) {
                            // Status == 400 == error
                            if (result.getStatus() == Network.API_STATUS_ERROR)
                                listener.onDialog("Statut 400", result.getMessage());
                            else
                                listener.onSuccessGetNews(result.getResponse());
                        }
                        else
                            listener.onDialog("Problème de connection", context.getString(R.string.connection_problem));
                    }
                });
    }

    public User getUser() {
        return user;
    }
}
