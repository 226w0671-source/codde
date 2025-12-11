package compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CodigoFuente {

    private ArrayList<String> lineas;

    public CodigoFuente(String directorio) throws FileNotFoundException, IOException{
    lineas = new ArrayList();
    File archivo = new File (directorio);
    FileReader lector = new FileReader(archivo);
    BufferedReader buffer = new BufferedReader(lector);
    String linea;
    while((linea=buffer.readLine))
        lineas.add(linea);
	buffer.close();
	lector.close();
}

public ArrayList<String>getLienas(){
	return lineas;
}
}

    