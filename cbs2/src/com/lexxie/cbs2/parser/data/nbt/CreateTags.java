package com.lexxie.cbs2.parser.data.nbt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.lexxie.cbs2.parser.coordinates.Coordinates;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.parser.data.nbt.Tag.Type;

public class CreateTags {

	public static Tag createTagBlocksSide(Builder builder, HashMap<Coordinates, BlockData> coordinateData){
		
	builder.reset();
		
	byte[] byteArray = new byte[builder.getMaxHeight() * builder.getMaxLength() * builder.getMaxWidth()];
		
	for(int i = 0; i < byteArray.length; i++){
			
			//get coordinate data
			Coordinates cord = new Coordinates(builder.getX(), builder.getY(), builder.getZ());
			
			if(coordinateData.get(cord) != null){
				
				BlockData data = coordinateData.get(cord);
				
				//getType
				if(coordinateData.get(cord).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.NORMAL){
					byteArray[i] = (byte) -119;
				}else if(coordinateData.get(cord).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.CHAIN){
					byteArray[i] = (byte) -45;
				}else if(coordinateData.get(cord).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.REPEAT){
					byteArray[i] = (byte) -46;
				}	
			}else{
				//set air
				byteArray[i] = (byte) 0;
			}
			
			builder.doStepData();
			
		}
		
		Tag blocks = new Tag(Type.TAG_Byte_Array, "Blocks", byteArray);
		
		return blocks;
		
	}
	
	public static Tag createTagBlockUp(Builder builder){
		
		//create byte Array for every execution line + init line
		Byte[][][] byteArray = new Byte[builder.getMaxLength()][builder.getMaxHeight()][builder.getMaxWidth()];
		
		//fill all byte arrays
		
		int length = 0;
		int width = 0;
		
		for(int i = 0; i < Init.getCommands().size(); i++){
			
			if(Init.getCommands().get(i).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.NORMAL){
				byteArray[length][i][width] = (byte) -119;
			}else if(Init.getCommands().get(i).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.CHAIN){
				byteArray[length][i][width] = (byte) -45;
			}else if(Init.getCommands().get(i).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.REPEAT){
				byteArray[length][i][width] = (byte) -46;
			}	
			
		}
		
		length++;
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		
		while(it.hasNext()){
			
			Entry pair = (Entry) it.next();
			
			Execute exe = (Execute) pair.getValue();
			
			for(int i = 0; i < exe.getCommands().size(); i++){
				
				if(exe.getCommands().get(i).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.NORMAL){
					byteArray[length][i][width] = (byte) -119;
				}else if(exe.getCommands().get(i).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.CHAIN){
					byteArray[length][i][width] = (byte) -45;
				}else if(exe.getCommands().get(i).getType() == com.lexxie.cbs2.parser.data.commands.BlockData.Type.REPEAT){
					byteArray[length][i][width] = (byte) -46;
				}	
			}
			length++;
			if(length == 16){
				length = 0;
				width++;
			}
		}
		
		//create the block array
		byte[] blockArray = new byte[builder.getMaxHeight() * builder.getMaxLength() * builder.getMaxWidth()];
	
		int blockCount = 0;
		
		for(int bWidth = 0; bWidth < builder.getMaxWidth(); bWidth++){
			for(int bHeight = 0; bHeight < builder.getMaxHeight(); bHeight++){
				for(int bLength = 0; bLength < builder.getMaxLength(); bLength++){
					
					if(byteArray[bLength][bHeight][bWidth] != null){
						blockArray[blockCount] = byteArray[bLength][bHeight][bWidth];
					}else{
						blockArray[blockCount] = (byte) 0;
					}
					
					blockCount++;
					
				}
			}
		}
		
		Tag blocks = new Tag(Type.TAG_Byte_Array, "Blocks", blockArray);
		
		return blocks;
		
	}
	
	public static  Tag createTagDataSide(Builder builder, HashMap<Coordinates, BlockData> coordinateData){
		
		builder.reset();
		
		byte[] byteArray = new byte[builder.getMaxHeight() * builder.getMaxLength() * builder.getMaxWidth()];
		
		for(int i = 0; i < byteArray.length; i++){
			
			Coordinates cord = new Coordinates(builder.getX(), builder.getY(), builder.getZ());
			
			if(coordinateData.get(cord) != null){
				//foundBlock - get BlockData
				BlockData data = coordinateData.get(cord);
				
				byteArray[i] = builder.getByteOnPos(data);
				
			}else{
				//no block => air
				byteArray[i] = (byte) 0;
			}
			
			builder.doStepData();
			
		}   
		
		Tag data = new Tag(Type.TAG_Byte_Array, "Data", byteArray);
		
		return data;
		
	}
	
	public static Tag createTagDataUp(Builder builder){
		
		//create byte Array for every execution line + init line
		Byte[][][] byteArray = new Byte[builder.getMaxLength()][builder.getMaxHeight()][builder.getMaxWidth()];
				
		int length = 0;
		int width = 0;
		
		//fill all byte arrays
		for(int i = 0; i < Init.getCommands().size(); i++){
					
				if(Init.getCommands().get(i).isConditional()){
					byteArray[length][i][width] = (byte) 9;
				}else{
					byteArray[length][i][width] = (byte) 1;
				}
			}
				
			length++;
				
		
			Iterator it = Execute.getMainLoops().entrySet().iterator();
			
			while(it.hasNext()){
					
				Entry pair = (Entry) it.next();
					
				Execute exe = (Execute) pair.getValue();
					
				for(int i = 0; i < exe.getCommands().size(); i++){
					
					if(exe.getCommands().get(i).isConditional()){
						byteArray[length][i][width] = (byte) 9;
					}else{
						byteArray[length][i][width] = (byte) 1;
					}
				}
				length++;
				if(length == 16){
					length = 0;
					width++;
				}
			}
		
		
		byte[] dataArray = new byte[builder.getMaxHeight() * builder.getMaxLength() * builder.getMaxWidth()];
		
		int blockCount = 0;
		
		for(int bWidth = 0; bWidth < builder.getMaxWidth(); bWidth++){
			for(int bHeight = 0; bHeight < builder.getMaxHeight(); bHeight++){
				for(int bLength = 0; bLength < builder.getMaxLength(); bLength++){
					
					if(byteArray[bLength][bHeight][bWidth] != null){
						dataArray[blockCount] = byteArray[bLength][bHeight][bWidth];
					}else{
						dataArray[blockCount] = (byte) 0;
					}
					
					blockCount++;
					
				}
			}
		}
		
		Tag data = new Tag(Type.TAG_Byte_Array, "Data", dataArray);
		
		return data;
		
	}
	
}
