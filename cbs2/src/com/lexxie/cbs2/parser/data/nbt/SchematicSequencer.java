package com.lexxie.cbs2.parser.data.nbt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.parser.coordinates.Coordinates;
import com.lexxie.cbs2.parser.coordinates.Tags;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.parser.data.nbt.Tag.Type;
import com.lexxie.cbs2.project.ProjectManager;

public class SchematicSequencer {

	private HashMap<Coordinates, BlockData> coordinateData = new HashMap<Coordinates, BlockData>();
	private Builder builder;
	
	//Schematic data
	Tag schematic;
	Tag entities;
	Tag tileEntities;
	Tag tileTicks;
	Tag height;
	Tag length;
	Tag materials;
	Tag width;
	Tag biomes;
	Tag blocks;
	Tag data;
	
	//Normal CommandBlock id (Blocks) : -119
	//Repeating commandBlock id (Blocks) : -46
	//chain CommandBlock id (Blocks) : -45
	
	public SchematicSequencer(){
		builder = new Builder();
		//Create empty Entities list
		entities = new Tag("Entities", Type.TAG_Compound);
				
		//Create empty TileEntities list
		tileEntities = new Tag("TileEntities", Type.TAG_Compound);
				
		//Create empty TileEntities list
		tileTicks = new Tag("TileTicks", Type.TAG_Compound);
				
		//Create Materials 'Alpha' String
		materials = new Tag(Type.TAG_String, "Materials", "Alpha");
		
		//set length
		length = new Tag(Type.TAG_Short, "Length",(short) builder.getMaxLength());
				
		//Create Schematic root 
		Tag schematicRootList = new Tag("schematicRootList", Type.TAG_Compound);
		schematicRootList.addTag(new Tag(Type.TAG_End, null, null));
				
		schematic = new Tag(Type.TAG_Compound, "Schematic", schematicRootList.getValue());
				
		
	}
	
