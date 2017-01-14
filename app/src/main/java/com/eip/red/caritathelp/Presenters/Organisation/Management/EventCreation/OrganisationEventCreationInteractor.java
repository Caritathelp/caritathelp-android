package com.eip.red.caritathelp.Presenters.Organisation.Management.EventCreation;

import android.content.Context;
import android.text.TextUtils;

import com.eip.red.caritathelp.Models.Network;
import com.eip.red.caritathelp.Models.Organisation.Event;
import com.eip.red.caritathelp.Models.Organisation.EventInformations;
import com.eip.red.caritathelp.Models.Organisation.Organisation;
import com.eip.red.caritathelp.Models.Profile.MainPictureJson;
import com.eip.red.caritathelp.Models.User.User;
import com.eip.red.caritathelp.Presenters.SubMenu.MyOrganisations.OrganisationCreation.IOnOrganisationCreationsFinishedListener;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;

/**
 * Created by pierr on 18/03/2016.
 */
public class OrganisationEventCreationInteractor {

    static final private String     ERROR_MANDATORY = "Ce champ est obligatoire";

    private Context context;
    private User user;
    private int     organisationId;
    private String  encodedImg;

    public OrganisationEventCreationInteractor(Context context, User user, int organisationId) {
        this.context = context;
        this.user = user;
        this.organisationId = organisationId;
        encodedImg = null;
    }

    public void createEvent(IOnOrganisationEventCreationFinishedListener listener, HashMap<String, String> data) {
        if (!checkErrors(listener, data))
            requestAPI(listener, data);
    }

    private boolean checkErrors(IOnOrganisationEventCreationFinishedListener listener, HashMap<String, String> data) {
        boolean error = false;

        // Check Title
        if (TextUtils.isEmpty(data.get("title"))) {
            listener.onTitleError(ERROR_MANDATORY);
            error = true;
        }

        // Check Description
        if (TextUtils.isEmpty(data.get("description"))) {
            listener.onDescriptionError(ERROR_MANDATORY);
            error = true;
        }

        return (error);
    }


    private void requestAPI(final IOnOrganisationEventCreationFinishedListener listener, HashMap<String, String> data) {
        JsonObject json = new JsonObject();

        json.addProperty("token", user.getToken());
        json.addProperty("assoc_id", organisationId);
        json.addProperty("title", data.get("title"));
        json.addProperty("description", data.get("description"));
        json.addProperty("place", data.get("location"));
        json.addProperty("begin", data.get("date begin"));
        json.addProperty("end", data.get("date end"));

        System.out.println("BEGIN : " + data.get("date begin"));
        System.out.println("END : " + data.get("date end"));

        Ion.with(context)
                .load("POST", Network.API_LOCATION + Network.API_REQUEST_ORGANISATION_EVENTS)
                .setHeader("access-token", user.getToken())
                .setHeader("client", user.getClient())
                .setHeader("uid", user.getUid())
                .setJsonObjectBody(json)
                .as(new TypeToken<EventInformations>(){})
                .setCallback(new FutureCallback<EventInformations>() {
                    @Override
                    public void onCompleted(Exception error, EventInformations result) {
                        if (error == null) {
                            // Status == 400 == error
                            if (result.getStatus() == Network.API_STATUS_ERROR)
                                listener.onDialogError("Statut 400", result.getMessage());
                            else {
                                Event event = result.getResponse();
                                if (encodedImg != null)
                                    uploadEventImg(event, encodedImg, "filename.jpg", "original_filename.jpg", true, listener);
                                else
                                    listener.onSuccess(event.getId(), event.getTitle());
                            }
                        }
                        else
                            listener.onDialogError("Problème de connection", "Vérifiez votre connexion Internet");
                    }
                });
    }

    public void uploadEventImg(final Event event, String file, String filename, String originFilename, boolean isMain, final IOnOrganisationEventCreationFinishedListener listener) {
        JsonObject json = new JsonObject();
        json.addProperty("file", file);
        json.addProperty("filename", filename);
        json.addProperty("original_filename", originFilename);
        json.addProperty("is_main", isMain);
        json.addProperty("event_id", event.getId());

        Ion.with(context)
                .load("POST", Network.API_LOCATION_2 + Network.API_REQUEST_PICTURES)
                .setHeader("access-token", user.getToken())
                .setHeader("client", user.getClient())
                .setHeader("uid", user.getUid())
                .setJsonObjectBody(json)
                .as(new TypeToken<JsonObject>() {})
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        if (error == null)
                            listener.onSuccess(event.getId(), event.getTitle());
                        else
                            listener.onDialogError("Problème de connection", "Vérifiez votre connexion Internet");
                    }
                });
    }

    public void setEncodedImg(String encodedImg) {
        this.encodedImg = encodedImg;
    }

}
