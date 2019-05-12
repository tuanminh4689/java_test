package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtils {

//	private static Properties properties;
	
//	public static void initial() {
//		Properties propertiess = new Properties();
//        try {
//        	System.out.println();
//            propertiess.load(new FileInputStream("/src/main/resources/application.properties"));
//            
//        } catch (IOException e) {
////            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
//        }
//	}
	
	public static String getProperty(String key) {
		try (InputStream input = new FileInputStream("./src/main/resources/application.properties")){
			Properties prop = new Properties();
			prop.load(input);
			return prop.getProperty(key);
			
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
