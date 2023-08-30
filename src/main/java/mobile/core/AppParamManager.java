package mobile.core;





import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.DataTable;



public class AppParamManager {
	private Map<String, String> pMap;
	private HashMap<String, String> pVMap = new LinkedHashMap();
	public void runParamManager(DataTable arg1) {
		//List<String> list;
		//pMap.clear();
		pMap = new HashMap();
		for (Map<String, String> data : arg1.asMaps(String.class, String.class)) {
			pMap = data;
		}
		
		
	}
	
	public void runVerticalParamManager(List<List<String>> p_parameters) {
		pVMap = new LinkedHashMap();
		for (List<String> param : p_parameters) {
			String paramName = param.get(0).toString().trim();
			String paramValue = param.get(1).toString().trim();
			pVMap.put(paramName , paramValue);
			
		}
		return;
	}
		
	public String getParam(String key) {
		return pMap.get(key);
	}
	public String getVParam(String key) {
		return pVMap.get(key);
	}
	public Map<String, String> getPMap() {
	    return pMap;
	  }
	public boolean checkKeyExist(String key) {
		return pVMap.containsKey(key);
	}

}

