package tecnicas.directions;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;


//Autor: Geraldo Braz Duarte Filho
//Modificado em: 18/05/2014

public class GoogleDirections implements ConsultasGoogle{
	
	/**Método que solicita ao google o arquivo Json com todas as informações 
	 * de direções. Retorna uma String com o texto do arquivo Json.
	 * @param String origem, String destino
	 * @return String json*/
	public static String getJson(String origem, String destino, String ... ptsRef){
		String googleJson = null;
		String googleOutput;
		String waypoints;
		String urlStr;
		URL url;
		
		try {
			
			if(ptsRef.length == 0)
				url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + 
										origem + "&destination=" + destino +"&sensor=false");
			else{
				waypoints = ptsRef[0];
				for(int i = 1; i < ptsRef.length; i++)
					waypoints = waypoints + "|" + ptsRef[i];
				
				urlStr = "http://maps.googleapis.com/maps/api/directions/json?origin=" + 
				origem + "&destination=" + destino +"&waypoints="+waypoints + "&sensor=false";
				
				System.out.println(urlStr);
				
				url = new URL(urlStr);
			}
			//Abrindo conexão e recebendo o stream direto em forma de String
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			//Criando uma string do arquivo Json recebido pelo Google
			googleOutput =  br.readLine();
			
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
	
	
	/**Método que dá o caminho em código polyline
	 * @param String json
	 * @return String polyline*/
	public static String getPolyline(String origem, String destino, String ... waypoints){
		
		String json;
		String polylineStr = "";
		
		if(waypoints.length == 0)
			json = getJson(origem, destino);
		else
			json = getJson(origem, destino, waypoints); 
			
		JSONObject jsonObj = new JSONObject(json);
			
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONObject overviewPolyline = routes1.getJSONObject("overview_polyline");
			
		polylineStr = overviewPolyline.getString("points");
		
		return polylineStr;
	}

	private static JSONObject legs1(String origem, String destino){
		String json = getJson(origem, destino);
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONArray legs = routes1.getJSONArray("legs");
		
		return legs.getJSONObject(0);
	}
	
	@Override
	public int getDistancia(String origem, String destino) {
		JSONObject distance = legs1(origem, destino).getJSONObject("distance");
		
		return distance.getInt("value");
	}

	@Override
	public int getDuracao(String origem, String destino) {
		JSONObject duration = legs1(origem, destino).getJSONObject("duration");
		
		return duration.getInt("value");
	}

	@Override
	public String getDistanciaStr(String origem, String destino) {
		JSONObject distance = legs1(origem, destino).getJSONObject("distance");
		
		return distance.getString("text");
	}

	@Override
	public String getDuracaoStr(String origem, String destino) {
		JSONObject duration = legs1(origem, destino).getJSONObject("duration");
		
		return duration.getString("text");
	}

	@Override
	public String getCoordenadas(String endereco) {
		JSONObject end_location = legs1(endereco, endereco).getJSONObject("end_location");
		
		return end_location.getDouble("lat") +"|"+ end_location.getDouble("lng");
	}

	@Override
	public ArrayList<String> getCaminho(String origem, String destino) {
		String step;
		JSONArray steps =  legs1(origem, destino).getJSONArray("steps");
		
		ArrayList<String> lista = new ArrayList<String>();
		
		for(int i = 0; i < steps.length(); i++){
			JSONObject stepAtual = steps.getJSONObject(i);
			step = stepAtual.getString("html_instructions");
			step = step.replaceAll("<b>", "");
			step = step.replaceAll("</b>", "");
			lista.add(step);
		}
		
		return lista;
	}
}

