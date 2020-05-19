package com.example.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class MyGdxGame extends ApplicationAdapter {
	// batch for images (textures), shapeRenderer for Shapes
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	
	BitmapFont font;
	Texture background;
	Texture gameover;
	Texture[] birds = new Texture[2]; // array size is 2 (0, 1)
	Texture[] tubes = new Texture[2];
	
	float tubesX = 0;
	float tubesX2 = 0;
	
	Random random = new Random();
	float opening = 0;
	float opening2 = 0;
	
	int flapState = 0;
	
	Circle birdCircle;
	Rectangle tubeRect;
	
	float birdY = 0;
	float birdVelocity = 0;
	
	int gameState = 0;
	int score = 0;
	boolean []scoreReset = {false, false};
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10f);
		
		gameover = new Texture("gameover.png");
		
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		
		tubes[0] = new Texture("toptube.png");
		tubes[1] = new Texture("bottomtube.png");
		
		tubesX = Gdx.graphics.getWidth();
		tubesX2 = Gdx.graphics.getWidth() + Gdx.graphics.getWidth() * 2/3;
		
		random = new Random();
		opening = Gdx.graphics.getHeight()/ 2 + random.nextInt(400) - 400;
		random = new Random();
		opening2 = Gdx.graphics.getHeight()/ 2 + random.nextInt(400) - 400;
		
		birdY = Gdx.graphics.getHeight()/ 2 - birds[flapState].getHeight()/2;
		
		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		tubeRect = new Rectangle();
	}

	@Override
	public void render () {
		
		if (flapState == 0) {
			flapState = 1;
		} else if (flapState == 1) {
			flapState = 0;
		}
		
		if (gameState == 1) {
			
			if (Gdx.input.justTouched()) {
				birdVelocity = -25;
			}
			
			if (birdY > 0) {
				birdY -= birdVelocity;
				birdVelocity += 1.5;
				tubesX -= 5;
				tubesX2 -= 5;
				
				// bad code, shoulda used array for tubeX and loop here
				if (tubesX < -tubes[0].getWidth()) {
					tubesX = Gdx.graphics.getWidth() + 100;
					random = new Random();
					opening = Gdx.graphics.getHeight()/ 2 + random.nextInt(400) - 400;
					scoreReset[0] = false;
				} else if (tubesX2 < -tubes[0].getWidth()) {
					tubesX2 = Gdx.graphics.getWidth() + 100;
					random = new Random();
					opening2 = Gdx.graphics.getHeight()/ 2 + random.nextInt(400) - 400;
					scoreReset[1] = false;
				}
			} else {
				mDeathCode();
				gameState = 2;
            }
			
		} else {
			if (Gdx.input.justTouched()) {
				
				score = 0;
				gameState = 1;
			}
		}
		
		// batch.begin : "we will start displaying sprites now"
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		
		if (gameState != 2) {
			batch.draw(tubes[0], tubesX, opening + 250);
			batch.draw(tubes[1], tubesX, opening - tubes[1].getHeight() - 200);
			
			batch.draw(tubes[0], tubesX2, opening2 + 250);
			batch.draw(tubes[1], tubesX2, opening2 - tubes[1].getHeight() - 200);
			
			batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);
			
		} else {
			batch.draw(gameover, 100, 600, Gdx.graphics.getWidth() - 200, 600);
		}
		font.draw(batch, String.valueOf(score), 500, Gdx.graphics.getHeight() - 150);
		
		// batch.end : "we will stop drawing"?
		batch.end();
		
		
		// SHAPE RENDER SIMPLY DRAWS THE SHAPES TO BE SEEN
		//  THINK CTX.FILLRECT
		
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2 );
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		
		// ineffecient SHIT code, should have used arrays!
		// but since im just learning the commands, its no big deal
		tubeRect.set(tubesX, opening + 250, tubes[0].getWidth(), tubes[0].getHeight());
		//shapeRenderer.rect(tubeRect.x, tubeRect.y, tubeRect.width, tubeRect.height);
		
		if (Intersector.overlaps(birdCircle, tubeRect)) {
			mDeathCode();
		}
		
		tubeRect.set(tubesX, opening - tubes[1].getHeight() - 200, tubes[1].getWidth(), tubes[1].getHeight());
		//shapeRenderer.rect(tubeRect.x, tubeRect.y, tubeRect.width, tubeRect.height);
		if (Intersector.overlaps(birdCircle, tubeRect)) {
			mDeathCode();
		}
		tubeRect.set(tubesX2, opening2 + 250, tubes[0].getWidth(), tubes[0].getHeight());
		//shapeRenderer.rect(tubeRect.x, tubeRect.y, tubeRect.width, tubeRect.height);
		if (Intersector.overlaps(birdCircle, tubeRect)) {
			mDeathCode();
		}
		tubeRect.set(tubesX2, opening2 - tubes[1].getHeight() - 200, tubes[1].getWidth(), tubes[1].getHeight());
		//shapeRenderer.rect(tubeRect.x, tubeRect.y, tubeRect.width, tubeRect.height);
		if (Intersector.overlaps(birdCircle, tubeRect)) {
			mDeathCode();
		}
		//shapeRenderer.end();
		
		// bad scoring system or no?
		// rob used scoring tube which sounds not bad 🤔
		if (tubesX < Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2 && !scoreReset[0]) {
			score++;
			scoreReset[0] = true;
		} else if (tubesX2 < Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2 && !scoreReset[1]) {
			score++;
			scoreReset[1] = true;
		}
	}
	
	@Override
	public void dispose () {
	
	}
	
	public void mDeathCode() {
		gameState = 2;
		tubesX = Gdx.graphics.getWidth();
		tubesX2 = Gdx.graphics.getWidth() + Gdx.graphics.getWidth() * 2/3;
		birdY = Gdx.graphics.getHeight()/ 2 - birds[flapState].getHeight()/2;
		birdVelocity = 0;
		scoreReset[0] = false;
		scoreReset[1] = false;
	}
}
