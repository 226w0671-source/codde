package Compilador;

import java.io.IOException;

public class Compilador{

    public static void main(String[] args) {
        probarAnalizadorlexico("src/Compilador/codigofuente.fjl",false);
        probarAnalizadorSintactico("src/Compilador/codigofuente.fjl",true);
        probarAnalizadorSemantico("src/Compilador/codigofuente.fjl",false);
    }
    public static void probarAnalizadorlexico(String directorio, boolean mostrarSoloErrores) {
      try{
        CodigoFuente cf = new CodigoFuente(directorio);
        AnalizadorLexico al = new AnalizadorLexico(cf.getLineas());
        String bufferError = "";
        Tokens t;
        while ((t=al.siguienteToken()) !=Tokens.EOT) 
            if(!mostrarSoloErrores)
                if(t==Tokens.ERROR&&al.getTokenActual() .getToken() .length()==1)
                    buefferError+=al.getTokenActual().getToken();
                else {
                    if(bufferError.length()>0){
                        System.out.println("\""+bufferError+"\" es"+t);
                        bufferError = "";
                    }
                    System.out.println("\""+al.getTokenActual+"\" es ");

                }
      }catch(FileNotFoundException exc){
        System.err.println("no se encontro el archivo con el codigo fuente.");
      } catch(IOException EXC){
        System.out.println("hubo un error durante la lectura  del archivo con el codigo fuente.");
      }
    }
     public static void probarAnalizadorSintactico(String directorio, boolean mostrarArbol) {
			try {
				CodigoFuente cf = new CodigoFuente(directorio);
				AnalizadorLexico al = new AnalizadorLexico(cf.getLineas());
				AnalizadorSintactico as = new AnalizadorSintactico(al);
				if(mostrarArbol)
					System.out.println("\033[34m"+arsi.toString()+"\033[30m");
			}catch(FileNotFoundException exc) {
				System.err.println("No se encontro el archivo con el codigo fuente.");
			}catch(IOException exc) {
				System.err.println("Hubo un error durante la lectura del archivo con el codigo fuente.");
			}
	 }
	
    public static void probarAnalizadorSemantico(String directorio){
        try{
            CodigoFuente cf = new CodigoFuente(directorio);
            AnalizadorLexico al = new analizadorLexico(cf.getLineas());
            AnalizadorSintactico as = new AnalizadorSintactico(al);
			AnalizadorSemantico anse = new AnalizadorSemantico(as);
			anse.analizarSemantica();		
		}catch(FileNotFoundException exc) {
			System.err.println("No se encontro el archivo con el codigo fuente.");
		}catch(IOException exc) {
			System.err.println("Hubo un error durante la lectura del archivo con el codigo fuente.");
		}	
	}
	
}
        

    