package compilador;

public class Variable {
	private final Token identificador;
	private final Token tipo;
	private NodoSintactico valor;
	public Variable(Token tipo, Token identificador) 
	{
		this.identificador = identificador;
		this.tipo = tipo;
	}
	public void setValor(NodoSintactico valor) 
	{
		this.valor = valor;
	}
	
	public Token getIdentificador() {
		return identificador;
	
	}
	public Token getTipo() {
		return tipo;
	}
	
	public NodoSintactico getValor() {
		return valor;
	}
	
	public boolean tieneValor()
	{
		boolean tiene = false;
		try {
			valor.getNombre();
			tiene = true;
		}catch(NullPointerException exc) {}
		return tiene;
	}
}
