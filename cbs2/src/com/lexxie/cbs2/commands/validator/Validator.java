package com.lexxie.cbs2.commands.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lexxie.cbs2.commands.Command;
import com.lexxie.cbs2.commands.Command.CType;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.project.ProjectManager;

public class Validator {

	private  Pattern parameterPattern, coordiantesPattern, numberPattern, nbtPattern;
	
	private int warnings = 0;

	private List<WarningInfo> info = new ArrayList<WarningInfo>();
	
	public void validateAll(){
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		
		while(it.hasNext()){
			
			Entry pair = (Entry) it.next();
			
			for(BlockData data : ((Execute) pair.getValue()).getCommands()){
				
				validate(data.getCommand());
				
			}
		}
		
		for(BlockData data : Init.getCommands()){
			validate(data.getCommand());
		}
		
		System.out.println("Size:" + info.size() + "Warnings:" + warnings);
		
	}
	
	public void validate(String validate){
		
		String[] parts = validate.split("\\s+");
		
		boolean found = false; 
		
		if(parts.length > 0 && !validate.equals("#")){
		
			if(parts[0].charAt(0) == '#')
				parts[0] = parts[0].substring(1);
			
			for(Command command : Command.syntax){
			 
				if(command.getCommand().equals(parts[0])){
					
					found = true;
					
					Command root = command;
					
					for(int i = 1; i < parts.length; i++){
						
						root = getSubCommand(root, parts [i]);
						
						if(root != null){
							
							if(root.getType() == CType.EXECUTE && !parts[i].equals("detect")){
								
								String execute = "#" + parts[i];
								
								for(int j = i; j < parts.length - 1; j++){
									execute += " " + parts[j + 1];
								}
								
								validate(execute);
								break;
							}
							
							if(i == parts.length - 1 && root.hasNext() && !root.getNextAllOptional()){
								noMatch(validate, root.getExpectations(), parts[i]);
								break;
							}
							
							if(root.getType() == CType.COMMAND && !root.getCommand().equals(parts[i])){
								noMatch(validate, "Invalid command", parts[i]);
							}else if(root.getType() == CType.PARAMETER){
								Matcher m = parameterPattern.matcher(parts[i]);
								if(!m.matches()){
									noMatch(validate,"Invalid Parameter", parts[i]);
								}
							}else if(root.getType() == CType.TEXT){
								break;
							}else if(root.getType() == CType.COORDIANTES){
								Matcher m = coordiantesPattern.matcher(parts[i]);
								if(!m.matches()){
									noMatch(validate, "Invalid coordiante", parts[i]);
								}
							}else if(root.getType() == CType.NUMBER){
								Matcher m = numberPattern.matcher(parts[i]);
								if(!m.matches()){
									noMatch(validate, "Expected number", parts[i]);
								}
							}else if(root.getType() == CType.NBT){
								Matcher m = nbtPattern.matcher(parts[i]);
								if(!m.matches()){
									noMatch(validate, "Invalid nbt format", parts[i]);
								}
							}
						}else{
							noMatch(validate,"no command found", parts[i]);
							break;
						}
						
					}
			
					if(parts.length == 1 && root.hasNext()){
						noMatch(validate, root.getExpectations(), parts[0]);
					}
					
				}
			}
			
			if(!found){
				noMatch(validate,"Unrecoginzed command" , parts[0]);
			}
			
		}	
	}
	
	private void noMatch(String line,String desc, String invalid){
	
		warnings ++;
		
		info.add(new WarningInfo(line, invalid, desc));
		
	}
	
