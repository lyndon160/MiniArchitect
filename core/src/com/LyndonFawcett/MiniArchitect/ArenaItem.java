package com.LyndonFawcett.MiniArchitect;

import java.util.ArrayList;
import java.util.HashMap;

import com.LyndonFawcett.MiniArchitect.utils.Notification;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
	public String modelName;
	public ArrayList<ArenaItem> group = new ArrayList<ArenaItem>();
	public ArrayList<Nodelet> nodes= new ArrayList<Nodelet>();
	private Skin skin;
	public int value=0;

	//Build normal object
	public ArenaItem(Model model,String name,InputMultiplexer multiplexer) {
		super(model);
		modelName=name;
		Gdx.app.log("Creating arena model", name);
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));
		calculateBoundingBox(bounds);
		center = bounds.getCenter();
		dimensions = bounds.getDimensions();
		for (ArenaItem i:Arena.instances) {
			i.draggable=false;
			i.selected=false;
			Arena.wireFrames.remove(i.wire);
			i.wire=null;

		}
		draggable = true;
		createWire();
		selected= true;
		skin =new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));
		if(Arena.nodes == null)
			Arena.nodes=new HashMap<String, ArrayList<Nodelet>>();
		if(Arena.nodes.containsKey(modelName)){
			//System.out.println("Nodes");
			for(Nodelet n:Arena.nodes.get(modelName)){
				//System.out.println(n.x+ "   "+n.z);
				nodes.add(new Nodelet(n.x, n.z,n.roty,n.rotw, n.connector));
			}
			//nodes = new ArrayList<Nodelet>(Arena.nodes.get(modelName));
		}
		if(name.contains("QQQ"))
			value =  Integer.parseInt(name.replace("g3db", "").replaceAll("\\D+",""));

	}

	//Constructor building a wall
	//Magnet listener - when a wall gets close to another wall it clips
	//Going to need a start point and end point for placement and dimensions of the wall
	public ArenaItem(InputMultiplexer multiplexer,double length, double rotation, Vector3 placement) {
		super(new ModelBuilder().createBox((float)length, 2f, 0.4f, 
				new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
				Usage.Position | Usage.Normal));
		this.rotation=(float) rotation-90;
		// Arena.pCam.unproject(placement);
		placement.y=0f;
		this.transform.setToTranslation(placement);

		skin =new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));

		this.transform.rotate(0, 1, 0, (float) rotation-90);
		Gdx.app.log("Model", this.toString());
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));

		bounds.set(new Vector3(1f,1f,1f), new Vector3(0,0,0));

		center = bounds.getCenter();
		dimensions = bounds.getDimensions();

		wall = true;
		modelName="wall";
		for (ArenaItem i:Arena.instances) {
			i.draggable=false;
			i.selected=false;
			Arena.wireFrames.remove(i.wire);
			i.wire=null;
		}
		draggable = true;
		createWire();
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
	ModelInstance wire;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(delete)
			return false;
		//Gdx.app.log(this.toString(), bounds.getDimensions()+"");

		//if it intersects with me move me!
		Ray pickRay = Arena.pCam.getPickRay(screenX, screenY);

		//bounds.mul(this.transform);
		//calculateBoundingBox(bounds);
		//	if(Intersector.intersectRayBounds(pickRay, bounds.set(bounds.min.mul(this.transform), bounds.max.mul(this.transform)), boxIntersection)){


		if(!Stroke.paint && !Stroke.grab && Intersector.intersectRayBoundsFast(pickRay,transform.getTranslation(new Vector3()),dimensions)){

			Gdx.app.log(this.modelName + "  " + this.toString(), "Selected!");


			for (ArenaItem i:Arena.instances) {
				//if other objects are not in my group
				i.draggable=false;
				i.selected=false;
				if(!group.contains(i)){

					Arena.wireFrames.remove(i.wire);
					i.wire=null;
				}
				//Select group members
				else{
					i.createWire();
				}
			}

			createWire();

			wire.transform.set(this.transform.getTranslation(new Vector3()),this.transform.getRotation(new Quaternion()));


			selected = true;
			draggable = true;
			return true;
		}

		draggable = false;

		return false;
	}

	public void createWire(){
		if(wire ==null ){
			ModelBuilder modelBuilder = new ModelBuilder();
			Model model = modelBuilder.createBox(1f,1f,1f, GL20.GL_LINES,
					new Material(ColorAttribute.createDiffuse(Color.GREEN)),
					Usage.Position | Usage.Normal);

			wire = new ModelInstance(model);

			wire.transform.set(this.transform.getTranslation(new Vector3()),this.transform.getRotation(new Quaternion()));
			Arena.wireFrames.add(wire);
		}

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//set wall to proper height

		//calculateBoundingBox(bounds);
		//if(this.selected)
		//bounds.mul(this.transform);

		//Gdx.app.log(this.toString(), this.selected+"");
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

		if(!Stroke.paint && this.draggable&&move&&!Arena.grab && !delete){
			//System.out.println("Dragging :"+modelName);
			//multiply not set to
			if(this.nodes.size()==0)
				for(ArenaItem i: group){
					//if someone else in group with nodes then i cant be moved
					if(i.nodes.size()>0){//AND i am connected to something then i am child and cannot be moved

						//check if i am in one of these nodes
						for(Nodelet n :i.nodes){
						//	System.out.println("Angle "+n.angle+" Radius "+n.radius);
						//	System.out.println(n.connector + " " + this.modelName);
							Vector3 tmpTransthis = this.transform.getTranslation(new Vector3());
							if(this.modelName.contains(n.connector)){
								Vector3 tmpTransN = i.transform.getTranslation(new Vector3());
								if(tmpTransthis.x < (n.x +tmpTransN.x) +1 &&
										tmpTransthis.x > (n.x +tmpTransN.x) -1 &&
										tmpTransthis.z < (n.z +tmpTransN.z) +1 &&
										tmpTransthis.z > (n.z +tmpTransN.z) -1 
										){
									return false;
								}
							}
						}


					}
				}
			//	calculateBoundingBox(bounds);
			Intersector.intersectRayPlane(pickRay, xzPlane, planeIntersection);


			//compare current x and z against new x and z
			float xDiff = this.transform.getTranslation(new Vector3()).x - planeIntersection.x;
			float zDiff = this.transform.getTranslation(new Vector3()).z - planeIntersection.z;


			//save rotation and move model to finger pos
			Quaternion rot = this.transform.getRotation(new Quaternion());
			this.transform.setTranslation(planeIntersection.x,0 ,planeIntersection.z);
			//System.out.println("Rotation :" + rot);
			//System.out.println("Location :" + this.planeIntersection);
			//Move items in group
			for(ArenaItem i: group){
				i.transform.setTranslation(i.transform.getTranslation(new Vector3()).x - xDiff,0,i.transform.getTranslation(new Vector3()).z - zDiff);
				i.wire.transform.setTranslation(i.transform.getTranslation(new Vector3()));

				for(Nodelet n:i.nodes)
					n.debug(i.transform.getTranslation(new Vector3()));

			}

			Vector3 tmpTransthis =transform.getTranslation(new Vector3());
			//Scan other arena items and check their nodes
			for(ArenaItem i: Arena.instances){
				if(i.equals(this))
					continue;
				for(Nodelet n :i.nodes){
				//	System.out.println("Angle "+n.angle+" Radius "+n.radius);
				//	System.out.println(n.connector + " " + this.modelName);
					if(this.modelName.contains(n.connector)){
						Vector3 tmpTransN = i.transform.getTranslation(new Vector3());
						if(tmpTransthis.x < (n.x +tmpTransN.x) +1 &&
								tmpTransthis.x > (n.x +tmpTransN.x) -1 &&
								tmpTransthis.z < (n.z +tmpTransN.z) +1 &&
								tmpTransthis.z > (n.z +tmpTransN.z) -1 
								){
							this.transform.set(n.x +tmpTransN.x,0,n.z +tmpTransN.z,0,n.roty,0,n.rotw);
							if(n.angle != n.oldangle){
								this.transform.rotate(0f,1f,0f,-((float)n.angle- (float)n.oldangle));
							}
							break;


						}
					}
				}
			}

			//DELETE ITEM
			//Check and see if model in delete box
			if(screenY < Stroke.deleteBox.getHeight()&& screenX < Stroke.deleteBox.getWidth()){
				System.out.println("Delete me " + screenY);
				delete=true;

				
				//reduce total price
				Stroke.runningCost.setText("£"+(Integer.parseInt(Stroke.runningCost.getText().toString().replace("£",""))-value));
				
				//delete group members
				for(ArenaItem i :group){
					Stroke.runningCost.setText("£"+(Integer.parseInt(Stroke.runningCost.getText().toString().replace("£",""))-i.value));
					
					Arena.instances.removeValue(i, false);
					Arena.wireFrames.remove(i.wire);
				}

				for(Nodelet n : nodes)
					if(Arena.debug.contains(n.wire))
						Arena.debug.remove(n.wire);
				nodes.clear();

				Arena.instances.removeValue(this, false);
				Arena.wireFrames.remove(wire);
				new Notification(Stroke.stage,skin,new Label("ITEM DELETED",skin,"24"));
				try{
					//	if(Stroke.multiplexer.getProcessors().contains(this, false))
					//		Stroke.multiplexer.getProcessors().removeValue(this, false);
				}
				catch(Exception e){System.out.print(e);}
			}

			wire.transform.set(this.transform.getTranslation(new Vector3()),this.transform.getRotation(new Quaternion()));
			//move node bits around
			for(Nodelet n:nodes)
				n.debug(this.transform.getTranslation(new Vector3()));
		}

		//set wall to proper height
		if(wall){
			Vector3 t =this.transform.getTranslation(new Vector3());
			this.transform.setTranslation(t.x,0.5f,t.z);
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
		if(!Stroke.paint && this.draggable&&move&&!Arena.grab && !delete){
			this.transform.rotate(0, 1, 0, amount*10);
			updateNodes(-(amount*10));
			updateGroup(-(amount*10));
			if(wire !=null)
				wire.transform.rotate(0, 1, 0, amount*10);

			return true;
		}
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

			//	Gdx.app.log("Pinch a", lastA.toString() + newA.toString());
		//		Gdx.app.log("Pinch b", lastB.toString() + newB.toString());

				//Need to check if new and old have changed
				if(lastA.y == newA.y && lastB.y == newB.y){
					Gdx.app.log("Pinch", "Same so returning");
					return false;
				}

				//check direction by comparing X
				if(oldA.x > oldB.x){
					if(lastA.y <= newA.y && lastB.y >= newB.y){
						Gdx.app.log("Pinch", "Rotate negative 1");
						this.transform.rotate(0, 1, 0, -100*Gdx.graphics.getDeltaTime());
						wire.transform.rotate(0, 1, 0, -100*Gdx.graphics.getDeltaTime());
						updateNodes((100*Gdx.graphics.getDeltaTime()));
						updateGroup((100*Gdx.graphics.getDeltaTime()));

					}
					else if(lastA.y >= newA.y && lastB.y <= newB.y){
						Gdx.app.log("Pinch", "Rotate positive 2");
						this.transform.rotate(0, 1, 0, 100*Gdx.graphics.getDeltaTime());
						wire.transform.rotate(0, 1, 0, 100*Gdx.graphics.getDeltaTime());
						updateNodes(-(100*Gdx.graphics.getDeltaTime()));
						updateGroup(-(100*Gdx.graphics.getDeltaTime()));

					}
				}
				else{
					if(lastA.y <= newA.y && lastB.y >= newB.y){
						Gdx.app.log("Pinch", "Rotate negative 3");
						this.transform.rotate(0, 1, 0, 100*Gdx.graphics.getDeltaTime());
						wire.transform.rotate(0, 1, 0, 100*Gdx.graphics.getDeltaTime());
						updateNodes(-(100*Gdx.graphics.getDeltaTime()));
						updateGroup(-(100*Gdx.graphics.getDeltaTime()));

					}
					else if(lastA.y >= newA.y && lastB.y <= newB.y){
						Gdx.app.log("Pinch", "Rotate positive 4");
						this.transform.rotate(0, 1, 0, -100*Gdx.graphics.getDeltaTime());
						wire.transform.rotate(0, 1, 0, -100*Gdx.graphics.getDeltaTime());
						updateNodes((100*Gdx.graphics.getDeltaTime()));
						updateGroup((100*Gdx.graphics.getDeltaTime()));

					}
				}

			}

			lastA = newA.cpy();
			lastB = newB.cpy();

		}		
		return false;
	}
	//Rotates group members
	public void updateGroup(float a){
		//System.out.println(this.nodes.size());
		for(ArenaItem i: group){
			if(i.equals(this))
				continue;
			//calculate current rotation and radius
			
			Vector3 tempivec = i.transform.getTranslation(new Vector3());
			Vector3 tempthisvec = this.transform.getTranslation(new Vector3());
			float z=tempivec.z - tempthisvec.z;
			float x=tempivec.x - tempthisvec.x;
			double angle=Math.toDegrees(Math.atan2(z,x));//updated angle;

			angle+=a;
			double radius=Math.sqrt(x*x+z*z);
			//project rotation on current vector
			x =(float) (radius*Math.cos(Math.toRadians(angle)));
			z =(float) (radius*Math.sin(Math.toRadians(angle)));
			//set to new vector#
			//

			//i.transform.set(new Vector3(x+tempthisvec.x,0,z+tempthisvec.z),tmpq);
			//i.wire.transform.set(new Vector3(x+tempthisvec.x,0,z+tempthisvec.z),tmpq);
			i.transform.setTranslation(new Vector3(x+tempthisvec.x,0,z+tempthisvec.z));
			i.wire.transform.setTranslation(new Vector3(x+tempthisvec.x,0,z+tempthisvec.z));
			i.updateNodes(a);

			//orient object
			i.transform.rotate(0,1,0,(float) -a);
			i.wire.transform.rotate(0,1,0,(float) -a);
		}
	}


	public void updateNodes(double a){
		for(Nodelet n :this.nodes){
			n.updateAngle(a);
			n.debug(this.transform.getTranslation(new Vector3()));
			//	this.wire.transform.rotate(0f,1f,0f,)
		}
	}


}
