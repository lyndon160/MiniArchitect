package com.LyndonFawcett.MiniArchitect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ArenaControls implements GestureListener{

	PerspectiveCamera pCam;
	OrthographicCamera oCam;

	ArenaControls(PerspectiveCamera pCam, OrthographicCamera oCam){
		this.pCam=pCam;
		this.oCam=oCam;

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
		if(Stroke.grab){
			//pCam.translate(-deltaX/160, 0, -deltaY/160);
			//oCam.translate(-deltaX/160, 0, -deltaY/160);
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	public float initialScale = 1.0f;
	float oldZoom=0;
	@Override
	public boolean zoom(float initialDistance, float distance) {
		//	Gdx.app.log(initialDistance+"", distance+"");
			//Calculate pinch to zoom
		/*	float ratio = initialDistance / distance;
			float zoom = MathUtils.clamp(initialScale * ratio, 0.1f, 1.0f);
			float dist;
			if(initialDistance>distance)
				dist=0.1f;
			else if(initialDistance == distance)
				dist=0f;
			else
				dist=-0.1f;
			//Clamp range and set zoom
			
				oCam.translate(0, dist, 0);

				pCam.translate(0, dist, 0);
	
		
			if(oCam.position.y > 16){
				oCam.translate(0, -0.1f, 0);
				oCam.update();
				pCam.translate(0,  -0.1f, 0);
				pCam.update();
			}
			if(oCam.position.y < 3){
				oCam.translate(0, 0.1f, 0);
				oCam.update();
				pCam.translate(0,  0.1f, 0);
				pCam.update();
			}
			oCam.update();
			pCam.update();
			//Gdx.app.log(initialDistance+"", distance+"");
			oldZoom=zoom;
*/
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
		 Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

}