	private Command getSubCommand(Command cmd, String key){
		
		if(cmd != null && cmd.getCommandsMap().size() > 1){
			if(!cmd.getNextCommands(key).isEmpty()){
				return cmd.getNextCommand(key);
			}else if(cmd != null && cmd.getNextAllCommand() != null){
				return cmd.getNextAllCommand();
			}else if(cmd != null && cmd.containsExecute()){
				Command c =  new Command();
				c.setType(CType.EXECUTE);
				return c;
			}
		}else if(cmd != null && cmd.getCommandsMap().size() == 1){
			
			List<Command> commands = cmd.getAllNextCommands();
			
			if(!commands.isEmpty()){
				Command c = commands.get(0);
				
				if(c.getType() == CType.ALL){
					return c;
				}else if(c.getType() == CType.PARAMETER){
					return c;
				}else{
					return c;
				}
			}
			
		}
		
		return null;
	}
	
	public void initPattern(){
		
		parameterPattern = Pattern.compile("@(a|p|r|e)(\\[(" + initStaticParameters() + ")(," + initStaticParameters() + ")*\\])?");
		
		coordiantesPattern = Pattern.compile("~?[0-9]*?(.[0-9]+)?");
		  
		numberPattern = Pattern.compile("[0-9]+?");
		
		nbtPattern = Pattern.compile("\\{" + initNBT() + "(," + initNBT() + ")*\\}");
		
	}
	
	private String initNBT(){
		
		String expression = "(id:\"[a-zA-Z0-9._:]+\"|Pos:\\[[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?\\]|Motion:\\[[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?\\]"
				+ "|Rotation:\\[[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?\\]|FallDistance:[0-9]+|Fire:-?[0-9]+|Air:[0-9]+|OnGround:(1|0)|Dimension:(-1|0|1)|Invulnerable:(1|0)"
				+ "|PortalCooldown:[0-9]+|UUIDMost:[a-zA-Z0-9]+|UUIDLeast:[a-zA-Z0-9]+|Invisible:(1|0)|CustomName:\"[a-zA-Z0-9]+\"|CustomNameVisible:(1|0)|Silent:(1|0)"
				+ "|Passengers:\\[[a-zA-Z0-9\\W]+\\]|Glowing:(1|0)|CommandStats:\\{(SuccessCountObjective:\"[a-zA-Z0-9]+\"|SuccessCountName:\"[a-zA-Z0-9]+\""
				+ "|\"AffectedBlocksObjective:[a-zA-Z0-9]+\"|AffectedBlocksName:\"[a-zA-Z0-9]+\"|AffectedEntitiesObjective:\"[a-zA-Z0-9]+\"|AffectedEntitiesName:\"[a-zA-Z0-9]+\""
				+ "|AffectedItemsObjective:\"[a-zA-Z0-9]+\"|AffectedItemsName:\"[a-zA-Z0-9]+\"|QueryResultObjective:\"[a-zA-Z0-9]+|QueryResultName:\"[a-zA-Z0-9]+ \")"
				+ "(,(SuccessCountObjective:\"[a-zA-Z0-9]+\"|SuccessCountName:\"[a-zA-Z0-9]+\""
				+ "|\"AffectedBlocksObjective:[a-zA-Z0-9]+\"|AffectedBlocksName:\"[a-zA-Z0-9]+\"|AffectedEntitiesObjective:\"[a-zA-Z0-9]+\"|AffectedEntitiesName:\"[a-zA-Z0-9]+\""
				+ "|AffectedItemsObjective:\"[a-zA-Z0-9]+\"|AffectedItemsName:\"[a-zA-Z0-9]+\"|QueryResultObjective:\"[a-zA-Z0-9]+|QueryResultName:\"[a-zA-Z0-9]+ \"))*\\}"
				+ "|Health:[0-9]+(.[0-9]+)?|AbsorptionAmount:[0-9]+(.[0-9]+)?|HurtTime:[0-9]+|HurtByTimestamp:[0-9]+|DeathTime:[0-9]+"
				+ "|Attributes:\\[" + attributes() + "(," + attributes() + ")*\\]|ActiveEffect:\\[" + effects() + "(," + effects() + ")*\\]"
				+ "|HandItems:\\[" + items() + "(," + items() +")?\\]|ArmorItems:\\[" + items() + "(," + items() + "){0,3}\\]|DeathLootTable:\"[a-zA-Z0-9]+\""
				+ "|DeatLootTableSeed:[0-9]+|CanPickUpLoot:(1|0)|NoAI:(1|0)|PersistenceRequired:(1|0)|LeftHanded:(1|0)|Team:\"[a-zA-Z0-9]\"|Leashed:(1|0)"
				+ "|BatFlags:(0|1)|IsChickenJockey:(1|0)|EggLayTime:[0-9]+|Powered:(1|0)|ExplosionRadius:[0-9]+|Fuse:[1-9]+|Ignited:(1|0)|"
				+ "DragonPhase:(0|1|2|3|4|5|6|7|8|9|10)|carried:[a-zA-Z0-9]+|carriedData:[a-zA-Z0-9]|LifeTime:[0-9]+|PlayerSpawned:(1|0)|Bred:(1|0)"
				+ "|ChestedHorse:(1|0)|EatingHaystack:(1|0)|HasReproduced:(1|0)|Tame:(1|0)|Temper:[0-9]+|Type:(0|1|2|3|4)|Variant:[0-9]+|OwnerName:\"[a-zA-Z0-9]+\""
				+ "|OwnerUUID:\"[a-zA-Z0-9]\"|Items:\\[" + items() + "(," + items() + ")*\\]|SaddleItem:\\{id:saddle\\}|SkeletonTrapTime:[0-9]+"
				+ "|ExplosionPower:[0-1]+|Elder:(1|0)|Size:[0-9]+|wasOnGround:(1|0)|InLove:[0-9]+|Age:[0-9]+|ForcedAge:[0-9]+|CatType:(0|1|2|3)"
				+ "|IsVillager:(1|0)|IsBaby:(1|0)|ConversionTime:-?[0-9]+|CanBreakDoors:(1|0)|VillagerProfession:(0|1|2|3|4|5)|ZombieType:(0|1|2|3|4|5|6)"
				+ "|Anger:-?[0-9]+|HurtBy:\"[a-zA-Z0-9]+\"|RabbitType:[0-9]+|MoreCarrotTicks:[0-9]+|Sheared:(1|0)|Color:(0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15)"
				+ "|Peek:[0-9]+|AttachFace:[0-9]+|APX:[0-9]+|APY:[0-9]+|APZ:[0-9]+|SkeletonType:(0|1)|Profession:(0|1|2|3|4|5)|Riches:[0-9]+|Career:[0-9]+"
				+ "|CareerLevel:[0-9]+|Willing:(0|1)|Inventory:\\[" + items() + "(," + items() +  ")*\\]|Offers:\\{" + offers() + "(," + offers() + ")*\\}"
				+ "|PlayerCreated:(1|0)|Invul:[0-9]+|Angry:(1|0)|CollarColor:[0-9]+|Sitting:(1|0)|NoBasePlate:(1|0)|NoGravity:(1|0)|Item:" + items() + ")";
		
		return expression;
	}
	
