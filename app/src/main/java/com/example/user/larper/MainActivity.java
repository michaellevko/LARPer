package com.example.user.larper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.larper.Model.Model;

public class MainActivity extends Activity
        implements InitProfileFragment.OnInitProfileFragmentListener,
                    ProfileFragment.OnProfileFragmentListener{

    String acctDisplayName;
    int acctId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "MainActivity");
        String acctDisplayName = this.getIntent().getExtras()
                .getString(getResources().getString(R.string.account_display_name));

        // Setting the navigation tabs
        final ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(actionbar.NAVIGATION_MODE_TABS);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);

        /*
        // Create tabs fragments and place in list for tab creation        
        final LinkedHashMap<String, Fragment> fragmentsHolder = new LinkedHashMap<>();
        fragmentsHolder.put(getResources()
                .getString(R.string.profile_fragment), ProfileFragment.newInstance());
        fragmentsHolder.put(getResources()
                .getString(R.string.map_fragment), new MapFragment());
        fragmentsHolder.put(getResources()
                .getString(R.string.lore_fragment), new LoreFragment());
        fragmentsHolder.put(getResources()
                .getString(R.string.blueprints_fragment), new BlueprintsFragment());
        fragmentsHolder.put(getResources()
                .getString(R.string.contacts_fragment), new ContactsFragment());


        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                String fragmentTag = tab.getText().toString();
                Fragment fragment = fragmentsHolder.get(fragmentTag);
                // If fragment isnt in backstack, add it, otherwise, show it
                if (getFragmentManager().findFragmentByTag(fragmentTag) == null){
                    ft.add(R.id.main_fragment_container , fragment, fragmentTag);
                } else {
                    ft.show(fragment);
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                ft.hide(fragmentsHolder.get(tab.getText().toString()));
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Tab creation
        for (Map.Entry<String, Fragment> entry : fragmentsHolder.entrySet()) {
            actionbar.addTab(actionbar.newTab().setText(entry.getKey()).setTabListener(tabListener));
        }

        /* Check Firebase for user profile
         * If profile doesnt exist, call InitProfileFragment
         * Else, pull user profile and call HomeTabFragment*/
        getFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, InitProfileFragment.newInstance(acctDisplayName))
                .commit();

    }

    @Override
    public void profileInitialized() {
        this.goToFragment(new ProfileFragment(), getString(R.string.profile_fragment));
    }

    @Override
    public void profileDeleted() {
        /*TODO: CALL SQLITE TO DELETE ALL RELATED ITEMS*/
        Model.getInstance().deleteProfile();
        Model.getInstance().getBlueprints().clear();
        Model.getInstance().getLore().clear();
        /*TODO: DELETE MAPS*/
        this.goToFragment(InitProfileFragment.newInstance(acctDisplayName),
                getString(R.string.init_profile_fragment));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile_tab: {
                this.goToFragment(new ProfileFragment(),
                        getString(R.string.profile_fragment));
                break;
            }
            case R.id.map_tab: {
                this.goToFragment(new MapFragment(),
                        getString(R.string.map_fragment));
                break;
            }
            case R.id.lore_tab: {
                this.goToFragment(new LoreFragment(),
                        getString(R.string.lore_fragment));
                break;
            }
            case R.id.blueprints_tab: {
                this.goToFragment(new BlueprintsFragment(),
                        getString(R.string.blueprints_fragment));
                break;
            }
            case R.id.contacts_tab: {
                this.goToFragment(new ContactsFragment(),
                        getString(R.string.contacts_fragment));
                break;
            }
        }
        return true;
    }

    private void goToFragment(Fragment frag, String tag) {
        getFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, frag, tag).commit();
    }
}
