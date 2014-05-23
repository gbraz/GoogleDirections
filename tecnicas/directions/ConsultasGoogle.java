package tecnicas.directions;

import java.util.ArrayList;

/**Inteface com os métodos de consulta ao Google Directions
 * 
 * @author Geraldo
 *
 */
public interface ConsultasGoogle{
	//Retorna a distancia da viagem em metros
	int getDistancia(String origem, String destino);
	//Retorna a duração da viagem em segundos
	int getDuracao(String origem, String destino);
	//Retorna a distancia da viagem em metros no formato de String
	String getDistanciaStr(String origem, String destino);
	//Retorna a duração da viagem em segundos no formato de String
	String getDuracaoStr(String origem, String destino);
	//Retorna as coordenadas geograficas de um endereço
	String getCoordenadas(String endereco);
	//Retorna os passos para chegar a um destino a partir de uma origem
	ArrayList<String> getCaminho(String origem, String destino);
}
