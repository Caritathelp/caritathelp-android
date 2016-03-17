package com.eip.red.caritathelp.Views.SubMenu.MyOrganisations.OrganisationCreation;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.eip.red.caritathelp.Main.MainActivity;
import com.eip.red.caritathelp.Models.Network;
import com.eip.red.caritathelp.Presenters.SubMenu.MyOrganisations.OrganisationCreation.OrganisationCreationPresenter;
import com.eip.red.caritathelp.R;

/**
 * Created by pierr on 23/02/2016.
 */

public class OrganisationCreationView extends Fragment implements IOrganisationCreation, View.OnClickListener{

    private OrganisationCreationPresenter presenter;

    private EditText    name;
    private EditText    theme;
    private EditText    city;
    private EditText    description;
    private ProgressBar progressBar;

    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Network Model
        Network network = ((MainActivity) getActivity()).getModelManager().getNetwork();

        // Init Presenter
        presenter = new OrganisationCreationPresenter(this, network);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View    view = inflater.inflate(R.layout.fragment_organisation_creation, container, false);

        // Init UI Element
        name = (EditText) view.findViewById(R.id.organisation_creation_name);
        theme = (EditText) view.findViewById(R.id.organisation_creation_theme);
        city = (EditText) view.findViewById(R.id.organisation_creation_city);
        description = (EditText) view.findViewById(R.id.organisation_creation_description);
        progressBar = (ProgressBar) view.findViewById(R.id.organisation_creation_progress_bar);

        // Init Button Listener
        view.findViewById(R.id.top_bar_organisation_creation_return).setOnClickListener(this);
        view.findViewById(R.id.organisation_creation_btn_create).setOnClickListener(this);

        // Init Dialog
        dialog = new AlertDialog.Builder(getActivity().getBaseContext())
                .setCancelable(true)
                .create();

        return (view);
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId());
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setNameError(String error) {
        name.setError(error);
    }

    @Override
    public void setThemeError(String error) {
        theme.setError(error);
    }

    @Override
    public void setCityError(String error) {
        city.setError(error);
    }

    @Override
    public void setDescriptionError(String error) {
        description.setError(error);
    }

    @Override
    public void setDialogError(String title, String msg) {
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

    @Override
    public void navigateToOrganisationView(String name) {
        // Display Dialog Box
        dialog.setTitle("Création réussie");
        dialog.setMessage("Bienvenue sur la page d'accueil de l'association " + name);
        dialog.show();
    }

    public EditText getName() {
        return name;
    }

    public EditText getTheme() {
        return theme;
    }

    public EditText getCity() {
        return city;
    }

    public EditText getDescription() {
        return description;
    }
}
