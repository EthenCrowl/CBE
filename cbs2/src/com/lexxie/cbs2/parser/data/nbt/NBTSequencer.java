package com.lexxie.cbs2.parser.data.nbt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.parser.coordinates.Coordinates;
import com.lexxie.cbs2.parser.coordinates.Tags;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.BlockData.Stat;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.parser.data.commands.Variable;
import com.lexxie.cbs2.parser.data.nbt.Tag.Type;
import com.lexxie.cbs2.project.ProjectManager;

public class NBTSequencer {

	public static enum FACING {south, north, west, east, up, down};
	
	//Tags
	Tag nbtRoot, blocks, entities, palette, size, author, version;
	
	private List<Palette> palettes = new ArrayList<Palette>();
	
	private HashMap<Integer, List<Tag>> blockTags = new HashMap<Integer, List<Tag>>();
	
	int cordX, cordY, cordZ, max;
	
	public NBTSequencer(){

		blocks = new Tag("blocks", Type.TAG_Compound);
		
		entities = new Tag("entities", Type.TAG_Compound);
		
		palette = new Tag("palette", Type.TAG_Compound);
		
		max = 32;
		
		size = new Tag("size", Type.TAG_Int);
		
		size.addTag(new Tag(Type.TAG_Int, "", max));
		size.addTag(new Tag(Type.TAG_Int, "", max));
		size.addTag(new Tag(Type.TAG_Int, "", max));
		
		author = new Tag(Type.TAG_String, "author", "commandBlockEditor");
		version = new Tag(Type.TAG_Int, "version", 1);
		
		Tag nbtRootList = new Tag("nbtRootList", Type.TAG_Compound);
		nbtRootList.addTag(new Tag(Type.TAG_End, null, null));
		nbtRoot = new Tag(Type.TAG_Compound, ProjectManager.activeProject.getName(), nbtRootList.getValue());
		
		addAirPalette();
		
		cordX = 0;
		cordY = 0;
		cordZ = 0;
		
	}