	public boolean make(){
		
		convertInit();
		convertExe();
		
		//set max_width and max_height
		width = new Tag(Type.TAG_Short, "Width", (short) builder.getMaxWidth());
		height = new Tag(Type.TAG_Short, "Height", (short) builder.getMaxHeight());
		
		//add lists
		schematic.addTag(entities);
		schematic.addTag(tileEntities);
		schematic.addTag(tileTicks);
		
		//adds height, length, width and materials
		schematic.addTag(height);
		schematic.addTag(length);
		schematic.addTag(materials);
		schematic.addTag(width);
		
		schematic.addTag(createTagBlocks());
		
		//create data tag
		schematic.addTag(createTagData());
		
		try {
			
			File root = new File("Schematics");
			
			if(!root.exists())root.mkdir();
			
			File file = new File("Schematics\\" + ProjectManager.activeProject.getName() + ".schematic");
			
			OutputStream output = new FileOutputStream(file);
			
			schematic.writeTo(output);
			
			output.flush();
			output.close();
			
			JOptionPane.showMessageDialog(new JDialog(), "Successfully created '" + ProjectManager.activeProject.getName() + ".schematic'");
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JDialog(), "Couldn't create file: "  + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JDialog(), "Couldn't create file: "  + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
		
	}
	
	private Tag createTagData(){
		
		return builder.buildData(builder, coordinateData);
		
	}
	
	private Tag createTagBlocks(){
		
		return builder.buildBlock(builder, coordinateData);
		
	}
	
	private void convertExe(){
		
		builder.reset();
		builder.setZ(1);
		
		//iterate over every execution list
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		while(it.hasNext()){
			
			Entry pair = (Entry) it.next();
			
			Execute exe = (Execute) pair.getValue();
			
			//convert ExecuteCommand data to .nbt format and add is to TileEntities list
			for(BlockData data : exe.getCommands()){
				
				addTags(data);
				
				builder.doStepEntitie();
				builder.setMax();
			}
		
			builder.nextRow();
			
		}
		
	}
	
	private void convertInit(){
		
		for(BlockData data : Init.getCommands()){
			
			addTags(data);
			
			builder.doStepEntitie();
			builder.setMax();
			
		}
		
	}
	
	private void addTags(BlockData data){
		
		if(data.hasTag()){
			Tags.getTags().add(new Tags(data.getTag(), new Coordinates(builder.getX(), builder.getY(), builder.getZ())));
		}
		
		coordinateData.put(new Coordinates(builder.getX(), builder.getY(), builder.getZ()), data);
		
		//Create empty tag list for Compoundtag
		Tag tileDataList = new Tag("tileDatalist", Type.TAG_Compound);
		tileDataList.addTag(new Tag(Type.TAG_End, null, null));
		
		//Create CompoundTag
		Tag tileData = new Tag(Type.TAG_Compound, null, tileDataList.getValue());
		
		//if the block needs redstone
		Tag auto = new Tag(Type.TAG_Byte, "auto", getAuto(data));
		//the command
		Tag command = new Tag(Type.TAG_String, "Command", getCommand(data, builder.getX(), builder.getY(), builder.getZ()));
		//condition Met - default 0
		Tag conditionMet = new Tag(Type.TAG_Byte, "ConditionMet",(byte) 0);
		//customName '@' - change later
		Tag customName = new Tag(Type.TAG_String, "CustomName", data.getBlockName());
		//id = Control
		Tag id = new Tag(Type.TAG_String, "id", "Control");
		//powered - default 0
		Tag powered = new Tag(Type.TAG_Byte, "powered",(byte) 0);
		//successCount - default 0
		Tag successCount = new Tag(Type.TAG_Int, "SuccessCount", 0);
		//trackOutput - default 0
		Tag trackOutput = new Tag(Type.TAG_Byte, "TrackOutput",(byte) 0);
		//set position
		Tag xPos = new Tag(Type.TAG_Int, "x", builder.getX());
		Tag yPos = new Tag(Type.TAG_Int, "y", builder.getY());
		Tag zpos = new Tag(Type.TAG_Int, "z", builder.getZ());
		
		//add tags to CompoundTag
		tileData.addTag(auto);
		tileData.addTag(command);
		tileData.addTag(conditionMet);
		tileData.addTag(customName);
		tileData.addTag(id);
		tileData.addTag(powered);
		tileData.addTag(successCount);
		tileData.addTag(trackOutput);
		tileData.addTag(xPos);
		tileData.addTag(yPos);
		tileData.addTag(zpos);
		
		//add to TileEntities
		tileEntities.addTag(tileData);
		
	}
	
	/**
	 * 	return the command of data
	 * 	@param Blockdata
	 * 	@return command as String
	 * */
	private String getCommand(BlockData data){
		return data.getCommand().replaceFirst("#", "/");
	}
	
	/**
	 * 	return the command of the block
	 * 	@param Blockdata, x, y, z
	 * 	@return command as String 
	 * */
	private String getCommand(BlockData data, int x, int y,int z){
		
		String command = "";
		String tmp = "";
		
		Coordinates v = new Coordinates(x,y,z);
		
		command = data.getCommand();
		
		for(Tags tag : Tags.getTags()){
			
			command = tmp;
			command = data.getCommand().replaceAll("%" + tag.getName() + "%", Tags.subtract(tag.getCoordinates(), v).toString());
			tmp = command;
		}
		
		return command.replaceFirst("#", "/");
	}
	
	/**
	 * 	return if the block needs redstone
	 * 	@param BlockData
	 * 	@return byte 0/1
	 * */
	private byte getAuto(BlockData data){
		
		if(data.isRedstone() == true)
		{
			return 0;
		}else{
			return 1;
		}
		
	}

	/**
	 * 	returns if the Integer is even
	 * 	@param int
	 * 	@return boolean
	 * */
	private boolean isEven(int x){
		
		if((x & 1) == 0){
			//x is even
			return true;
		}else{
			//x is odd
			return false;
		}
		
	}
	
}
