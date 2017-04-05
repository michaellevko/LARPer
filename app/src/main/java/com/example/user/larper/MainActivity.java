package com.example.user.larper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.larper.Model.Model;

public class MainActivity extends Activity
        implements ProfileFragment.OnProfileFragmentListener,
        BlueprintsFragment.OnBlueprintFragmentListener,
        NewBlueprintFragment.OnNewBlueprintFragmentListener,
        LoreFragment.OnLoreFragmentListener{

    String acctDisplayName;
    int acctId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "MainActivity");
        //String acctDisplayName = this.getIntent().getExtras().getString(getResources().getString(R.string.account_display_name));

        // Setting the navigation tabs
        final ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(actionbar.NAVIGATION_MODE_TABS);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);


        /* Check Firebase for user profile
         * If profile doesnt exist, call InitProfileFragment
         * Else, pull user profile and call HomeTabFragment*/

        getFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, new ProfileFragment())
                .commit();
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
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, frag, tag).commit();
        }
    }

    @Override
    public void profileDeleted() {
        /*TODO: CALL SQLITE TO DELETE ALL RELATED ITEMS*/
        Model.getInstance().deleteProfile();
        Model.getInstance().getBlueprints().clear();
        Model.getInstance().getLore().clear();
        /*TODO: DELETE MAPS*/
        gotoInitProfileActivity();
    }

    private void gotoInitProfileActivity(){
        Intent intent = new Intent(this, InitProfileActivity.class);
        intent.putExtra(getApplicationContext().getResources()
                .getString(R.string.account_display_name), acctDisplayName);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void gotoNewBlueprintInterface() {
        this.goToFragment(new NewBlueprintFragment(), getString(R.string.new_blueprint_fragment));
    }

    @Override
    public void gotoBlueprintInterface() {
        this.goToFragment(new BlueprintsFragment(), getString(R.string.blueprints_fragment));
    }

    @Override
    public void gotoNewLoreInterface() {
        this.goToFragment(new NewLoreFragment(), getString(R.string.lore_fragment));
    }
}