	private String offers(){
		
		String expression = "Recipes:\\[\\{(rewardExp:(1|0)|maxUses:[0-9]+|uses:[0-9]+|buy:\\[" + items() + "(," + items() + ")\\]"
				+ "|buyB:\\[" + items() + "(," + items() + ")\\]|sell:\\[" + items() + "(," + items() + ")\\])\\}"
				+ "(,(\\{(rewardExp:(1|0)|maxUses:[0-9]+|uses:[0-9]+|buy:\\[" + items() + "(," + items() + ")\\]"
				+ "|buyB:\\[" + items() + "(," + items() + ")\\]|sell:\\[" + items() + "(," + items() + ")\\])\\}))*\\]";
		
		
		
		return expression;
		
	}
	
	private String items(){
		
		String expression = "\\{(id:\"[a-zA-Z0-9._:]+\"|Pos:\\[[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?\\]|Motion:\\[[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?\\]"
				+ "|Age:-?[0-9]+|Health:[0-9]+|PickupDelay:[0-9]+|Owner:\"[a-zA-Z0-9]+\"|Thrower:\"[a-zA-Z0-9]+\"|Count:-?[0-9]+|Slot:[a-zA-Z0-9]+"
				+ "|Damage:[0-9]+|Slot:[a-zA-Z0-9])"
				+ "(,(id:\"[a-zA-Z0-9._:]+\"|Pos:\\[[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?\\]|Motion:\\[[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?,[0-9]+(.[0-9]+)?\\]"
				+ "|Age:-?[0-9]+|Health:[0-9]+|PickupDelay:[0-9]+|Owner:\"[a-zA-Z0-9]+\"|Thrower:\"[a-zA-Z0-9]+\"|Count:-?[0-9]+|Slot:[a-zA-Z0-9]+"
				+ "|Damage:[0-9]+|Slot:[a-zA-Z0-9]))*\\}";
		
		return expression;
	
	}
	
