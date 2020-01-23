package com.mygdx.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class GameFragment extends AndroidFragmentApplication {
	private GdxGame game;

	GameFragment(){
		this.game = new GdxGame();
	}

	public GdxGame getGame() {
		return game;
	}

	public GameplayScreen getGamePlayScreen(){
		return (GameplayScreen)game.getScreen();
	}

	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return initializeForView(game);
	}
}
