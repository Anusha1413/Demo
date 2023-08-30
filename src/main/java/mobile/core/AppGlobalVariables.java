package mobile.core;

public class AppGlobalVariables {

		private static String t_app_name;
		public static void initialize() {
			AppController.getEnvironment().equalsIgnoreCase("STAGE");
		
				t_app_name ="Your .apk name";
		}
		
		/*
		 * { if(AppController.getEnvironment().equalsIgnoreCase("STAGE")) { m_app_name
		 * ="2957.apk"; }else
		 * if(AppController.getEnvironment().equalsIgnoreCase("BETA")) { m_app_name
		 * ="MindoulaBeta.apk"; }else
		 * if(AppController.getEnvironment().equalsIgnoreCase("DEMO")) { m_app_name
		 * ="MindoulaDemo.apk"; } }
		 */
	/*	public static void initializeapp() {
			if(AppController.getAppName().equalsIgnoreCase("Your app name")) {
				AppController.setAppName("Your app file path");
			}else if(AppController.getAppName().equalsIgnoreCase("Your app name")) {
				AppController.setAppName("Your app file path");
			}
		}*/
		
			public static String getAppName(){
				return t_app_name;
			}
			public static void setAppName (String appName){
				AppGlobalVariables.t_app_name = appName;
			}

	
}
