package compilador;

import java.util.ArrayList;

public class NodoSintactico {
	private final String nombre;
	private ArrayList<NodoSintactico> derivados;
	private final Token token;
	private final boolean esTerminal;
	/**
	 * Advertencia: usar solo en caso de que sea terminal.
	 * @param token Token
	 */
	public NodoSintactico (Token token) {
		this.nombre = token.getTipo();
		this.token = token;
		derivados = new ArrayList();
		esTerminal = true;
	}
	/**
	 * Advertencia: usar solo en caso de que sea no terminal.
	 * @param nombre String
	 */
	public NodoSintactico (String nombre) {
		this.nombre = nombre;
		token = null;
		derivados = new ArrayList();
		esTerminal = false;
	}
	/**
	 * Define todos los derivados (advertencia: es destructivo, es decir, si hay algo antes, lo borra).
	 * @param derivados ArrayList<NodoSintactico>
	 */
	public void setDerivados(ArrayList<NodoSintactico> derivados) 
	{
		this.derivados = derivados;
	}
	/**
	 * Agrega un derivado al final de la lista de derivados.
	 * @param derivado NodoSintactico
	 */
	public void agregarDerivado(NodoSintactico derivado)
	{
		derivados.add(derivado);
	}
	/**
	 * Devuelve todos los derivados.
	 * @return ArrayList<NodoSintactico>
	 */
	public ArrayList<NodoSintactico> getDerivados()
	{
		return derivados;
	}
	/**
	 * Regresa el NodoSintactico que este en la posicion index dentro de la lista de derivados.
	 * @param index int
	 * @return NodoSintactico
	 */
	public NodoSintactico getDerivado(int index)
	{
		return derivados.get(index);
	}
	/**
	 * Dado un nombre, devuelve un arreglo con todos los NodoSintactico de dicho nombre que deriven del actual NodoSintactico.
	 * @param nombre String
	 * @return ArrayList<NodoSintactico> por si hay mas de una coincidencia.
	 */
	public ArrayList<NodoSintactico> getDerivado(String nombre)
	{
		ArrayList<NodoSintactico> d = new ArrayList<NodoSintactico>();
		for(int i=0;i<derivados.size();i++)
			if(derivados.get(i).getNombre().equals(nombre))
				d.add(derivados.get(i));
		return d;
	}
	
	public String getNombre()
	{
		return nombre;
	}
	
	public Token getToken() {
		return token;
	}
	
	public boolean esTerminal() {
		return esTerminal;
	}
	
	public boolean tieneDerivados() 
	{
		return !derivados.isEmpty();
	}
}
