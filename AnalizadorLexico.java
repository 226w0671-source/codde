package compilador;

import java.util.ArrayList;

public class AnalizadorLexico {
	
	private int indiceFila;
	private int indiceCaracter;
	private ArrayList<String> entrada;
	private Token tokenActual;
	private Token bufferErrores;
	
	public AnalizadorLexico() {
		indiceFila = 0;
		indiceCaracter = 0;
		tokenActual = new Token("",0,0);
		bufferErrores = new Token("",0,0);
	}
	
	public AnalizadorLexico(ArrayList<String>ingreso) {
		indiceFila = 0;
		indiceCaracter = 0;
		entrada = ingreso;
		tokenActual = new Token("",0,0);
		bufferErrores = new Token("",0,0);
	}
	
	/**
	 * Analiza si un determinado Token pertenece al lexico.
	 * Imprime un mensaje de error si no pertenece.
	 * 
	 * @param ingreso objeto Token a validar
	 * @return Tokens.ERROR si no forma parte del lexico, otros Tokens
	 * 			si pertenece
	 * */
	public Tokens analizarToken(Token ingreso) {
		Tokens t = Tokens.ERROR;
		String token = ingreso.getToken();
		if(token.startsWith("_")&&token.length()>1) {
			if(esLetra(token.charAt(1))) {
			boolean valido = false;
			if(token.length()==2)
				valido = true;
			else
				for(int i=2; i<token.length(); i++) {
					valido = validoParaIdentificador(token.charAt(i));
					if(!valido) break;
				}
			if(valido)
				t = Tokens.IDENTIFICADOR;
			}
		}
		if(esDigitoNumerico(token.charAt(0))) {
			if(token.contains(".")){
				String[] partes = new String[] {token.substring(0, token.indexOf(".")),
						token.substring(token.indexOf(".")+1)};
				boolean valido = false;
				for(int i=0; i<2; i++) {
					if(partes[i].isEmpty()) break;
					if(partes[i].length()==1)
						valido = esDigitoNumerico(partes[i].charAt(0));
					else
						for(int j=0; j<partes[i].length(); j++) {
							valido = esDigitoNumerico(partes[i].charAt(j));
							if(!valido) break;
						}
					if(!valido) break;
				}
				if(valido)
					t = Tokens.NUMERO_DOUBLE;
			}else {
				boolean valido = false;
				if(token.length()==1)
					valido = true;
				else
					for(int i=0; i<token.length(); i++) {
						valido = esDigitoNumerico(token.charAt(i));
						if(!valido) break;
					}
				if (valido)
					t = Tokens.NUMERO_LONG;
			}
		}
		switch(token) {
			case "long": t = Tokens.LONG; break;
			case "double": t = Tokens.DOUBLE; break;
			case "while": t = Tokens.WHILE; break;
			case "if": t = Tokens.IF; break;
			case "(": t = Tokens.PARIZQ; break;
			case ")": t = Tokens.PARDER; break;
			case "{": t = Tokens.LLAVEIZQ; break;
			case "}": t = Tokens.LLAVEDER; break;
			case ";": t = Tokens.PYCOMA; break;
			case ",": t = Tokens.COMA; break;
			case "write": t = Tokens.WRITE; break;
			case "read": t = Tokens.READ; break;
			case "else": t = Tokens.ELSE; break;
			case "break": t = Tokens.BREAK; break;
			case "continue": t = Tokens.CONTINUE; break;
			case "==": t = Tokens.IGUAL; break;
			case "!=": t = Tokens.DISTINTO; break;
			case "<>": t = Tokens.DISTINTO; break;
			case ">=": t = Tokens.MAYORIGUAL; break;
			case "<=": t = Tokens.MENORIGUAL; break;
			case "<": t = Tokens.MENOR; break;
			case ">": t = Tokens.MAYOR; break;
			case "+=": t = Tokens.MASNUMERO; break;
			case "-=": t = Tokens.MENOSNUMERO; break;
			case "*=": t = Tokens.PORNUMERO; break;
			case "/=": t = Tokens.DIVIDIDONUMERO; break;
			case "++": t = Tokens.MASUNO; break;
			case "--": t = Tokens.MENOSUNO; break;
			case "+": t = Tokens.MAS; break;
			case "-": t = Tokens.MENOS; break;
			case "*": t = Tokens.POR; break;
			case "/": t = Tokens.DIVISION; break;
			case "=": t = Tokens.ASIGNADOR; break;
			case "&&": t = Tokens.AND; break;
			case "||": t = Tokens.OR; break;
			case " ": t = Tokens.ESPACIO; break;
			case "\t": t = Tokens.ESPACIO; break;
		}
		if(t == Tokens.ERROR) 
		{
			if(token.length()>1)
			{
				System.err.print("Error en linea "+(ingreso.getIndiceFila()+1)+", caracter"
				+(ingreso.getIndiceComienzo()+1)+"= \""+token+"\" no pertenece al lexico,");
			}else {
				if(bufferErrores.getToken().length()==0)
				{
					bufferErrores.setIndiceFila(ingreso.getIndiceFila());
					bufferErrores.setIndiceComienzo(ingreso.getIndiceComienzo());
				}
				bufferErrores.setToken(bufferErrores.getToken()+token);
			}
		}
		else
		{
			if(bufferErrores.getToken().length()>0)
			{
				System.err.print("Error en linea "+(bufferErrores.getIndiceFila()+1)
						+", caracter"+(bufferErrores.getIndiceComienzo()+1)
						+": \""+bufferErrores.getToken()+"\"no pertenece al lexico.");
				bufferErrores = new Token("",0,0);
			}
			tokenActual.setTipo(t.toString());
		}
		return t;
		
	}
	private boolean validoParaIdentificador(char c)
	{
		boolean valido = false;
		if(c >= 46 && c <= 57)
			valido=true;
		if(c >= 65 && c <= 90)
			valido=true;
		if(c==95)
			valido=true;
		if(c >= 97 && c <= 122 )
			valido=true;
		return valido;
	}
	
