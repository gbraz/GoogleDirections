package tecnicas.directions;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


//Autor: Geraldo Braz Duarte Filho
//Modificado em: 18/05/2014

public class GoogleDirections implements ConsultasGoogle{
	
	/**Método que solicita ao google o arquivo Json com todas as informações 
	 * de direções. Retorna uma String com o texto do arquivo Json.
	 * @param String origem, String destino
	 * @return String json*/
	public static String getJson(String origem, String destino){
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
	
	
	/**Método que dá o caminho em código polyline
	 * @param String json
	 * @return String polyline*/
	public static String getPolyline(String origem, String destino){
		String json = getJson(origem, destino);
		
		String polylineStr = "";
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONObject overviewPolyline = routes1.getJSONObject("overview_polyline");
		
		polylineStr = overviewPolyline.getString("points");

		return polylineStr;
	}

	@Override
	public int getDistancia(String origem, String destino) {
		// TODO Auto-generated method stub
		String json = getJson(origem, destino);
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONArray legs = routes1.getJSONArray("legs");
		
		JSONObject legs1 = legs.getJSONObject(0);
		
		JSONObject distance = legs1.getJSONObject("distance");
		
		return distance.getInt("value");
	}

	@Override
	public int getDuracao(String origem, String destino) {
		// TODO Auto-generated method stub
		String json = getJson(origem, destino);
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONArray legs = routes1.getJSONArray("legs");
		
		JSONObject legs1 = legs.getJSONObject(0);
		
		JSONObject duration = legs1.getJSONObject("duration");
		
		return duration.getInt("value");
	}

	@Override
	public String getDistanciaStr(String origem, String destino) {
		// TODO Auto-generated method stub
		String json = getJson(origem, destino);
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONArray legs = routes1.getJSONArray("legs");
		
		JSONObject legs1 = legs.getJSONObject(0);
		
		JSONObject distance = legs1.getJSONObject("distance");
		
		return distance.getString("text");
	}

	@Override
	public String getDuracaoStr(String origem, String destino) {
		// TODO Auto-generated method stub
		String json = getJson(origem, destino);
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONArray legs = routes1.getJSONArray("legs");
		
		JSONObject legs1 = legs.getJSONObject(0);
		
		JSONObject duration = legs1.getJSONObject("duration");
		
		return duration.getString("text");
	}

	@Override
	public String getCoordenadas(String endereco) {
		// TODO Auto-generated method stub
		String json = getJson(endereco, endereco);
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routes1 = routes.getJSONObject(0);
		
		JSONArray legs = routes1.getJSONArray("legs");
		
		JSONObject legs1 = legs.getJSONObject(0);
		
		JSONObject end_location = legs1.getJSONObject("end_location");
		
		return "" + end_location.getDouble("lat") + end_location.getDouble("lng");
	}

	@Override
	public ArrayList<String> getCaminho(String origem, String destino) {
		// TODO Auto-generated method stub
		String json = getJson(origem, destino);
		
		JSONObject jsonObj = new JSONObject(json);
		
		JSONArray routes = jsonObj.getJSONArray("routes");
		
		JSONObject routesPrincipal = routes.getJSONObject(0);
		
		JSONArray legs = routesPrincipal.getJSONArray("legs");
		
		JSONObject legs1 = legs.getJSONObject(0);
		
		JSONArray steps =  legs1.getJSONArray("steps");
		
		ArrayList<String> lista = new ArrayList<String>();
		
		for(int i = 0; i < steps.length(); i++){
			JSONObject stepAtual = steps.getJSONObject(i);
			
			lista.add(stepAtual.getString("html_instructions"));
		}
		
		return lista;
	}
}

