package com.LyndonFawcett.MiniArchitect;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class ArenaItem extends ModelInstance implements InputProcessor, GestureListener{
	boolean selected=false;
    public BoundingBox bounds = new BoundingBox();
    public Vector3 center = new Vector3();
    public Vector3 dimensions = new Vector3();
    static public ArrayList<ArenaItem> walls;
    boolean wall = false;
    float rotation;
	
	public ArenaItem(Model model,InputMultiplexer multiplexer) {
		super(model);
		Gdx.app.log("Model", this.toString());
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));
		calculateBoundingBox(bounds);
		center = bounds.getCenter();
        dimensions = bounds.getDimensions();
        
	}

	//Constructor building a wall
	//Magnet listener - when a wall gets close to another wall it clips
	//Going to need a start point and end point for placement and dimensions of the wall
	public ArenaItem(InputMultiplexer multiplexer,double length, double rotation, Vector3 placement) {
		super(new ModelBuilder().createBox((float)length*5, 1f, 0.4f, 
	            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
	            Usage.Position | Usage.Normal));
		this.rotation=(float) rotation-90;
        Arena.pCam.unproject(placement);
    	placement.y=0;
		this.transform.setToTranslation(placement);

		this.transform.rotate(0, 1, 0, (float) rotation-90);
		Gdx.app.log("Model", this.toString());
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));
		
		center = bounds.getCenter();
        dimensions = bounds.getDimensions();
		wall = true;
		//this.transform.idt();
		calculateBoundingBox(bounds);
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
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		Gdx.app.log(this.toString(), bounds.getDimensions()+"");

		//if it intersects with me move me!
		Ray pickRay = Arena.pCam.getPickRay(screenX, screenY);
		
		//bounds.mul(this.transform);
		calculateBoundingBox(bounds);
			if(Intersector.intersectRayBounds(pickRay, bounds.set(bounds.min.mul(this.transform), bounds.max.mul(this.transform)), boxIntersection)){
				Gdx.app.log(this.toString(), "Touched!");
				

				for (ArenaItem i:Arena.instances) {
					i.selected=false;
				}
				selected = true;
				return false;
			}
			
			selected = false;
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		calculateBoundingBox(bounds);
		selected = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Ray pickRay = Arena.pCam.getPickRay(screenX, screenY);
		if(this.selected){
			calculateBoundingBox(bounds);
			Intersector.intersectRayPlane(pickRay, xzPlane, planeIntersection);
			//multiply not set to
			
			this.transform.setToTranslation(planeIntersection.x,0 ,planeIntersection.z);
			if(this.wall){
				this.transform.rotate(0, 1, 0,rotation);
			}
			// need to move box
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

	
	
	//rotate object
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		if(!Stroke.grab){
			// If I am selected rotate me!
			Gdx.app.log("Pinch", this.toString());
			Vector2 a = initialPointer2.sub(initialPointer1);
	        Vector2 b = pointer2.sub(pointer1);
	        a = a.nor();
	        b = b.nor();
	        float deltaRot = (float)(Math.atan2(b.y,b.x) - Math.atan2(a.y,a.x));
	        float deltaRotDeg = (float)((deltaRot*180)/Math.PI);
	        this.transform.rotate(1, 0, 1, deltaRotDeg*Gdx.graphics.getDeltaTime());
		}
        return false;
	}


}
