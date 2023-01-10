package com.example.iPrueba.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iPrueba.document.Booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.json.*;

//https://datosabiertos.ayto-arganda.es/dataset/3edc135e-0713-4557-9866-d76e8c91f223/resource/243046b4-f1b4-49e7-84ce-c09509675b15/download/registrohora3trimestre2022.json
//https://datosabiertos.jcyl.es/web/jcyl/risp/es/sociedad-bienestar/datos-plantillas-cuerpos-policia-local/1285126997658.json
//https://dipcas.opendatasoft.com/api/v2/catalog/datasets/directorio-de-bibliotecas-valencianas0/exports/json
//https://www.zaragoza.es/contenidos/bici/carriles_WGS84.json

@RestController
@RequestMapping("/api/ejemplo")
public class DatosAbiertosController {

	
	@Operation(description = "Introduce una url y devuelve el texto plano de la página", responses = {
			@ApiResponse(responseCode = "200", description = "Texto plano devuelto"),
			@ApiResponse(responseCode = "404", description = "La url aportada no contiene texto plano") })
	@GetMapping(value="/showByUrl", produces = MediaType.APPLICATION_JSON_VALUE)
	public static String showByUrl(URL url) throws IOException {
	    	InputStream input = url.openStream();
			StringBuilder json = new StringBuilder();
	        InputStreamReader isr = new InputStreamReader(input);
	        BufferedReader reader = new BufferedReader(isr);
	        int c;
	        while ((c = reader.read()) != -1) {
	            json.append((char) c);
	        }
	        
	        input.close();
	        return json.toString();
	    
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	
	@Operation(description = "Introduce un número de línea y recibe toda la información sobre la linea del autobus", responses = {
			@ApiResponse(responseCode = "200", description = "Linea encontrada"),
			@ApiResponse(responseCode = "404", description = "Línea no encontrada o no existe") })
	@GetMapping(value="/lineaParadasBus", produces =MediaType.APPLICATION_JSON_VALUE)
	public static String lineaParadasBus(int linea) throws IOException{
		try {
			String url = "https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTLineasYParadas/lineasyparadas.geojson";
			BufferedReader rd = new BufferedReader(new InputStreamReader(new URL(url).openStream(), Charset.forName("UTF-8")));
			
			String jsonText = readAll(rd);
			JSONArray jsonArray = new JSONArray(jsonText);

			List<JSONObject> jList = new ArrayList<>();
			for(int i = 0; i < jsonArray.length(); i++) {
				jList.add(jsonArray.getJSONObject(i));
			}
			
			JSONObject result = new JSONObject();
			boolean fin = false;
			Iterator<JSONObject> jIterator = jList.iterator();
			while(jIterator.hasNext() && !fin) {
				JSONObject aux = jIterator.next();
				if(Float.parseFloat(aux.get("codLineaStr").toString()) == linea){
					result = aux;
					fin = false;
				}
			}
			
			rd.close();
			return result.toString();
			
		} finally {
		}
	}
	
	@Operation(description = "Introduce un número de línea y recibe las paradas que hace el autobus de dicha linea", responses = {
			@ApiResponse(responseCode = "200", description = "Paradas encontradas"),
			@ApiResponse(responseCode = "500", description = "Paradas no encontradas o línea no existente") })
	@GetMapping(value="/mostrarParadasBus")
	public static String mostrarParadasBus(int linea) throws IOException{
		try {
			String url = "https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTLineasYParadas/lineasyparadas.geojson";
			BufferedReader rd = new BufferedReader(new InputStreamReader(new URL(url).openStream(), Charset.forName("UTF-8")));
			
			String jsonText = readAll(rd);
			JSONArray jsonArray = new JSONArray(jsonText);

			List<JSONObject> jList = new ArrayList<>();
			for(int i = 0; i < jsonArray.length(); i++) {
				jList.add(jsonArray.getJSONObject(i));
			}
			
			JSONObject lineaAux = new JSONObject();
			boolean fin = false;
			Iterator<JSONObject> jIterator = jList.iterator();
			while(jIterator.hasNext() && !fin) {
				JSONObject aux = jIterator.next();
				if(Integer.parseInt(aux.get("codLineaStr").toString()) == linea){
					lineaAux = aux;
					fin = false;
				}
			}
			
			List<JSONObject> paradasList = new ArrayList<>();
			for(int i = 0; i < lineaAux.getJSONArray("paradas").length(); i++) {
				paradasList.add(lineaAux.getJSONArray("paradas").getJSONObject(i));
			}
			
			String resultado = "Paradas: \n";
			for (JSONObject jsonObject : paradasList) {
				resultado += "Nombre: " + jsonObject.getJSONObject("parada").getString("nombreParada");
				resultado += ", direccion: " + jsonObject.getJSONObject("parada").getString("direccion") + ",\n";
			}
			
			rd.close();
			return resultado;
			
		} finally {
		}
	}
	
	@Operation(description = "Introduce latitud y longitud, devuelve las paradas de autobus más cercanas en un determinado radio. Ejemplo: latitud 36.737835, longitud -4.4222507, rango 0.005. Tarda un rato. ", responses = {
			@ApiResponse(responseCode = "200", description = "Paradas encontradas") })
	@GetMapping(value="/mostrarParadasBusCercana", produces =MediaType.APPLICATION_JSON_VALUE)
	public static String mostrarParadasBusCercana(float latitud, float longitud, float rango) throws IOException{
		try {
			String url = "https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTLineasYParadas/lineasyparadas.geojson";
			BufferedReader rd = new BufferedReader(new InputStreamReader(new URL(url).openStream(), Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray jsonArray = new JSONArray(jsonText);

			//Lista de lineas
			List<JSONObject> jList = new ArrayList<>();
			for(int i = 0; i < jsonArray.length(); i++) {
				jList.add(jsonArray.getJSONObject(i));
			}

			//Iterador de la lista de lineas
			Iterator<JSONObject> jIterator = jList.iterator();
			List<JSONObject> paradasList = new ArrayList<>();
			
			//Sobre cada linea
			while(jIterator.hasNext()) {
				JSONObject linea = jIterator.next();
				JSONArray arrayParadas = linea.getJSONArray("paradas");
				
				//Sobre la lista de paradas
				for(int i = 0; i < arrayParadas.length(); i++) {
					//Aux = objeto interno de paradas
					JSONObject aux = arrayParadas.getJSONObject(i);
					//Buscar parada
					float auxLat = aux.getJSONObject("parada").getFloat("latitud"), auxLong = aux.getJSONObject("parada").getFloat("longitud");		
					
					final int R = 6371; // Radius of the earth

				    double latDistance = Math.toRadians(auxLat - latitud);
				    double lonDistance = Math.toRadians(auxLong - longitud);
				    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				            + Math.cos(Math.toRadians(latitud)) * Math.cos(Math.toRadians(auxLat))
				            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
				    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
				    double distance = R * c * 1000; // convert to meters


				    distance = Math.pow(distance, 2);
					
					
					if(Math.sqrt(distance) < rango ) {
						arrayParadas.getJSONObject(i).put("linea", linea.get("codLineaStr"));
						paradasList.add(arrayParadas.getJSONObject(i));
					}
				}
			}

			JSONArray res = new JSONArray();
			for (JSONObject jsonObject : paradasList) {
				res.put(jsonObject);
			}
			
			
			
			rd.close();
			return res.toString();
			
		} finally {
		}
	}
	
	
}
