import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import org.json.simple.parser.JSONParser;
public class Main {

	public static void main(String[] args) {
		
		shuffle();
		System.exit(0);
		
	}

	
	@SuppressWarnings("unchecked")
	private static void shuffle() {
		
		//lists to save the srd
		ArrayList<File> fileSource = new ArrayList<File>();
		ArrayList<File> fileDestination = new ArrayList<File>();
		
		JSONParser parser = new JSONParser();
		
		try{
			//gets the 1.10.json file to get the locations of the sounds in .minecraft
			Object obj = parser.parse(new FileReader(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/assets/indexes/1.10.json"));
		
			HashMap<String, HashMap<String, HashMap<String, String>>> hm = (HashMap<String, HashMap<String, HashMap<String, String>>>) obj;
		
			HashMap<String, HashMap<String, String>> sounds = hm.get("objects");
		
			//get each sounds
			for(String key : sounds.keySet()){
				
				HashMap<String, String> s = sounds.get(key);
		
				if(key.split("/").length>1 && key.split("/")[1].equals("sounds")){
				
					try{
						String[] subfold = s.get("hash").split("");
						
						//get the source file
						Path p = Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/assets/objects/" + subfold[0] + subfold[1] + "/" + s.get("hash"));
						File f = p.toFile();
						fileSource.add(f);
						
						//create the destination file
						String[] keytab = key.split("/");
					
						String folder = "";
						for(int k = 0; k<keytab.length-1 ; k++){
							folder += keytab[k] + "/";
						}
						
						File f3 = new File(folder);
						f3.mkdirs();
						File f2 = new File(key);
						if(!f2.exists()){
							f2.createNewFile();
						}
					
						fileDestination.add(f2);
					
						}catch(Exception e){
							e.printStackTrace();
						}
			
					}
				
				}	
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Random r = new Random();
		r.setSeed(System.nanoTime());
		//shuffle the src/des and add each src on a des.
		Collections.shuffle(fileSource, r);
		
		for(int i = 0; i< fileSource.size(); i++){
			
			try{

				Path src = fileSource.get(i).toPath();
				Path des = fileDestination.get(i).toPath();
				Files.copy(src, des, StandardCopyOption.REPLACE_EXISTING);
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
		
		}
	}
}
