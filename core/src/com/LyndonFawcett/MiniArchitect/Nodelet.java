package com.LyndonFawcett.MiniArchitect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Nodelet {
	float x,z,roty,rotw;
	double angle;
	double radius;
	
	
	float oldx,oldz,oldroty,oldrotw;
	double oldangle;
	double oldradius;
	
	
	
	String connector;
	public Nodelet(float x, float z,float roty,float rotw, String connector) {
		this.x=x;
		this.z=z;
		this.roty=roty;
		this.rotw=rotw;
		this.connector=connector;
		this.angle=Math.toDegrees(Math.atan2(z, x));
		this.oldangle = angle;
		this.radius=Math.sqrt(x*x+z*z);
	}
	
	public void reset(){
		this.x=oldx;
		this.z=oldz;
		this.roty=oldroty;
		this.rotw=oldrotw;
		this.angle=oldangle;
		this.radius=oldradius;
	}

	public void updateAngle(double a){
		System.out.println("x :"+x+"z :"+z);
		angle = angle + a;
		x =(float) (radius*Math.cos(Math.toRadians(angle)));
		z =(float) (radius*Math.sin(Math.toRadians(angle)));
		System.out.println("x :"+x+"z :"+z +"  UPDATED");
		
	//	Quaternion q = new Quaternion(0,roty,0,rotw);
		//roty+=a;
		//q.transform(new Vector3()).rotate(new Vector3(0,1,0),(float) a);
	//	rotw=q.w;
	//	roty=q.y;

	}
		ModelInstance wire;
	public void debug(Vector3 v){
		if(Arena.debug.contains(wire))
			Arena.debug.remove(wire);
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model = modelBuilder.createBox(0.1f,0.1f,0.1f, GL20.GL_LINES,
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal);
		
		wire = new ModelInstance(model);
		wire.transform.setToTranslation(new Vector3(v.x+x,0,v.z+z));
		
		Arena.debug.add(wire);
	}
}
