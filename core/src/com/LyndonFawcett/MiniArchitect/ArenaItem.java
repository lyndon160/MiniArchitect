package com.LyndonFawcett.MiniArchitect;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class ArenaItem extends ModelInstance implements InputProcessor, GestureListener{
	boolean selected=false;
	boolean delete=false;
	static boolean move = true;
    public BoundingBox bounds = new BoundingBox();
    public Vector3 center = new Vector3();
    public Vector3 dimensions = new Vector3();
    static public ArrayList<ArenaItem> walls;
    boolean wall = false;
    float rotation;
	
    //Build normal object
	public ArenaItem(Model model,InputMultiplexer multiplexer) {
		super(model);
		Gdx.app.log("Model", this.toString());
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));
		calculateBoundingBox(bounds);
		center = bounds.getCenter();
        dimensions = bounds.getDimensions();
		for (ArenaItem i:Arena.instances) {
			i.draggable=false;
			i.selected=false;
		}
        selected= true;
	}

	//Constructor building a wall
	//Magnet listener - when a wall gets close to another wall it clips
	//Going to need a start point and end point for placement and dimensions of the wall
	public ArenaItem(InputMultiplexer multiplexer,double length, double rotation, Vector3 placement) {
		super(new ModelBuilder().createBox((float)length, 1f, 0.4f, 
	            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
	            Usage.Position | Usage.Normal));
		this.rotation=(float) rotation-90;
       // Arena.pCam.unproject(placement);
    	placement.y=0.5f;
		this.transform.setToTranslation(placement);

		this.transform.rotate(0, 1, 0, (float) rotation-90);
		Gdx.app.log("Model", this.toString());
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));
		
        bounds.set(new Vector3(1f,1f,1f), new Vector3(0,0,0));
		
		center = bounds.getCenter();
        dimensions = bounds.getDimensions();

		wall = true;
		//this.transform.idt();
		//calculateBoundingBox(bounds);
		
		for (ArenaItem i:Arena.instances) {
			i.draggable=false;
			i.selected=false;
		}
        selected= true;

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

	
	
	Vector3 planeIntersection = new Vector3();
	Vector3 boxIntersection = new Vector3();
	final Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);
	boolean draggable = false;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		//Gdx.app.log(this.toString(), bounds.getDimensions()+"");

		//if it intersects with me move me!
		Ray pickRay = Arena.pCam.getPickRay(screenX, screenY);
		
		//bounds.mul(this.transform);
		//calculateBoundingBox(bounds);
		//	if(Intersector.intersectRayBounds(pickRay, bounds.set(bounds.min.mul(this.transform), bounds.max.mul(this.transform)), boxIntersection)){
			
		
		if(Intersector.intersectRayBoundsFast(pickRay,transform.getTranslation(new Vector3()),dimensions)){
			
			Gdx.app.log(this.toString(), "Touched!");
					
	
					for (ArenaItem i:Arena.instances) {
						i.draggable=false;
						i.selected=false;
					}
					selected = true;
					draggable = true;
					//Pixmap colour = new Pixmap(16,16, Pixmap.Format.RGBA8888);
					//colour.setColor(Color.OLIVE);
					//this.materials.get(0).set(TextureAttribute.createSpecular(new Texture(colour)));
					return false;
				}
				
		draggable = false;
			
			return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//calculateBoundingBox(bounds);
		//if(this.selected)
		//bounds.mul(this.transform);
		
		Gdx.app.log(this.toString(), this.selected+"");
		//selected = false;
		return false;
	}
	public static void stopTouch(){
		move = false;
	}
	public static void resumeTouch(){
		move = true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Ray pickRay = Arena.pCam.getPickRay(screenX, screenY);
		if(this.draggable&&move){
		//	calculateBoundingBox(bounds);
			Intersector.intersectRayPlane(pickRay, xzPlane, planeIntersection);
			//multiply not set to
			
			
			//save rotation
			

			Quaternion rot = this.transform.getRotation(new Quaternion());
			this.transform.set(planeIntersection.x,0 ,planeIntersection.z,	rot.x,rot.y,rot.z,rot.w);
			//this.transform.set
			

			
			//Check and see if model in deletebox
			if(screenY < Stroke.deleteBox.getHeight()){
				System.out.println("Delete me " + screenY);
				delete=true;
				Arena.instances.removeValue(this, false);
			}
			
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

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	
	Vector2 lastA, lastB;
	//rotate object
	@Override	
	public boolean pinch(Vector2 oldA, Vector2 oldB,Vector2 newA, Vector2 newB) {
		if(!Stroke.grab){
			if(lastA != null && this.selected){
				//TODO add check to see if this arena item is selected
				
				Gdx.app.log("Pinch a", lastA.toString() + newA.toString());
				Gdx.app.log("Pinch b", lastB.toString() + newB.toString());
				
				//Need to check if new and old have changed
				if(lastA.y == newA.y && lastB.y == newB.y){
					Gdx.app.log("Pinch", "Same so returning");
					return false;
				}
				
				//check direction by comparing X
				if(oldA.x > oldB.x){
					if(lastA.y <= newA.y && lastB.y >= newB.y){
						Gdx.app.log("Pinch", "Rotate negative");
						this.transform.rotate(0, 1, 0, -100*Gdx.graphics.getDeltaTime());
						}
					else if(lastA.y >= newA.y && lastB.y <= newB.y){
						Gdx.app.log("Pinch", "Rotate positive");
						this.transform.rotate(0, 1, 0, 100*Gdx.graphics.getDeltaTime());
					}
				}
				
				else{
					if(lastA.y <= newA.y && lastB.y >= newB.y){
						Gdx.app.log("Pinch", "Rotate negative");
						this.transform.rotate(0, 1, 0, 100*Gdx.graphics.getDeltaTime());
					}
					else if(lastA.y >= newA.y && lastB.y <= newB.y){
						Gdx.app.log("Pinch", "Rotate positive");
						this.transform.rotate(0, 1, 0, -100*Gdx.graphics.getDeltaTime());
					}
				}

			}
			
			lastA = newA.cpy();
			lastB = newB.cpy();
		
		}		
        return false;
	}
	
}