	private String effects(){
		
		String expression = "\\{(Id:(1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27)|Amplifier:[0-9]+|Duration:[0-9]+|Ambient:(1|0)"
				+ "|ShowParticles:(1|0))(,(Id:(1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27)|Amplifier:[0-9]+|Duration:[0-9]+|Ambient:(1|0)"
				+ "|ShowParticles:(1|0)))*\\}";
		
		return expression;
		
	}
	
	private String attributes(){
		
		String expression = "\\{(Name:(generic.maxHealth|generic.followRange|generic.knockbackResistance|generic.movementSpeed|generic.attackDamage"
				+ "|generic.armor|generic.armorToughness|generic.attackSpeed|generic.luck|horse.jumpStrength|zombie.spawnReinforcemets)|Base:[0-9]+"
				+ "|Modifiers:\\[" + modifiers() + "(," + modifiers() + ")*\\])"
				+ "(,(Name:(generic.maxHealth|generic.followRange|generic.knockbackResistance|generic.movementSpeed|generic.attackDamage"
				+ "|generic.armor|generic.armorToughness|generic.attackSpeed|generic.luck|horse.jumpStrength|zombie.spawnReinforcemets)|Base:[0-9]+"
				+ "|Modifiers:\\[" + modifiers() + "(," + modifiers() + ")*\\]))*\\}";
		
		return expression;
		
	}
	
	private String modifiers(){
		
		String expression = "(Name:\"[a-zA-Z0-9]+\"|Amount:[0-9]+|Operation:(0|1|2)|UUIDMost:[a-zA-Z0-9]+|UUIDLeast:[a-zA-Z0-9]+)";
		
		return expression;
		
	}	
	
