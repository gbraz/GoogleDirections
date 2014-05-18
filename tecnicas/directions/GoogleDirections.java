package tecnicas.directions;

import java.io.*;
import java.net.*;
import org.json.*;

//Autor: Geraldo Braz Duarte Filho
//Modificado em: 18/05/2014

public class GoogleDirections{
	/**Método que solicita ao google o arquivo Json com todas as informações 
	 * de direções. Retorna uma String com o texto do arquivo Json.
	 * @param String origem, String destino
	 * @return String json*/
	public static String solicitacao(String origem, String destino){
		String googleJson = null;
		String googleOutput;
		URL url;
		
		try {
			url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + 
											origem + "&destination=" + destino +"&sensor=false");
			
			//Abrindo conexão e recebendo o stream direto em forma de String
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			googleOutput =  br.readLine();
			
			//Criando uma string do arquivo Json recebido pelo Google 
			while(googleOutput != null){
				if(googleJson == null)
					googleJson = googleOutput + "\n";
				else
					googleJson = googleJson + googleOutput + "\n";
				googleOutput =  br.readLine();
			}	
			br.close();
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return googleJson;
		
	}
	
	/**Método que dá o caminho através de coordenadas geográficas
	 * @param String json
	 * @return String caminhoCoordenadas*/
	public static String caminho(String json){
		String caminhoCoordenadas = "";
	
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routesPrincipal = routes.getJSONObject(0);
		
		JSONArray legs = routesPrincipal.getJSONArray("legs");
		
		JSONObject legsPrincipal = legs.getJSONObject(0);
		
		JSONArray steps = legsPrincipal.getJSONArray("steps");
			
		JSONObject stepsPrincipal;
		
		for(int i = 0; i < steps.length(); i++){
			JSONObject jobj4 = steps.getJSONObject(i);
			
			//Formatando a string com as coordenadas para uso no link
			if(i == 0){
				stepsPrincipal = jobj4.getJSONObject("start_location");
				caminhoCoordenadas = "" + stepsPrincipal.getDouble("lat");
				caminhoCoordenadas = caminhoCoordenadas + "," + stepsPrincipal.getDouble("lng");
				stepsPrincipal = jobj4.getJSONObject("end_location");
				caminhoCoordenadas = caminhoCoordenadas + "|" + stepsPrincipal.getDouble("lat");
				caminhoCoordenadas = caminhoCoordenadas + "," + stepsPrincipal.getDouble("lng");
			}
			else{
				stepsPrincipal = jobj4.getJSONObject("end_location");
				caminhoCoordenadas = caminhoCoordenadas + "|" + stepsPrincipal.getDouble("lat");
				caminhoCoordenadas = caminhoCoordenadas + "," + stepsPrincipal.getDouble("lng");
			}
		}
		return caminhoCoordenadas;
	}
	
	/**Método que dá o caminho em código polyline
	 * @param String json
	 * @return String polyline*/
	public static String polyline(String json){
		String polylineStr = "";
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routesPrincipal = routes.getJSONObject(0);
		
		JSONObject overviewPolyline = routesPrincipal.getJSONObject("overview_polyline");
		
		polylineStr = overviewPolyline.getString("points");

		return polylineStr;
	}
}

