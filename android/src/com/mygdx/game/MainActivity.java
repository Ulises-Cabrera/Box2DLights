package com.mygdx.game;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    public MainActivity() {
        super(R.layout.activity_main);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGame();
    }

    private void loadGame() {
        GameFragment libgdxFragment = new GameFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.game_fragment);

        if (fragment != null)
            transaction.replace(R.id.game_fragment, libgdxFragment).commitNow();
        else
            transaction.add(R.id.game_fragment, libgdxFragment).commitNow();
    }

    @Override
    public void exit() {
        Log.d("", "exit");
    }
}
