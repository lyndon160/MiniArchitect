package com.LyndonFawcett.MiniArchitect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class WallListener implements InputProcessor {
	InputMultiplexer multiplexer;
	boolean finished = false;
		   public WallListener(InputMultiplexer multiplexer) {
			   super();
				this.multiplexer = multiplexer;
	}

		@Override
		   public boolean keyDown (int keycode) {
		      return false;
		   }

		   @Override
		   public boolean keyUp (int keycode) {
		      return false;
		   }

		   @Override
		   public boolean keyTyped (char character) {
		      return false;
		   }
		   Ray down = null;
		   float downZ, downX;
		   final Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);
		   Vector3 downIntersection = new Vector3();
		   @Override
		   public boolean touchDown (int x, int y, int pointer, int button) {
			   if(!finished){
				   ArenaItem.stopTouch();
				   Stroke.paint = true;
			  down = Arena.pCam.getPickRay(x, y);
			  Intersector.intersectRayPlane(down, xzPlane, downIntersection);
			  downZ = down.origin.z;
			  downX = down.origin.x;
			//  Gdx.app.log("down z location",down.origin.z+"");
		//	  Gdx.app.log("Wall down",down.origin+"");
			   }
			  return false;
		   }

		   Ray up = null;
		   Vector3 tmpUp;
		   double rotation;
		   Vector3 upIntersection = new Vector3();
		   @Override
		   public boolean touchUp (int x, int y, int pointer, int button) {
			   if(!finished){
				   ArenaItem.resumeTouch();
			   up = Arena.pCam.getPickRay(x, y);
			   //from this build a model between two points
		//	   tmpUp = camera.up.origin;
			   Intersector.intersectRayPlane(up, xzPlane, upIntersection);
			   
			//  float xLength=up.origin.x - downX;
			//  float zLength=up.origin.z - downZ;
			   
				float xLength=upIntersection.x - downIntersection.x;
			    float zLength=upIntersection.z - downIntersection.z;
			   
			   double length = downIntersection.dst(upIntersection);
			   
					   //double length = Math.sqrt(Math.pow(xLength,2f)+Math.pow(zLength,2f));
			   
			//   Gdx.app.log("Wall up",up.origin.toString());
			//   Gdx.app.log("down z location",downZ+"");
			 //  Gdx.app.log("up z location",up.origin.z+"");		   
			   //Remove listener at end
			  // multiplexer.removeProcessor(this);
			   Gdx.app.log("Length of wall",length+"");
			   						//Might need the positive value of this
			   rotation = Math.toDegrees(Math.atan2(xLength, zLength));//calculate angle to rotate wall
			   
			   Gdx.app.log("Rotation",rotation+"");		
			   
			   

			   Arena.instances.add(new ArenaItem(multiplexer,length, rotation,downIntersection.lerp(upIntersection, 0.5f)));
			   finished = true;
			   Stroke.paint=false;
			   Stroke.wipeDrawings();
			   }
		      return false;
		   }

		   @Override
		   public boolean touchDragged (int x, int y, int pointer) {
		      return false;
		   }

		   @Override
		   public boolean mouseMoved (int x, int y) {
		      return false;
		   }

		   @Override
		   public boolean scrolled (int amount) {
		      return false;
		   }
}
