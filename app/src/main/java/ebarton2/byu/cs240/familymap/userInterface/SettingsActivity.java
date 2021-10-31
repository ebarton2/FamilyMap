package ebarton2.byu.cs240.familymap.userInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import ebarton2.byu.cs240.familymap.DataCache;
import ebarton2.byu.cs240.familymap.R;
import ebarton2.byu.cs240.familymap.model.Settings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch lifeStory = findViewById(R.id.switch1);
        lifeStory.setChecked(Settings.instance().getLifeStoryLines());
        lifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.instance().setLifeStoryLines(isChecked);
            }
        });

        Switch familyTree = findViewById(R.id.switch2);
        familyTree.setChecked(Settings.instance().getFamilyTreeLines());
        familyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.instance().setFamilyTreeLines(isChecked);
            }
        });

        Switch spouseLines = findViewById(R.id.switch3);
        spouseLines.setChecked(Settings.instance().getSpouseLines());
        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.instance().setSpouseLines(isChecked);
            }
        });

        Switch fatherSide = findViewById(R.id.switch4);
        fatherSide.setChecked(Settings.instance().getPaternalLines());
        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.instance().setPaternalLines(isChecked);
            }
        });

        Switch motherSide = findViewById(R.id.switch5);
        motherSide.setChecked(Settings.instance().getMaternalLines());
        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.instance().setMaternalLines(isChecked);
            }
        });

        Switch maleEvents = findViewById(R.id.switch6);
        maleEvents.setChecked(Settings.instance().getMaleEvents());
        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.instance().setMaleEvents(isChecked);
            }
        });

        Switch femaleEvents = findViewById(R.id.switch7);
        femaleEvents.setChecked(Settings.instance().getFemaleEvents());
        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.instance().setFemaleEvents(isChecked);
            }
        });


        LinearLayout logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.instance().clear();
                DataCache.instance().setLoginState(false);
                Settings.instance().clear();
                finish();
                Toast.makeText(SettingsActivity.this, "You will never logout", Toast.LENGTH_SHORT).show();
            }
        });
    }
}