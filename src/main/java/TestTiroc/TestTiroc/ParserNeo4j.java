package TestTiroc.TestTiroc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import net.sf.jsqlparser.JSQLParserException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * è il parser per query cypher. Non riuscivo a trovarne uno decente per java
 *
 */
//controllare i casi in cui manca la where e da aggiustare con spazi. vedi spezzatore query
public class ParserNeo4j {
	private List<String> listaProiezioni;
	private List<String> listaFrom;
	private List<List<String>> matriceWhere;
	
	
	public void spezza(String cypherQuery) throws JSQLParserException, FileNotFoundException{	
		//creo la lista from
		this.listaFrom = new LinkedList<>(); 
		File fileJSON = new File("/Users/micheletedesco1/Desktop/fileJSON.txt");
		Scanner scanner = new Scanner(fileJSON);
		//{'table' : 'persona', 'database' : 'postgerSQL', 'members':['persona.id', 'persona.nome', 'persona.scuola'] 'query' : 'SELECT * FROM persona WHERE 1=1'}
		//{'table' : 'scuola', 'database' : 'mongoDB', 'members':['scuola.id', 'scuola.nome'] }
		while (scanner.hasNextLine()) {			
			String line = scanner.nextLine();
			JsonParser parser = new JsonParser();
			JsonObject myJson = parser.parse(line).getAsJsonObject();
			String table = myJson.get("table").getAsString();
			if (cypherQuery.toLowerCase().contains(table.toLowerCase()))
				this.listaFrom.add(table);
		}

		scanner.close();
		System.out.println(listaFrom.toString());
		
		//creo la listaWhere
		this.matriceWhere = new LinkedList<>();
		String[] parti = cypherQuery.split(" WHERE ");
		String[] parti2 = parti[1].split(" RETURN ");
		
		String oggettoStringaWhere = parti2[0];
		String[] oggettiStatement = oggettoStringaWhere.split(" AND ");
    	for (int i=0; i<oggettiStatement.length; i++){
    		String[] oggettiStatementSeparati = oggettiStatement[i].split("=");
    		List<String> rigaMatrice = new LinkedList<>();
    		rigaMatrice.add(oggettiStatementSeparati[0]);
    		rigaMatrice.add(oggettiStatementSeparati[1]);
    		this.matriceWhere.add(rigaMatrice);			 		
    	} 	
    	
    	//creo la listaSelect
    	this.listaProiezioni = new LinkedList<>();
    	String oggettoStringaReturn = parti2[1];
        String[] partiReturn = oggettoStringaReturn.split("\\,");
        for (int i=0; i<partiReturn.length; i++){
        	this.listaProiezioni.add(partiReturn[i]);
        }
        
		
		
		
		
		
		
		
	}

	public List<String> getListaProiezioni() {
		return listaProiezioni;
	}

	public void setListaProiezioni(List<String> listaProiezioni) {
		this.listaProiezioni = listaProiezioni;
	}

	public List<String> getListaFrom() {
		return listaFrom;
	}

	public void setListaFrom(List<String> listaFrom) {
		this.listaFrom = listaFrom;
	}

	public List<List<String>> getMatriceWhere() {
		return matriceWhere;
	}

	public void setMatriceWhere(List<List<String>> matriceWhere) {
		this.matriceWhere = matriceWhere;
	}
	
	public static void main(String[] args) throws FileNotFoundException, JSQLParserException {
		String cypherQuery ="MATCH (persona:persona), (scuola:scuola) WHERE persona.scuola=scuola.id AND scuola.nome='caffe' RETURN persona.name";
		ParserNeo4j parserNeo4j = new ParserNeo4j();
		parserNeo4j.spezza(cypherQuery);
		System.out.println("lista proiezioni----->" + parserNeo4j.getListaProiezioni().toString());
		System.out.println("lista tabelle----->" + parserNeo4j.getListaFrom().toString());
		System.out.println("lista clausule where [attributo valore]---->" + parserNeo4j.getMatriceWhere().toString());
	}
}