	private boolean esLetra(char c)
	{
		boolean es = false;
		if(c >=65 && c <= 90)
			es=true;
		if(c >= 97 && c <= 122)
			es=true;
		return es;
			
	}
	private boolean esDigitoNumerico(char c)
	{
		boolean valido = false;
		if(c >=48 && c <= 57)
			valido=true;
		return valido;
	}
	
	/**
	 * Esta funcion arma el siguiente token del ingreso asignado al analizador y valida que
	 * sea del lexico.
	 * 
	 *  @rturn Tokena.Erroral si el token pertenece al lexico, otro Tokens caso contrario;
	 *  Tokens.EOT si ya se recorrio todo el ingreso.
	 *  @throws NullFointerException en caso de que no se haya ingresado al analizador
	 */
	public Tokens siguienteToken() throws NullPointerException
	{
		Tokens t = Tokens.ERROR;
		String linea;
		String token = "";
		if(indiceFila==entrada.size())
		{
			t = Tokens.EOT;
			tokenActual = new Token("",0,0);
		}
		else
		{
			linea = entrada.get(indiceFila);
			if(!linea.trim().isEmpty())
			{
				if(validoParaIdentificador(linea.charAt(indiceCaracter))) //id o numero
				{
					String aux = linea.substring(indiceCaracter);
					if(aux.startsWith("_")) //¿Identificador?
					{
						for(int i=0;i<aux.length(); i++)
							if(!validoParaIdentificador(aux.charAt(i)))
							{
								aux = aux.substring(0,i);
								break;
							}
						token = aux;
					}
					else {
						if(esLetra(aux.charAt(0))) //¿Palabra primitiva?
						{
							String[] primitivas = new String[] {"if","while","break","else",
									"continue","long","double","read","write"};
							for(int i=0;i<primitivas.length;i++)
								if(aux.startsWith(primitivas[i]))
									token = primitivas[i];
							}
						if(esDigitoNumerico(aux.charAt(0))) //¿Numero?
						{
							for(int i=0;i<aux.length();i++)
							{
								if(!esDigitoNumerico(aux.charAt(i))&&aux.charAt(i)!='.')
								{
									token = aux.substring(0,i);
									break;
								}
							}
						  }
						}
					}
				    else {
					if(linea.substring(indiceCaracter,indiceCaracter+1).equals(" ") ||
							linea.substring(indiceCaracter,indiceCaracter+1).equals("\t"))
					{
						token = linea.substring(indiceCaracter,indiceCaracter+1);
					}else {
						if(indiceCaracter<=linea.length()-1)
						{
							
							if(validoParaIdentificador(linea.charAt(indiceCaracter)))
						{
							t = siguienteToken();
						}else { //Si es un simbolo.
							char caux;
							try {
								String aux = linea.substring(indiceCaracter,indiceCaracter+2); //¿Es de dos caracteres?
								String validos = "++--+=-=*=/===<=>=!=<>&&||";
								for(int i=0;i<validos.length();i+=2)
								{
									token = aux.equals(validos.substring(i,i+2)) ? aux : "";
									if(!token.isEmpty()) break;
								}
								if(token.isEmpty()) // ¿Es de un caracter?
								{
									caux = linea.charAt(indiceCaracter);
									validos = "(){},.=<>+-*/;";
									for(int i=0;i<validos.length();i++)
									{
										token = caux==validos.charAt(i) ?
												String.valueOf(caux) : "";
										break;
									}
								}
							}catch(StringIndexOutOfBoundsException exc)
							{
								caux = linea.charAt(indiceCaracter); //codigo por si
								//es el ultimo caracter de la linea.
								String validos = "(){},.=<>+-*/;";
								for(int i=0;i<validos.length();i++)
								{
									token = caux==validos.charAt(i) ? String.valueOf(caux)
											:"";
									break;
								}
							}
						}
					} else {
						indiceCaracter=0;
						indiceFila++;
					}
				}
			}
			if(!token.isEmpty())
			{
				tokenActual = new Token(token,indiceFila,indiceCaracter);
				t = analizarToken(new Token(token,indiceFila,indiceCaracter));
				indiceCaracter += tokenActual.getToken().length();
				if(indiceCaracter>linea.length()-1)
				{
					indiceCaracter=0;
					indiceFila++;
				}
			}else {
				tokenActual = new Token(linea.substring(indiceCaracter,indiceCaracter+1),
						indiceFila,indiceCaracter);
				t = analizarToken(tokenActual);
				indiceCaracter++;
				if(indiceCaracter>linea.length()-1)
				{
					indiceCaracter=0;
					indiceFila++;
				}
			}
		}
		 else {
			if(indiceFila<entrada.size()-1)
			{
				indiceFila++;
				t = siguienteToken();
			}
			if(indiceFila==entrada.size())
			{
				t = Tokens.EOT;
				tokenActual = new Token("",0,0);
			}
		}
	  }
	  return t;
	}

public void setIngreso(ArrayList<String>ingreso) {
	entrada = ingreso;
	indiceCaracter = indiceFila = 0;
}

public Token getTokenActual() {
	return tokenActual;
 }
}