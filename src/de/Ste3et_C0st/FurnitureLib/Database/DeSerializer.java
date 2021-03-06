package de.Ste3et_C0st.FurnitureLib.Database;

import de.Ste3et_C0st.FurnitureLib.NBT.NBTCompressedStreamTools;
import de.Ste3et_C0st.FurnitureLib.NBT.NBTTagCompound;
import de.Ste3et_C0st.FurnitureLib.NBT.NBTTagList;
import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import de.Ste3et_C0st.FurnitureLib.main.FurnitureManager;
import de.Ste3et_C0st.FurnitureLib.main.ObjectID;
import de.Ste3et_C0st.FurnitureLib.main.Type.EventType;
import de.Ste3et_C0st.FurnitureLib.main.Type.PublicMode;
import de.Ste3et_C0st.FurnitureLib.main.Type.SQLAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class DeSerializer {
	
	public int purged = 0;
	
	@SuppressWarnings("unchecked")
	public static ObjectID Deserialize(String objId, String in, SQLAction action, World world) {
		ObjectID obj = new ObjectID(objId);
		byte[] by = Base64.getDecoder().decode(in);
		try(ByteArrayInputStream bin = new ByteArrayInputStream(by)) {
			NBTTagCompound compound = NBTCompressedStreamTools.read(bin);
			if(Objects.isNull(compound)) {return null;}
			EventType evType = EventType.valueOf(compound.getString("EventType"));
			PublicMode pMode = PublicMode.valueOf(compound.getString("PublicMode")); 
			UUID uuid = uuidFetcher(compound.getString("Owner-UUID"));
			HashSet<UUID> members = membersFetcher(compound.getList("Members"));
			Location startLocation = locationFetcher(compound.getCompound("Location"), world);
			if(Objects.isNull(startLocation)){
				obj.setSQLAction(SQLAction.REMOVE);
				FurnitureLib.getInstance().getFurnitureManager().addObjectID(obj);
				return null;
			}
			obj.setStartLocation(startLocation);
			obj.setEventTypeAccess(evType);
			obj.setPublicMode(pMode);
			obj.setMemberList(members);
			obj.setUUID(uuid);
			obj.setFinish();
			obj.setSQLAction((action != null && action.equals(SQLAction.SAVE)) ? SQLAction.SAVE : SQLAction.NOTHING);
			obj.setFromDatabase(true);
			
			if(compound.hasKey("entities") || compound.hasKey("entitys")) {
				NBTTagCompound armorStands = compound.hasKey("entitys") ? compound.getCompound("entitys") : compound.getCompound("entities");
				armorStands.c().stream().filter(Objects::nonNull).forEach(packet -> {
					NBTTagCompound metadata = armorStands.getCompound((String) packet);
					Location loc = locationFetcher(metadata.getCompound("Location"), world);
					FurnitureManager.getInstance().createFromType(metadata.getString("EntityType"), loc, obj).loadMetadata(metadata);
				});
			}else if(!FurnitureLib.isNewVersion() && compound.hasKey("ArmorStands")) {
				NBTTagCompound armorStands = compound.getCompound("ArmorStands");
				armorStands.c().stream().filter(Objects::nonNull).forEach(packet -> {
					NBTTagCompound metadata = armorStands.getCompound((String) packet);
					Location loc = locationFetcher(metadata.getCompound("Location"), world);
					FurnitureManager.getInstance().createFromType("armor_stand", loc, obj).loadMetadata(metadata);
				});
			}else {
				NBTTagCompound armorStands = Converter.convertPacketItemStack(compound.getCompound("ArmorStands"));
				armorStands.c().stream().filter(Objects::nonNull).forEach(packet -> {
					NBTTagCompound metadata = armorStands.getCompound((String) packet);
					Location loc = locationFetcher(metadata.getCompound("Location"), world);
					FurnitureManager.getInstance().createFromType("armor_stand", loc, obj).loadMetadata(metadata);
				});
				obj.setSQLAction(SQLAction.UPDATE);
			}
			return obj;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static HashMap<UUID, Long> offlineMap = new HashMap<UUID, Long>();
	
	public static void autoPurge(int purgeTime) {
		FurnitureManager.getInstance().getObjectList().stream().filter(entry -> Objects.nonNull(entry.getUUID())).forEach(entry -> {
			if(!offlineMap.containsKey(entry.getUUID())) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getUUID());
				offlineMap.put(entry.getUUID(), offlinePlayer.getLastPlayed());
			}
			long time = offlineMap.containsKey(entry.getUUID()) ? offlineMap.get(entry.getUUID()) : -1;
			if(time > 0) {
				if(FurnitureLib.getInstance().isAfterDate(time, purgeTime)) {
					if(FurnitureLib.getInstance().isPurgeRemove()) {
						FurnitureManager.getInstance().remove(entry);
					}else {
						entry.setSQLAction(SQLAction.REMOVE);
					}
				}
			}
		});
	}

    public static Location locationFetcher(NBTTagCompound location, World world) {
        double X = location.getDouble("X");
        double Y = location.getDouble("Y");
        double Z = location.getDouble("Z");
        float Yaw = location.getFloat("Yaw");
        float Pitch = location.getFloat("Pitch");
        if (Objects.isNull(world)) {
            FurnitureLib.getInstance().getLogger().info("The world: " + location.getString("World") + " does not exist.");
            return null;
        }
        Location loc = new Location(world, X, Y, Z);
        loc.setYaw(Yaw);
        loc.setPitch(Pitch);
        return loc;
    }

    public static UUID uuidFetcher(String s) {
        if (s.equalsIgnoreCase("NULL")) {
            return null;
        }
        try {
            return UUID.fromString(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static HashSet<UUID> membersFetcher(NBTTagList nbtList) {
        HashSet<UUID> uuidList = new HashSet<UUID>();
        if (nbtList == null || nbtList.size() == 0) {
            return uuidList;
        }
        for (int i = 0; i < nbtList.size(); i++) {
            String string = nbtList.getString(i).replaceAll("\"", "");
            try {
                uuidList.add(UUID.fromString(string));
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        return uuidList;
    }
}