	public boolean make(){
		
		convertData();
		
		nbtRoot.addTag(blocks);
		nbtRoot.addTag(entities);
		nbtRoot.addTag(palette);
		
		nbtRoot.addTag(size);
		nbtRoot.addTag(author);
		nbtRoot.addTag(version);
		
		try {
			

			
			if(ProjectManager.activeProject.getVariableHandler().getVariables().containsKey("CBE$NBTPATH")){
				
				File file = new File(ProjectManager.activeProject.getVariableHandler().getVariables().get("CBE$NBTPATH")
									+ "\\" + ProjectManager.activeProject.getName() +".nbt");
				
				OutputStream output = new FileOutputStream(file);
				
				nbtRoot.writeTo(output);
				
				output.flush();
				output.close();
				
				JOptionPane.showMessageDialog(new JDialog(), "Successfully created '" + ProjectManager.activeProject.getName() + ".nbt'");
				
			}else{
				
			File root = new File("NBT");
			
			if(!root.exists())root.mkdir();
			
			File file = new File("NBT\\" + ProjectManager.activeProject.getName() + ".nbt");
			
			OutputStream output = new FileOutputStream(file);
			
			nbtRoot.writeTo(output);
			
			output.flush();
			output.close();
			
			JOptionPane.showMessageDialog(new JDialog(), "Successfully created '" + ProjectManager.activeProject.getName() + ".nbt'");
			
		}
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JDialog(), "Couldn't create file: "  + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JDialog(), "Couldn't create file: "  + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private void calculateCoordiantes(){
		
		if(cordX < max - 1 && isEven(cordY)){
			cordX++;
		}else if(cordX > 0 && !isEven(cordY)){
			cordX--;
		}else if(cordX == max - 1 && cordY < max - 1){
			cordY++;
		}else if(cordX == max - 1 && cordY == max - 1){
			cordX = 0;
			cordY = 0;
			cordZ++;
		}else if(cordX == 0 && !isEven(cordY)){
			if(cordY < max - 1){
				cordY++;
			}else{
				cordY = 0;
				cordZ++;
			}
		}
		
	}
	
	private void addAirUp(){
		
		int count = 0;
		
		if(isEven(cordY)){
			count = cordY * max + cordX;
		}else{
			count = cordY * max + (max - 1 - cordX);
		}
		
		for(int i = count; i <= max*max - 1; i++){
			
			blocks.addTag(createAir());
			
			if(cordX < max - 1 && isEven(cordY)){
				cordX++;
			}else if(cordX > 0 && !isEven(cordY)){
				cordX--;
			}else if(cordX == max - 1 && cordY < max - 1){
				cordY++;
			}else if(cordX == 0 && !isEven(cordY)){
				if(cordY < max - 1){
					cordY++;
				}
			}	
		}
		
		calculateCoordiantes();
	}
	
	private void addAirTillEnd(){
		
		int count = (cordZ * max * max) + cordY * max + cordX;
		
		for(int i = count; i < max*max*max; i++){
			
			blocks.addTag(createAir());
			
			if(cordX < max - 1 && isEven(cordY)){
				cordX++;
			}else if(cordX > 0 && !isEven(cordY)){
				cordX--;
			}else if(cordX == max - 1 && cordY < max - 1){
				cordY++;
			}else if(cordX == 0 && !isEven(cordY)){
				if(cordY < max - 1){
					cordY++;
				}else{
					cordZ++;
					cordX = 0;
					cordY = 0;
				}
			}
			
		}
		
	}
	
	private void convertData(){
		
		//create Palette here
		for(BlockData data : Init.getCommands()){
			
			blocks.addTag(createBlockTag(data));
			
			calculateCoordiantes();
			
		}
		
		addAirUp();
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		while(it.hasNext()){
			
			Entry pair = (Entry) it.next();
			
			Execute exe = (Execute) pair.getValue();			
			for(BlockData data : exe.getCommands()){
					
				blocks.addTag(createBlockTag(data));
				
				calculateCoordiantes();
				
			}
			
			addAirUp();
			
		}
		
		addAirTillEnd();
		
		System.out.println("TagsSize: " + blockTags.size());
		
		//create blocks tag
		for(int i = 0; i < blockTags.size(); i++){
			for(int j = 0; j < blockTags.get(i).size(); j++){
				
				blocks.addTag(blockTags.get(i).get(j));
				
			}
		}
		
		//create palette
		for(int i = 0; i < palettes.size(); i++){
			
			Tag entryList = new Tag("entryList", Type.TAG_Compound);
			entryList.addTag(new Tag(Type.TAG_End, null, null));
			Tag entry = new Tag(Type.TAG_Compound, "", entryList.getValue());
			
			Tag propertiesList = new Tag("propertiesList", Type.TAG_Compound);
			propertiesList.addTag(new Tag(Type.TAG_End, null, null));
			Tag properties = new Tag(Type.TAG_Compound, "Properties", propertiesList.getValue());
			
			Tag conditional = new Tag(Type.TAG_String, "conditional", String.valueOf(palettes.get(i).isConditional()));
			Tag facing = new Tag(Type.TAG_String, "facing", palettes.get(i).getFacing().toString());
			
			properties.addTag(conditional);
			properties.addTag(facing);
			
			entry.addTag(properties);
			entry.addTag(new Tag(Type.TAG_String, "Name", palettes.get(i).getName()));
			
			palette.addTag(entry);
			
		}
		
	}
	
	private Tag createBlockTag(BlockData data){
		
		if(data.hasTag()){
			Tags.getTags().add(new Tags(data.getTag(), new Coordinates(cordX, cordY, cordZ)));
		}
		
		Tag blockList = new Tag("blockList", Type.TAG_Compound);
		blockList.addTag(new Tag(Type.TAG_End, null, null));
		Tag block = new Tag(Type.TAG_Compound, "", blockList.getValue());
		
		Tag nbtList = new Tag("nbtList", Type.TAG_Compound);
		nbtList.addTag(new Tag(Type.TAG_End, null, null));
		Tag nbt = new Tag(Type.TAG_Compound, "nbt", nbtList.getValue());
		
		//if the block needs redstone
		Tag auto = new Tag(Type.TAG_Byte, "auto", getAuto(data));
		//the command
		Tag command = new Tag(Type.TAG_String, "Command", getCommand(data, cordX, cordY, cordZ));
		//condition Met - default 0
		Tag conditionMet = new Tag(Type.TAG_Byte, "conditionMet",(byte) 0);
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
		
		if(!data.getStats().isEmpty()){
			
			Tag statList = new Tag("StatsList", Type.TAG_Compound);
			statList.addTag(new Tag(Type.TAG_End, null, null));
			Tag commandStats = new Tag(Type.TAG_Compound, "CommandStats", statList.getValue());
			
			for(Stat stat : data.getStats()){
				System.out.println("Type:" + stat.toString() + " selector:" + stat.getSelector() + " object:" + stat.getObject());
				
				if(stat == Stat.AE){
					Tag ae = new Tag(Type.TAG_String, "AffectedEntitiesName", stat.getSelector());
					Tag object = new Tag(Type.TAG_String, "AffectedEntitiesObjective", stat.getObject());
					commandStats.addTag(ae);
					commandStats.addTag(object);
				}else if(stat == Stat.AB){
					Tag ab = new Tag(Type.TAG_String, "AffectedBlocksName", stat.getSelector());
					Tag object = new Tag(Type.TAG_String, "AffectedBlocksObjective", stat.getObject());
					commandStats.addTag(ab);
					commandStats.addTag(object);
				}else if(stat == Stat.AI){
					Tag ai = new Tag(Type.TAG_String, "AffectedItemsName", stat.getSelector());
					Tag object = new Tag(Type.TAG_String, "AffectedItemsObjective", stat.getObject());
					commandStats.addTag(ai);
					commandStats.addTag(object);
				}else if(stat == Stat.SC){
					Tag sc = new Tag(Type.TAG_String, "SuccessCountName", stat.getSelector());
					Tag object = new Tag(Type.TAG_String, "SuccessCountObjective", stat.getObject());
					commandStats.addTag(sc);
					commandStats.addTag(object);
				}else if(stat == Stat.QR){
					Tag qr = new Tag(Type.TAG_String, "QueryResultName", stat.getSelector());
					Tag object = new Tag(Type.TAG_String, "QueryResultObjective", stat.getObject());
					commandStats.addTag(qr);
					commandStats.addTag(object);
				}
				
			}
			
			nbt.addTag(commandStats);
		}
		
		nbt.addTag(auto);
		nbt.addTag(command);
		nbt.addTag(conditionMet);
		nbt.addTag(customName);
		nbt.addTag(id);
		nbt.addTag(powered);
		nbt.addTag(successCount);
		nbt.addTag(trackOutput);
		
		Tag pos = new Tag("pos", Type.TAG_Int);
		pos.addTag(new Tag(Type.TAG_Int, "", cordZ));
		pos.addTag(new Tag(Type.TAG_Int, "", cordY));
		pos.addTag(new Tag(Type.TAG_Int, "", cordX));
		
		Tag state = new Tag(Type.TAG_Int, "state", addToPalette(data));
		
		block.addTag(nbt);
		block.addTag(pos);
		block.addTag(state);
		
		return block;
	}
	
	private int addToPalette(BlockData data){
		
		for(int i = 0; i < palettes.size(); i++){
			
			if(palettes.get(i).isTypeEqualsName(data.getType())){
				if(palettes.get(i).isConditional() == data.isConditional()){
					
					if(cordX < 31 && isEven(cordY)){
						if(palettes.get(i).facing == FACING.south){
							return i+1;
						}
					}else if(cordX > 0 && !isEven(cordY)){
						if(palettes.get(i).facing == FACING.north){
							return i+1;
						}
					}else if((cordX == 31 && isEven(cordY)) || (cordX == 0 && !isEven(cordY))){
						if(palettes.get(i).facing == FACING.up){
							return i+1;
						}
					}
					
				}
			}
			
		}
		
		addNewPallet(data);
		
		return addToPalette(data);
		
	}
	
	private void addNewPallet(BlockData data){
		
		FACING facing = null;
		String name = "";
		
		if(cordX < 31 && isEven(cordY)){
				facing = FACING.south;
		}else if(cordX > 0 && !isEven(cordY)){
				facing = FACING.north;
		}else if((cordX == 31 && isEven(cordY)) || (cordX == 0 && !isEven(cordY))){
				facing = FACING.up;
		}
		
		if(data.getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.NORMAL){
			name = "minecraft:command_block";
		}else if(data.getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.CHAIN){
			name = "minecraft:chain_command_block";
		}else if(data.getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.REPEAT){
			name = "minecraft:repeating_command_block";
		}
		
		palettes.add(new Palette(name, data.isConditional(), facing));
		
	}
	
	private void addAirPalette(){
		
		Tag entryList = new Tag("entryList", Type.TAG_Compound);
		entryList.addTag(new Tag(Type.TAG_End, null, null));
		Tag entry = new Tag(Type.TAG_Compound, "", entryList.getValue());
		
		entry.addTag(new Tag(Type.TAG_String, "Name", "minecraft:air"));
		
		palette.addTag(entry);
		
	}
	
	private Tag createAir(){
		
		Tag blockList = new Tag("blockList", Type.TAG_Compound);
		blockList.addTag(new Tag(Type.TAG_End, null, null));
		Tag block = new Tag(Type.TAG_Compound, "", blockList.getValue());
		
		Tag pos = new Tag("pos", Type.TAG_Int);
		pos.addTag(new Tag(Type.TAG_Int, "", cordZ));
		pos.addTag(new Tag(Type.TAG_Int, "", cordY));
		pos.addTag(new Tag(Type.TAG_Int, "", cordX));
		
		Tag state = new Tag(Type.TAG_Int, "state", 0);
		
		block.addTag(pos);
		block.addTag(state);
		
		return block;
	}
	
	private boolean isEven(int x){
		if((x & 1) == 0){
			//x is even
			return true;
		}else{
			//x is odd
			return false;
		}	
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
			command = data.getCommand().replaceAll("%" + tag.getName() + "%", Tags.subtract(tag.getCoordinates(), v).toString(true));
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
	
	private class Palette{
		
		private boolean conditional;
		private FACING facing;
		String name;
		
		public Palette(String name, boolean conditional, FACING facing){
			this.name = name;
			this.conditional = conditional;
			this.facing = facing;
		}
		
		public boolean isConditional(){
			return conditional;
		}
		
		public String getName(){
			return name;
		}
		
		public FACING getFacing(){
			return facing;
		}
		
		public boolean isTypeEqualsName(com.lexxie.cbs2.parser.data.commands.BlockData.Type type){
			
			if(type == com.lexxie.cbs2.parser.data.commands.BlockData.Type.NORMAL && name == "minecraft:command_block"){
				return true;
			}else if(type == com.lexxie.cbs2.parser.data.commands.BlockData.Type.CHAIN && name == "minecraft:chain_command_block"){
				return true;
			}else if(type == com.lexxie.cbs2.parser.data.commands.BlockData.Type.REPEAT && name == "minecraft:repeating_command_block"){
				return true;
			}
			
			return false;
		}
		
	}
	
}
