package com.LyndonFawcett.MiniArchitect;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * 3D implementation and view of room
 * 
 * 
 * @author Lyndon
 *
 */
public class Arena extends Stroke implements ApplicationListener{
	public CameraInputController camController, camController2;
	public ArenaControls control;
	static public PerspectiveCamera pCam;
	static public OrthographicCamera oCam;
	public Model model;
	public ModelInstance instance;
	public ModelBatch modelBatch;
	public Environment environment;
	public AssetManager assets;
	static public Array<ArenaItem> instances = new Array<ArenaItem>();
	public boolean loading;
	@Override
	public void create() { 
		//Initialise super class (Too complex for super call)
		createStroke();

		//lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();
		pCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		pCam.position.set(0f, 5f, 0f);
		pCam.lookAt(0,0,0);
		pCam.near = 1f;
		pCam.far = 100f;
		pCam.update();




		assets = new AssetManager();
		if (Gdx.app.getType() == ApplicationType.Android){}
		else{
			FileHandle[] files = Gdx.files.internal("../android/assets/furniture").list();
			for(FileHandle file: files) {
				assets.load("furniture/"+file.name(), Model.class);
			}
		}


		//Viewport set to fix perspective to orthographic
		oCam = new OrthographicCamera(Gdx.graphics.getWidth()/139, Gdx.graphics.getHeight()/135);
		oCam.position.set(0f, 5f, 0f);
		oCam.lookAt(0,0,0);
		oCam.near = 1f;
		oCam.far = 100f;
		oCam.update();

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f, 
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.setToTranslation(0, 0, 0);

		control = new ArenaControls(pCam, oCam);

		// camController = new CameraInputController(oCam);
		// camController2 = new CameraInputController(pCam);

		multiplexer.addProcessor(new GestureDetector(control));
		// multiplexer.addProcessor(camController2);
		// multiplexer.addProcessor(camController);
		loading = true;
	}


	private void doneLoading() {
		loading = false;
	}
	
	@Override
	public void addItem(String item){
		instances.add(new ArenaItem(assets.get("furniture/"+item, Model.class), multiplexer));
	}



	@Override
	void arenaRender() {
		if (loading && assets.update())
			doneLoading();

		if(pCamOn)
			modelBatch.begin(pCam);
		else
			modelBatch.begin(oCam);

		modelBatch.render(instances, environment);
		modelBatch.end();

		oCam.update();
		pCam.update();

	}



}
