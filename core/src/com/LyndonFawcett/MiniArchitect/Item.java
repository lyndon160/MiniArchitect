package com.LyndonFawcett.MiniArchitect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector3;


/**
 * 
 * @author Lyndon
 *
 * Represents an object within the scene
 * This will hold data about 3d i.e. colour .obj file, size, scale, rotation.
 * 
 * Contains sprite cross, should be shown after item has been clicked hovering at the top right of the item
 * 
 */
public class Item extends Sprite implements InputProcessor{
	

	public boolean touched = false;
	public Sprite cancel;
	public int id =0;
	public Item(Texture tex){
		super(tex);
		id = (int) (Math.random()*100000);
		Stroke.multiplexer.addProcessor(this);
		cancel = new Sprite(new Texture(Gdx.files.internal("cancel.png")));

		//select this item and unselect all others
		for (Item i:Stroke.furnishings) {
			i.selected=false;
		}
		selected = true;
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
		// TODO Auto-generated method stub
		return false;
	}

	Vector3 touchPoint = new Vector3();	
	boolean selected = true; // maybe implement highlighting object
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchPoint.set(screenX, screenY, 0);
		Stroke.cam.unproject(touchPoint);
		//Gdx.app.log(touchPoint.x+"", ""+touchPoint.y);
		//Gdx.app.log("x", ""+this.getBoundingRectangle().x);
		//Gdx.app.log("y", ""+this.getBoundingRectangle().y);
		
		//If top right is touched then close!
		if(cancel.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
			Gdx.app.log("removing","item :"+id);
			this.dispose();
			return false;
		}
		if(this.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
			//Gdx.app.log("Sprite", "Touched!");
			touched = true;
			
	
			for (Item i:Stroke.furnishings) {
				i.selected=false;
			}
			selected = true;
		}
		return false;
	}
	
	public void dispose(){
	//////	Stroke.multiplexer.removeProcessor(this);
	//	Stroke.furnishings.remove(this);
		for (int i=0; i<Stroke.furnishings.size();i++) {
			if(Stroke.furnishings.get(i).id==this.id){
				Stroke.furnishings.remove(i);
				return;
			}
		}
	}
	
	//draw me and my cancel button (only draw cancel if selected)
	boolean once=true;
	public void drawAll(SpriteBatch batch){
		this.draw(batch);
		if(selected)
			cancel.draw(batch);	
		if(once){
			cancel.setBounds(this.getX()+this.getWidth(), this.getY()+this.getHeight(),100,100);
			once=false;
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touched = false;
		return false;
	}

		//update this and cancel option
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(touched){
			touchPoint.set(screenX, screenY, 0);
			Stroke.cam.unproject(touchPoint);
			this.setPosition(touchPoint.x-this.getWidth()/2, touchPoint.y-this.getHeight()/2);
			cancel.setPosition(touchPoint.x+this.getWidth()/2, touchPoint.y+this.getHeight()/2);
		}
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
