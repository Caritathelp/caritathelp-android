package com.eip.red.caritathelp.Activities.Main.ViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.red.caritathelp.Models.Enum.Animation;
import com.eip.red.caritathelp.R;
import com.eip.red.caritathelp.Tools;
import com.eip.red.caritathelp.Views.Home.HomeView;
import com.eip.red.caritathelp.Views.MailBox.MailBoxView;
import com.eip.red.caritathelp.Views.OrganisationSearch.OrganisationSearchView;

/**
 * Created by pierr on 04/04/2016.
 */
public class MySecondPage extends Fragment {

    private MailBoxView mailBoxView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mailBoxView = (MailBoxView) MailBoxView.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.fragment_view_pager_second_page, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        Fragment    fragment = Tools.getLastFragment(getChildFragmentManager());
        if (fragment != null)
            Tools.replaceView(this, fragment, Animation.FADE_IN_OUT, true);
        else
            Tools.replaceView(this, mailBoxView, Animation.FADE_IN_OUT, true);
    }

    public MailBoxView getMailBoxView() {
        return mailBoxView;
    }
}
