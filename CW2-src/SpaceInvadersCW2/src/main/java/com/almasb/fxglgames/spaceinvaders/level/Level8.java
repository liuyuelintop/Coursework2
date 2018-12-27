package com.almasb.fxglgames.spaceinvaders.level;

import static com.almasb.fxgl.app.DSLKt.geti;
import static com.almasb.fxglgames.spaceinvaders.Config.ENEMIES_PER_ROW;
import static com.almasb.fxglgames.spaceinvaders.Config.ENEMY_ROWS;
import static com.almasb.fxglgames.spaceinvaders.Config.ENEMIES_PER_LEVEL;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.control.RandomMoveControl;
import com.almasb.fxglgames.spaceinvaders.Config;

import javafx.geometry.Rectangle2D;

public class Level8 extends SpaceLevel {

	 @Override
	    public void init() {
		    for( int x = 0; x < ENEMIES_PER_LEVEL; x++) {
		    	GameEntity enemy = spawnEnemy(x*40.625, 533.3);
		    }
	    }
	 
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
