package com.lexxie.cbs2.parser.data.nbt;

import java.util.HashMap;

import com.lexxie.cbs2.parser.coordinates.Coordinates;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.nbt.Tag.Type;

public class Builder {

	public static enum algorithmus{sideways, up, nbt};
	
	private static algorithmus alg;
	
	static{
		if(alg == null){
			alg = algorithmus.nbt;
		}
	}
	
	public static void setAlgo(algorithmus algo){
		alg = algo;
	}
	
	public static algorithmus getAlgo(){
		return alg;
	}
	
	private int x, y, z;
	private int maxHeight, maxWidth, maxLength;
	
	public Builder(){
		x = 0;
		y = 0;
		z = 0;
		
		if(alg == algorithmus.sideways){
			maxHeight = 1;
			maxWidth = 1;
			maxLength = 16;
		}else if(alg == algorithmus.up){
			maxHeight = 1;
			maxWidth = 1;
			maxLength = 16;
		}
		
	}

	public Tag buildBlock(Builder builder, HashMap<Coordinates, BlockData> coordinateData){
		
		if(alg == algorithmus.sideways){
			return CreateTags.createTagBlocksSide(builder, coordinateData);
		}else if(alg == algorithmus.up){
			return CreateTags.createTagBlockUp(builder);
		}
		return null;
	}
	
	public Tag buildData(Builder builder, HashMap<Coordinates, BlockData> coordinateData){
		
		if(alg == algorithmus.sideways){
			return CreateTags.createTagDataSide(builder, coordinateData);
		}else if(alg == algorithmus.up){
			return CreateTags.createTagDataUp(builder);
		}
		
		return null;
	}
	
	public void doStepEntitie(){
		
		if(alg == algorithmus.sideways){
			algSidewaysEntitie();
		}else if(alg == algorithmus.up){
			algUpEntitie();
		}
		
	}
	
	public void doStepData(){
		
		if(alg == algorithmus.sideways){
			algSidewaysData();
		}else if(alg == algorithmus.up){
			algUpData();
		}
		
	}
	
	private void algUpEntitie(){
		
		if(y < maxHeight){
			y++;
		}else{
			y = 0;
			if(z < maxLength - 1){
				z++;
				x = 0;
			}else{
				z = 0;
				x++;
			}
		}
		
	
	}
	
	private void algUpData(){
		
		if(y < maxHeight){
			y++;
		}else{
			z++;
			y = 0;
		}
		
		 if(z == maxLength){
				z = 0;
				y = 0;
				x++;
			}
		
	}
	
	private void algSidewaysEntitie(){
		
		//increase coords
		if(z < maxLength - 1 && z > 1){
			if(isEven(y))z++;
			else z--;
		}else{
			if(z == 1 && isEven(y)){z++;y--;}
			else if(z == maxLength - 1 && !isEven(y)){z--;y--;}
			y++;
		}
		
	}
	
	private void algSidewaysData(){
		
		if(x < maxWidth - 1){
			x++;
		}else{
			x = 0;
			if(z < maxLength - 1){
				z++;
			}else{
				x = 0;
				z = 0;
				y++;
			}
		}  
		
	}
	
	public void nextRow(){
		
		if(alg == algorithmus.up){
			z++;
			y = 0;
			if(z == maxLength){
				z = 0;
				x++;
			}
		}else if(alg == algorithmus.sideways){
			x++;
			y = 0;
			z = 1;
		}
		
	}
	
	public byte getByteOnPos(BlockData data){
		
		if(alg == algorithmus.up){
			return getByteOnPosUp(data);
		}else if(alg == algorithmus.sideways){
			return getByteOnPosSide(data);
		}
		return 0;
		
	}
	
	private byte getByteOnPosUp(BlockData data){
		
		if(data.isConditional()){
			return 9;
		}else{
			return 1;
		}
	}
	
	private byte getByteOnPosSide(BlockData data){
		
		//set blocks data relative to their position
		if(z > 0){
			//execution block
			if(z < maxLength - 1 && isEven(y)){
				//facing south
				if(data.isConditional()){
					return 11;
				}else{
					return 3;
				}
			}else if(z > 1 && !isEven(y)){
				//facing north
				if(data.isConditional()){
					return 10;
				}else{
					return 2;
				}
			}else{
				//facing up
				if(data.isConditional()){
					return 9;
				}else{
					return 1;
				}
			}
		}else{
			//initialize block
			if(y < maxHeight - 1 && isEven(x)){
				//facing up
				if(data.isConditional()){
					return 9;
				}else{
					return 1;
				}
			}else if(y > 0 && !isEven(x)){
				//facing down
				if(data.isConditional()){
					return 8;
				}else{
					return 0;
				}
			}else{
				//facing east
				if(data.isConditional()){
					return 13;
				}else{
					return 5;
				}
			}
		}
		
	}
		
	public void reset(){
		x = 0;
		y = 0;
		z = 0;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
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
	
	/**
	 * 	sets new Max values to integrate width, and height (length is always 15)
	 * */
	public void setMax(){
		//set max_height
		if(y == maxHeight){
			maxHeight++;
		}
		//set max_width
		if(x == maxWidth){
			maxWidth++;
		}
	}
	
}
