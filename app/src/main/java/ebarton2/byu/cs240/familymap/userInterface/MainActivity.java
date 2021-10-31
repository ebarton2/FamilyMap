package ebarton2.byu.cs240.familymap.userInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FillRequest;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;



import ebarton2.byu.cs240.familymap.DataCache;
import ebarton2.byu.cs240.familymap.R;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private MapsFragment mMapsFragment;
    private FragmentManager fm;
    private boolean state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        fm = this.getSupportFragmentManager();

        if (DataCache.instance().getLoginState() == false) {
            state = false;
            loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragmentContainer);
            if (loginFragment == null) {

                loginFragment = new LoginFragment();
                Bundle args = new Bundle();
                loginFragment.setArguments(args);
            }
            fm.beginTransaction().add(R.id.fragmentContainer, loginFragment).commit();

        } else {
            mMapsFragment = (MapsFragment) fm.findFragmentById(R.id.fragmentContainer);
            state = true;
            if (mMapsFragment == null) {

                mMapsFragment = new MapsFragment();
                Bundle args = new Bundle();
                mMapsFragment.setArguments(args);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            fm.beginTransaction().add(R.id.fragmentContainer, mMapsFragment).commit();
        }
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        if (state != DataCache.instance().getLoginState()) {
            System.out.println("Things are working");
            swapFragment(DataCache.instance().getLoginState());
        }

    }

    public void swapFragment(boolean type) {
        invalidateOptionsMenu();

        if (type) {
            fm.beginTransaction().remove(loginFragment).commit();
            state = true;
            if (mMapsFragment == null) {
                mMapsFragment = new MapsFragment();
                Bundle args = new Bundle();
                mMapsFragment.setArguments(args);
            }
            fm.beginTransaction().add(R.id.fragmentContainer, mMapsFragment).commit();
        } else {

            fm.beginTransaction().remove(mMapsFragment).commit();
            state = false;
            if (loginFragment == null) {
                loginFragment = new LoginFragment();
                Bundle args = new Bundle();
                loginFragment.setArguments(args);
            }
            fm.beginTransaction().add(R.id.fragmentContainer, loginFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (DataCache.instance().getLoginState()) {
            new MenuInflater(this).inflate(R.menu.main_activity_menu, menu);
            menu.findItem(R.id.settings_gear).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gear).colorRes(R.color.grey).actionBarSize());
            menu.findItem(R.id.search_badge).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search).colorRes(R.color.grey).actionBarSize());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.search_badge:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                //intent.putExtra()
                startActivity(intent);
                Toast.makeText(this, "Search Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings_gear:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);

                Toast.makeText(this, "Settings Button", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
