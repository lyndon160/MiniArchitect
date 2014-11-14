package com.LyndonFawcett.MiniArchitect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class ArenaItem extends ModelInstance implements InputProcessor{
	boolean selected=false;
    public BoundingBox bounds = new BoundingBox();
    public Vector3 center = new Vector3();
    public Vector3 dimensions = new Vector3();
     
	
	public ArenaItem(Model model,InputMultiplexer multiplexer) {
		super(model);
		Gdx.app.log("Model", this.toString());
		multiplexer.addProcessor(this);
		calculateBoundingBox(bounds);
		center = bounds.getCenter();
        dimensions = bounds.getDimensions();
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
		//if it intersects with me move me!
		Ray pickRay = Arena.pCam.getPickRay(screenX, screenY);
		
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
		selected = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Ray pickRay = Arena.pCam.getPickRay(screenX, screenY);
		if(this.selected){
			Intersector.intersectRayPlane(pickRay, xzPlane, planeIntersection);

			this.transform.setToTranslation(planeIntersection.x,0 ,planeIntersection.z);
			
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


}
