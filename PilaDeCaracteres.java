package compilador;

import java.util.ArrayList;

public class PilaDeCaracteres {
	private ArrayList<Character> pila;
	private int tope;
	public PilaDeCaracteres() 
	{
		pila = new ArrayList<>();
		tope=-1;
		agregarCaracter('z');
	}
	
	public boolean vacia() 
	{
		return tope==1;
	}
	
	public int getIndiceDeTope()
	{
		return tope;
	}
	/**
	 * Agrega un caracter al tope de la pila.
	 * @param c char
	 */
	public void agregarCaracter(char c)
	{
		pila.add(new Character(c));
		tope++;
	}
	/**
	 * ADVERTENCIA: Metodo destructivo.
	 * @return char
	 * @throws NullPointerException cuando la pila esta vacia.
	 */
	public char getCaracterDeTope() throws NullPointerException
	{
		if (vacia()) throw new NullPointerException("La pila esta vacia.");
		char c = pila.remove(tope).charValue();
		tope--;
		return c;
	}
	/**
	 * ADVERTENCIA: Metodo no destructivo.
	 * @return char
	 * @throws NullPointerException cuando la pila esta vacia.
	 */
	public char verTope() throws NullPointerException
	{
		if (vacia()) throw new NullPointerException("La pila esta vacia.");
		return pila.get(tope).charValue();
	}
}