	private  String initStaticParameters(){
		
		String expression = "(x=-?[0-9]+|z=[0-9]+|y=-?[0-9]+|r=-?[0-9]+|c=-?[0-9]+|"
			+ "m=-?[0-9]+|l=-?[0-9]+|lm=-?[0-9]+|rx=-?[0-9]+|ry=-?[0-9]+|rxm=-?[0-9]+|rym=-?[0-9]+|=-?[0-9]+|team=[a-zA-Z0-9]+|name=[a-zA-Z0-9]+|tag=[a-zA-Z0-9]+|dx=-?[0-9]+|dy=-?[0-9]+|dz=-?[0-9]+|"
			+ "x=!-?[0-9]+|z=!-?[0-9]+|y!=-?[0-9]+|r=!-?[0-9]+|c=!-?[0-9]+|"
			+ "m=!-?[0-9]+|l=!-?[0-9]+|lm=!-?[0-9]+|rx=!-?[0-9]+|ry=!-?[0-9]+|rxm=!-?[0-9]+|rym=!-?[0-9]+|=!-?[0-9]+|"
			+ "team=![a-zA-Z0-9]+|name=![a-zA-Z0-9]+|tag=![a-zA-Z0-9]+|dx=!-?[0-9]+|dy=!-?[0-9]+|dz=!-?[0-9]+|"
			+ "type=Item|type=XPOrb|type=AreaEffectCloud|type=ThrownEgg|type=Arrow|type=Snowball|type=Fireball|type=SnallFireball|type=ThrownEnderpearl|type=EyeOfEnderSignal"
			+ "|type=ThrownPotion|type=ThrownExpBottle|type=WitherSkull|type=FireworksRocketEntity|type=SpectralArrow|type=ShulkerBullet|type=DragonFireball"
			+ "|type=LeashKnot|type=Painting|type=ItemFrame|type=ArmorStand|type=PrimedTnt|type=FallingSand|type=MinecartCommandBlock|type=Boat|type=MinecartRideable"
			+ "|type=MinecartChest|type=MinecartFurnace|type=MinecartTNT|type=MinecartHopper|type=MinecartSpawner|type=Player|type=Creeper|type=Skeleton|type=Spider"
			+ "|type=Giant|type=Zombie|type=Slime|type=Ghast|type=PigZombie|type=Enderman|type=CaveSpider|type=Silverfish|type=Blaze|type=LavaSlime|type=EnderDragon"
			+ "|type=WitherBoss|type=Bat|type=Witch|type=Endermite|type=Guardian|type=Shulker|type=Pig|type=Sheep|type=Cow|type=Chicken|type=Squid|type=Wolf|type=MushroomCow"
			+ "|type=SnowMan|type=Ozelot|type=VillagerGolem|type=EntityHorse|type=Rabbit|type=PolarBear|type=Villager|type=EnderCrystal|"
			+ "type=!Item|type=!XPOrb|type=!AreaEffectCloud|type=!ThrownEgg|type=!Arrow|type=!Snowball|type=!Fireball|type=!SnallFireball|type=!ThrownEnderpearl|type=!EyeOfEnderSignal"
			+ "|type=!ThrownPotion|type=!ThrownExpBottle|type=!WitherSkull|type=!FireworksRocketEntity|type=!SpectralArrow|type=!ShulkerBullet|type=!DragonFireball"
			+ "|type=!LeashKnot|type=!Painting|type=!ItemFrame|type=!ArmorStand|type=!PrimedTnt|type=!FallingSand|type=!MinecartCommandBlock|type=!Boat|type=!MinecartRideable"
			+ "|type=!MinecartChest|type=!MinecartFurnace|type=!MinecartTNT|type=!MinecartHopper|type=!MinecartSpawner|type=!Player|type=!Creeper|type=!Skeleton|type=!Spider"
			+ "|type=!Giant|type=!Zombie|type=!Slime|type=!Ghast|type=!PigZombie|type=!Enderman|type=!CaveSpider|type=!Silverfish|type=!Blaze|type=!LavaSlime|type=!EnderDragon"
			+ "|type=!WitherBoss|type=!Bat|type=Witch|type=!Endermite|type=!Guardian|type=!Shulker|type=!Pig|type=!Sheep|type=!Cow|type=!Chicken|type=!Squid|type=!Wolf|type=!MushroomCow"
			+ "|type=!SnowMan|type=!Ozelot|type=!VillagerGolem|type=!EntityHorse|type=!Rabbit|type=!PolarBear|type=!Villager|type=!EnderCrystal"
			+ createScores() + ")";
		
			
		return expression;
	}
	
	private String createScores(){
		
		String score = "";
		
		for(Entry<String, String> entry : ProjectManager.activeProject.getScoreHandler().getScores().entrySet()){
			
			 score += "|score_" + entry.getKey() + "_min=-?[0-9]+|score_" + entry.getKey() + "=-?[0-9]+";
		}
		
		return score;
	}
	
	public int getWarningCount(){
		return warnings;
	}
	
	public void showWarnings(){	
		WarningGUI gui = new WarningGUI(info);
		gui.setVisible(true);
	}
	
	public class WarningInfo{
		
		String line, warning, description;
		
		public WarningInfo(String line, String warning, String description){
			
			this.line = line;
			this.warning = warning;
			this.description = description;
			
		}
		
		public String getDescription(){
			return description;
		}
		
		public String getWarning(){
			return warning;
		}
		
		public String getLine(){
			return line;
		}
		
	}
	
}
