package com.LyndonFawcett.MiniArchitect.utils;

import java.util.ArrayList;

import com.LyndonFawcett.MiniArchitect.Arena;
import com.LyndonFawcett.MiniArchitect.ArenaItem;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
/**
 * 
 * Conversion class to save space when uploading models
 * Coverts ArenaItem class
 * 
 * @author Lyndon
 *
 */
public class MinimalItem {
	Matrix4 transform;
	public String modelName;
	
	public MinimalItem(){
	}
	public MinimalItem(ArenaItem i){
		this.transform = i.transform;
		this.modelName = i.modelName;
	}
	
	public ArenaItem convertToArenaItem(InputMultiplexer multiplexer){
		if(Arena.assets == null)
			Arena.loadAssets();

		while(!Arena.assets.update()){
			//TODO loading bar with assets.getProgress();
			
		}//Spinz until assets are loaded
		System.out.println("Converting "+modelName);
	//	if(Arena.assets.isLoaded("downloaded/models/"+modelName, Model.class)){
		ArenaItem i = new ArenaItem(Arena.assets.get("downloaded/models/"+modelName, Model.class),modelName, multiplexer);
		i.transform.set(this.transform);
		return i;
		

	}
	
}
