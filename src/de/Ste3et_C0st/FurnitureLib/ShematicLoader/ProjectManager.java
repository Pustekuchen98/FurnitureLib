package de.Ste3et_C0st.FurnitureLib.ShematicLoader;

import java.io.FileNotFoundException;
import org.bukkit.configuration.file.YamlConfiguration;

import de.Ste3et_C0st.FurnitureLib.ModelLoader.ModelFileLoader;
import de.Ste3et_C0st.FurnitureLib.main.Type.PlaceableSide;

public class ProjectManager {

	public synchronized void loadProjectFiles(){
//		List<String> projectList = new ArrayList<String>();
//		File folder = new File("plugins/FurnitureLib/models/");
//		if(folder.exists()){
//			Arrays.asList(folder.listFiles()).stream().filter(Objects::nonNull).forEach(file -> {
//				String str = loadFile(file);
//				if(Objects.nonNull(str)) {
//					if(!projectList.contains(str)) projectList.add(str);
//				}
//			});
//		}
//		
//		if(projectList.size()>1){
//			String str = "";
//			Collections.sort(projectList);
//			for(String s : projectList) {str += s + ",";}
//			str = str.substring(0, str.length() - 1);
//			FurnitureLib.getInstance().send("FurnitureLib load Models("+projectList.size()+"): " + str);
//			FurnitureLib.getInstance().registerPluginFurnitures(FurnitureLib.getInstance());
//		}else{
//			FurnitureLib.getInstance().send("If you want to install more models look at here: http://dicecraft.de/furniture/models.php");
//		}
		ModelFileLoader.loadModelFiles();
	}
	
//	private String loadFile(File file) {
//		if(!file.getName().toLowerCase().endsWith(".dmodel")) {
//			System.out.println("Cannot load " + file.getName() + " < 1.13 file");
//		}else {
//			try {
//				YamlConfiguration configuration = new YamlConfiguration();
//				configuration.load(file);
//				String name = file.getName().replaceAll(".dModel", "");
//				String header = getHeader(configuration, name);
//				if(configuration.contains(header + ".projectData.entitys") || configuration.contains(header + ".projectData.blockList")){
//					PlaceableSide side = PlaceableSide.TOP;
//					String systemID = configuration.getString(header + ".system-ID");
//					if(configuration.isSet(header + ".placeAbleSide")){side = PlaceableSide.valueOf(configuration.getString(header + ".placeAbleSide"));}
//					Project p = new Project(systemID, FurnitureLib.getInstance(), new FileInputStream(file), side, ProjectLoader.class).setEditorProject(true);
//					int Width = 0, Height = 0, Lentgh = 0;
//					if(configuration.isConfigurationSection(header+".projectData.blockList")){
//						int minWitdh = 0, maxWidth = 0, maxHeight = 0, minHeight = 0, maxLentgh = 0, minLentgh = 0;
//						for(String str : configuration.getConfigurationSection(header+".projectData.blockList").getKeys(false)){
//							double x = configuration.getDouble(header+".projectData.blockList." + str + ".xOffset");
//							double y = configuration.getDouble(header+".projectData.blockList." + str + ".yOffset");
//							double z = configuration.getDouble(header+".projectData.blockList." + str + ".zOffset");
//							if(x > maxWidth) maxWidth = (int) x;
//							if(y > maxHeight) maxHeight = (int) y;
//							if(z > maxLentgh) maxLentgh = (int) z;
//							if(x < minWitdh) minWitdh = (int) x;
//							if(y < minHeight) minHeight = (int) y;
//							if(z < minLentgh) minLentgh = (int) z;
//						}
//						minWitdh = Math.abs(minWitdh);
//						minHeight = Math.abs(minHeight);
//						minLentgh = Math.abs(minLentgh);
//						
//						Lentgh = minWitdh + maxWidth + 1;
//						Height = minHeight + maxHeight + 1;
//						Width = minLentgh + maxLentgh + 1;
//					}
//					p.setSize(Width, Height, Lentgh, CenterType.RIGHT);
//					return systemID;
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
	
	public String getHeader(YamlConfiguration file, String fileName){
		try{
			return (String) file.getConfigurationSection("").getKeys(false).toArray()[0];
		}catch(ArrayIndexOutOfBoundsException ex){
			return fileName;
		}
	}
	
	public void registerProeject(String name, PlaceableSide side) throws FileNotFoundException{
//		File file = new File("plugins/FurnitureLib/models/", name+".dModel");
//		InputStream stream = new FileInputStream(file);
//		Project pro = new Project(name, FurnitureLib.getInstance(), stream, side, ProjectLoader.class);
//		pro.setEditorProject(true);
		//pro.setModel(stream);
	}
}
