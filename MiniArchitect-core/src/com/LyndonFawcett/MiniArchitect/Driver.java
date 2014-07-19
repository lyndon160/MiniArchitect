package com.LyndonFawcett.MiniArchitect;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Driver implements ApplicationListener, InputProcessor{

	SpriteBatch batch;
	ArrayList<Sprite> sketch;
	Sprite pixel;
	Pixmap colour, colourDebug;
	private OrthographicCamera cam;
	int SPRITECOUNT=400;
	int pointer=0;
	@Override
	public void create() {
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.setProjectionMatrix(cam.combined);
		Gdx.input.setInputProcessor(this);
		sketch= new ArrayList<Sprite>();
	
		colour =new Pixmap(1,1,Format.RGBA8888);
		colourDebug =new Pixmap(1,1,Format.RGBA8888);
		
		colourDebug.setColor(Color.RED);
		colourDebug.fillCircle(0,0, 10);
		colour.setColor(Color.CYAN);
		colour.fill();
		for(int i=0; i<SPRITECOUNT;i++)
			sketch.add(new Sprite(new Texture(colour), 10,10));
		pixel=new Sprite(new Texture(colour), 4,4);
		cam.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		cam.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		batch.begin();
		for(Sprite pixel:sketch){
			pixel.draw(batch);
		}
		batch.end();
	
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		create();
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastPos = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	Vector2 lastPos;
	Vector3 tp;
	@Override
	public boolean touchDragged(int screenX, int screenY, int p) {
		int x=screenX;
		int y=screenY;
		int diffX=screenX-(int)lastPos.x;
		int diffY=screenY-(int)lastPos.y;
		Gdx.app.log("diffX", diffX+"");
		Gdx.app.log("diffY", diffY+"");
		float steps=0;
		pixel=new Sprite(new Texture(colour), 4,4);
		Sprite debug=new Sprite(new Texture(colourDebug), 10,10);
		tp = new Vector3(screenX, screenY,0);
		cam.unproject(tp);
		debug.setPosition(tp.x, tp.y);
	//	sketch.add(debug);



		if(diffX!=0&&diffY!=0){	
			steps =(float)diffY/(float)diffX;
			Gdx.app.log("Step", steps+"");
		}
	

		if(diffX ==0&&diffY!=0){
			for(int i =(int)lastPos.y;i<screenY;i+=10){
				//Sprite pixel;
				//	pixel=new Sprite(new Texture(colour), 4,4);
				tp = new Vector3(screenX, i,0);
				cam.unproject(tp);
				//pixel.setPosition(tp.x, tp.y);
				sketch.get(pointer++).setPosition(tp.x, tp.y);
				//sketch.add(pixel);
			}

			for(int i =(int)lastPos.y;i>screenY;i-=10){
				//Sprite pixel=new Sprite(new Texture(colour), 4,4);
				tp = new Vector3(screenX, i,0);
				cam.unproject(tp);
				//pixel.setPosition(tp.x, tp.y);
				//sketch.add(pixel);

				sketch.get(pointer++).setPosition(tp.x, tp.y);
			}
		}


		for(int i =(int)lastPos.x;i<screenX;i+=10){
			//Sprite pixel=new Sprite(new Texture(colour), 4,4);
			tp = new Vector3(i, (float) (lastPos.y+=steps*10),0);
			cam.unproject(tp);
			//pixel.setPosition(tp.x, tp.y);
			//sketch.add(pixel);
			sketch.get(pointer++).setPosition(tp.x, tp.y);
		}

		for(int i =(int)lastPos.x;i>screenX;i-=10){
		//	Sprite pixel=new Sprite(new Texture(colour), 4,4);
			tp = new Vector3(i, (float) (lastPos.y-=steps*10),0);
			cam.unproject(tp);
			//pixel.setPosition(tp.x, tp.y);
		//	sketch.add(pixel);
			
			sketch.get(pointer++).setPosition(tp.x, tp.y);
		}

		lastPos.set(x, y);
		if(pointer+200>SPRITECOUNT)
			pointer=0;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}