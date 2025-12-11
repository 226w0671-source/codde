package compilador;

import java.util.ArrayList;

/*
 * Este arbol se rige bajo la politica LL(1)
 * (se aconseja leer el libro sobre compiladores de kenneth
 * aunque hau recursividad para la lectura.
 */
public class ArbolSintactico {
	private NodoSintactico raiz;
	public ArbolSintactico() {
		raiz = new NodoSintactico("Secuencia");
	}
	/**
	 * Se debe agregar ni mas ni menos que todos los terminales en orden de una instruccion SI, MIENTRAS U ORACION
	 * definidos en sintaxis.txt<br>
	 * Este metodo emplea el recorrido raiz izquierda, derecha para armar y agregar ramas al arbol.
	 * 
	 * @param terminales ArrayList <token>
	 */
	public void agregarTerminales(ArrayList<Token> terminales) {
		NodoSintactico sub_raiz = buscarSecuenciaSinDerivados();
		switch(terminales.get(0).getTipo()) {
		case "DOUBLE":
			agregarDeclaracion(sub_raiz, terminales);
			break;
		case "IDENTIFICADOR":
			agregarAsignacion(sub_raiz, terminales);
			break;
		case "IF":
			agregarSi(sub_raiz, terminales);
			break;
		case "LONG":
			agregarDeclaracion(sub_raiz, terminales);
			break;
		case "PYCOMA":
			agregarInstruccionVacia(sub_raiz, terminales);
			break;
		case "READ":
			agregarLeer(sub_raiz, terminales);
			break;
		case "WHILE":
			agregarMientras(sub_raiz, terminales);
			break;
		case "WRITE":
			agregarEscribir(sub_raiz, terminales);
			break;
		}
		sub_raiz.agregarDerivado(new NodoSintactico("SECUENCIA"));
	}
	/**
	 * Buscar el parentesis que cierre el primer parentesis de apertura del arreglo.
	 * @param alt ArrayList<token>
	 * @return int indice de 0 a n-1 de arreglo alt
	 */
	private int buscarIndiceParentesisDeCierre(ArrayList<Token> alt) {
		int i_cierre = 0;
		int abiertos = 0;
		boolean abertura =false;
		for(int i=0; i<alt.size(); i++) {
			if(alt.get(i).getTipo().equals("PARIZQ")) {
				abiertos++;
				abertura = true;
			}
			if(abertura&&alt.get(i).getTipo().equals("PARDER"))
				abiertos--;
			if(abertura&&abiertos==0) {
				i_cierre = i;
				break;
			}
		}
		return i_cierre;
	}
	//---703A
	private NodoSintactico buscarSecuenciaSinDerivados() {
		NodoSintactico ns = raiz;
		while(ns.tieneDerivados())
			ns = ns.getDerivado("SECUENCIA").get(0);
		return ns;
	}
	private void agregarDeclaracion(NodoSintactico subsubraiz, ArrayList<Token> terminales) {
		ArrayList<NodoSintactico> auxiliar = new ArrayList();
		auxiliar.add(new NodoSintactico("ORACION"));
		auxiliar.add(new NodoSintactico("INSTRUCCION"));
		auxiliar.add(new NodoSintactico("DECLARACION"));
		auxiliar.add(new NodoSintactico("TIPO"));
		auxiliar.add(new NodoSintactico(terminales.get(0)));
		for(int i=1; i<auxiliar.size(); i++)
			auxiliar.get(i-1).agregarDerivado(auxiliar.get(i));
		if(terminales.get(2).getTipo().equals("COMA")||terminales.get(2).getTipo().equals("PYCOMA")) {
			for(int i=1; i<terminales.size()-1; i++) {
				if(i%2==1) {
					if(i==1) {
						auxiliar.add(new NodoSintactico("LISTADO"));
						auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(auxiliar.size()-1));
						auxiliar.add(new NodoSintactico(terminales.get(i)));
						auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
						if(i<terminales.size()-2) {
						i++;
						auxiliar.add(new NodoSintactico(terminales.get(i)));
						auxiliar.get(auxiliar.size()-3).agregarDerivado(auxiliar.get(auxiliar.size()-1));
						}
				
				}else {//////
				    auxiliar.add(new NodoSintactico("LISTADO"));
				    auxiliar.get(auxiliar.size()-4).agregarDerivado(subsubraiz);
				    auxiliar.add(new NodoSintactico(terminales.get(i)));
				    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
				    if(i<terminales.size()-2) {
				    	i++;
				    	auxiliar.add(new NodoSintactico(terminales.get(i)));
				    	auxiliar.get(auxiliar.size()-3).agregarDerivado(auxiliar.get(auxiliar.size()-1));
				       }
					}
				}
			}
		}
		if(terminales.get(2).getTipo().equals("Asignador")) {
			auxiliar.add(new NodoSintactico(terminales.get(1)));
			auxiliar.add(new NodoSintactico(terminales.get(2)));
			auxiliar.add(new NodoSintactico("TERMINO"));
			for(int i=auxiliar.size()-3; i<auxiliar.size(); i++)
				auxiliar.get(auxiliar.size()-6).agregarDerivado(auxiliar.get(i));
			agregarTermino(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(3, terminales.size()-1)));	
		}
		auxiliar.add(new NodoSintactico(terminales.get(terminales.size()-1)));
		auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
		subsubraiz.agregarDerivado(auxiliar.get(0));
	}
	
	private void agregarAsignacion(NodoSintactico subsubraiz, ArrayList<Token> terminales) {
		ArrayList<NodoSintactico> auxiliar = new ArrayList();
		auxiliar.add(new NodoSintactico("ORACION"));
		auxiliar.add(new NodoSintactico("INSTRUCCION"));
		auxiliar.add(new NodoSintactico("ASIGNACION"));
		auxiliar.add(new NodoSintactico(terminales.get(0)));
		for(int i=1; i<auxiliar.size(); i++) 
			auxiliar.get(i-1).agregarDerivado(auxiliar.get(i));
			if(terminales.get(1).getTipo().equals("MASUNO")||terminales.get(1).getTipo().equals("MENOSUNO")) {
				auxiliar.add(new NodoSintactico(terminales.get(1)));
				auxiliar.get(2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
			}else {
				auxiliar.add(new NodoSintactico(terminales.get(1)));
				auxiliar.add(new NodoSintactico("TERMINO"));
				for(int i=auxiliar.size()-2; i<auxiliar.size(); i++)
					auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(i));
				agregarTermino(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(2, terminales.size()-1)));
			}
			auxiliar.add(new NodoSintactico(terminales.get(terminales.size()-1)));
	        auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	        subsubraiz.agregarDerivado(auxiliar.get(0));
		}
		private void agregarLeer (NodoSintactico subsubraiz, ArrayList<Token> terminales){
	        ArrayList<NodoSintactico> auxiliar = new ArrayList();
	        auxiliar.add(new NodoSintactico ("ORACION"));
	        auxiliar.add(new NodoSintactico ("INSTRUCCION"));
	        auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	        auxiliar.add(new NodoSintactico ("LEER"));
	        auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	        auxiliar.add(new NodoSintactico(terminales.get(0)));
	        auxiliar.get(2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	        auxiliar.add(new NodoSintactico(terminales.get(1)));
	        auxiliar.get(2).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	        auxiliar.add(new NodoSintactico(terminales.get(2)));
	        auxiliar.get(0).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	        subsubraiz.agregarDerivado (auxiliar.get(0));
	    }
		private void agregarEscribir(NodoSintactico subsubraiz, ArrayList<Token> terminales){
	        ArrayList<NodoSintactico> auxiliar = new ArrayList();
	        auxiliar.add(new NodoSintactico ("ORACION"));
	        auxiliar.add(new NodoSintactico("INSTRUCCION"));
	        auxiliar.add(new NodoSintactico ("ESCRIBIR"));
	        auxiliar.add(new NodoSintactico(terminales.get(0)));
	        for(int i=1;i<auxiliar.size();i++)
	            auxiliar.get(i-1).agregarDerivado(auxiliar.get(i));
	        auxiliar.add(new NodoSintactico ("TERMINO"));
	        auxiliar.get(auxiliar.size()-3).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	        agregarTermino (auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(1,terminales.size()-1)));
	        auxiliar.add(new NodoSintactico(terminales.get(terminales.size()-1)));
	        auxiliar.get(0).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	        subsubraiz.agregarDerivado (auxiliar.get(0));
		}
		private void agregarSi(NodoSintactico subsubraiz, ArrayList<Token> terminales){
			ArrayList<NodoSintactico> auxiliar = new ArrayList();
	        int indiceFinCondicion, indiceCierreLlave, indiceProximoPYCOMA;
	        auxiliar.add(new NodoSintactico ("SI"));
	        auxiliar.add(new NodoSintactico(terminales.get(0)));
	        auxiliar.add(new NodoSintactico(terminales.get(1)));
	        auxiliar.add(new NodoSintactico ("CONDICION"));
	        for(int i=auxiliar.size()-3;i<auxiliar.size();i++)
	        	auxiliar.get(0).agregarDerivado(auxiliar.get(i));
	        indiceFinCondicion = buscarIndiceParentesisDeCierre (terminales);
	        ArrayList<Token> condicion = new ArrayList(terminales.subList(2, indiceFinCondicion));
	        agregarCondicion(auxiliar.get(auxiliar.size()-1), condicion);
	        auxiliar.add(new NodoSintactico(terminales.get(indiceFinCondicion)));
	        auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	        if(terminales.get(indiceFinCondicion+1).getTipo().equals("LLAVEIZQ")){
	            indiceCierreLlave = buscarIndiceLlaveDeCierre(new ArrayList(terminales.subList(indiceFinCondicion+1,terminales.size())))+indiceFinCondicion+1;
	            auxiliar.add(new NodoSintactico(terminales.get(indiceFinCondicion+1)));
	            auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	            auxiliar.add(new NodoSintactico("SECUENCIA"));
	            auxiliar.get(0).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	            if(indiceFinCondicion+1!=indiceCierreLlave-1){
	                agregarSecuencia(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(indiceFinCondicion+2, indiceCierreLlave)));
	                
	            }
	            auxiliar.add(new NodoSintactico(terminales.get(indiceCierreLlave)));
	            auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	         
	        if(indiceCierreLlave != terminales.size()-1)
            {
                auxiliar.add(new NodoSintactico ("SINO"));
                auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
                agregarSiNo(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(indiceCierreLlave+1, terminales.size())));
            }
	        } else {
	            indiceProximoPYCOMA = indiceFinCondicion+1+buscarIndiceProximoPYCOMA(new ArrayList(terminales.subList(indiceFinCondicion+1,terminales.size())));
	            switch(terminales.get(indiceFinCondicion+1).getTipo()){
	                case "DOUBLE":
	                    agregarDeclaracion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1, indiceProximoPYCOMA+1)));
	                    break;
	                case "IDENTIFICADOR":
	                    agregarAsignacion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1, indiceProximoPYCOMA+1)));
	                    break;
	                case "IF":
	                    agregarSi(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,indiceProximoPYCOMA+1)));
	                    break;
	                case "LONG":
	                    agregarDeclaracion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1, indiceProximoPYCOMA+1)));
	                    break;
	                case "READ":
	                    agregarLeer (auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1, indiceProximoPYCOMA+1)));
	                    break;
	                case "WHILE":
	                    agregarMientras (auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1, indiceProximoPYCOMA+1)));
	                    break;
	                case "WRITE":
	                    agregarEscribir(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1, indiceProximoPYCOMA+1)));
	                    break;
	            } 
	            if(indiceProximoPYCOMA != terminales.size()-1) {
	                {
	                    auxiliar.add(new NodoSintactico("SINO"));
	                    auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                    agregarSiNo(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(indiceProximoPYCOMA+1,
	                            terminales.size())));
	                }
	            }
	            subsubraiz.agregarDerivado(auxiliar.get(0));
	        }
	        }
	            private void agregarSiBucle(NodoSintactico subsubraiz,ArrayList<Token> terminales)
	            {
	                ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                int indiceFinCondicion,indiceCierreLlave,indiceProximoPYCOMA;
	                auxiliar.add(new NodoSintactico("SIBUCLE"));
	                auxiliar.add(new NodoSintactico(terminales.get(0)));
	                auxiliar.add(new NodoSintactico(terminales.get(1)));
	                auxiliar.add(new NodoSintactico("CONDICION"));
	                for(int i=auxiliar.size()-3;i<auxiliar.size();i++)
	                    auxiliar.get(0).agregarDerivado(auxiliar.get(i));
	                indiceFinCondicion = buscarIndiceParentesisDeCierre(terminales);
	                ArrayList<Token> condicion = new ArrayList(terminales.subList(2, indiceFinCondicion));
	                agregarCondicion(auxiliar.get(auxiliar.size()-1), condicion);
	                auxiliar.add(new NodoSintactico(terminales.get(indiceFinCondicion)));
	                auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                if(terminales.get(indiceFinCondicion+1).getTipo().equals("LLAVEIZO"))
	                {
	                    indiceCierreLlave = buscarIndiceLlaveDeCierre(new ArrayList(terminales.subList(indiceFinCondicion+1,
	                            terminales.size())))+indiceFinCondicion+1;
	                    auxiliar.add(new NodoSintactico(terminales.get(indiceFinCondicion+1)));
	                    auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                    auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                    auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                    if(indiceCierreLlave<terminales.size()-1)
	                    {
	                        auxiliar.add(new NodoSintactico("SINOBUCLE"));
	                        auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                        agregarSiNoBucle(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(indiceCierreLlave+1,
	                                terminales.size())));
	                    }
	                }else{
	                    indiceProximoPYCOMA=indiceFinCondicion+1+buscarIndiceProximoPYCOMA(new ArrayList(terminales.subList(
	                            indiceFinCondicion+1,terminales.size())));
	                    switch(terminales.get(indiceFinCondicion+1).getTipo())
	                    {
	                        case "BREAK":
	                            agregarControl(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "CONTINUE":
	                            agregarControl(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "DOUBLE":
	                            agregarDeclaracion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                        break;
	                    case "IDENTIFICADOR":
	                        agregarAsignacion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                indiceProximoPYCOMA+1)));
	                        break;
	                    case "IF":
	                        agregarSiBucle(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                indiceProximoPYCOMA+1)));
	                        break;
	                    case "LONG":
	                        agregarDeclaracion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                indiceProximoPYCOMA+1)));
	                        break;
	                    case "READ":
	                        agregarLeer(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                indiceProximoPYCOMA+1)));
	                        break;
	                    case "WHILE":
	                        agregarMientras(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                indiceProximoPYCOMA+1)));
	                        break;
	                    case "WRITE":
	                        agregarEscribir(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                indiceProximoPYCOMA+1)));
	                        break;
	                }
	                if(indiceProximoPYCOMA<terminales.size()-1)
	                {
	                    auxiliar.add(new NodoSintactico("SINOBUCLE"));
	                    auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                    agregarSiNoBucle(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(indiceProximoPYCOMA+1,
	                            terminales.size())));
	                }
	            }
	            subsubraiz.agregarDerivado(auxiliar.get(0));
	            }
	            private void agregarMientras(NodoSintactico subsubraiz,ArrayList<Token>terminales)
	            {
	            	ArrayList<NodoSintactico>auxiliar = new ArrayList();
	            	int indiceFinCondicion,indiceCierreLlave,indiceProximoPYCOMA;
	            	auxiliar.add(new NodoSintactico("MIENTRAS"));
	            	auxiliar.add(new NodoSintactico(terminales.get(0)));
	            	auxiliar.add(new NodoSintactico(terminales.get(1)));
	            	auxiliar.add(new NodoSintactico("CONDICION"));
	            	for(int i=auxiliar.size()-3;i<auxiliar.size();i++)
	            		auxiliar.get(0).agregarDerivado(auxiliar.get(i));
	            	indiceFinCondicion = buscarIndiceParentesisDeCierre(terminales);
	            	ArrayList<Token> condicion = new ArrayList(terminales.subList(2, indiceFinCondicion));
	            	agregarCondicion(auxiliar.get(auxiliar.size()-1), condicion);
	            	auxiliar.add(new NodoSintactico(terminales.get(indiceFinCondicion)));
	            	auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	            	if(terminales.get(indiceFinCondicion+1).getTipo().equals("LLAVEIZQ"))
	            	{
	            		indiceCierreLlave = buscarIndiceLlaveDeCierre(new ArrayList(terminales.subList(indiceFinCondicion+1,
	            				terminales.size())))+indiceFinCondicion+1;
	            		auxiliar.add(new NodoSintactico(terminales.get(indiceFinCondicion+1)));
	            		auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	            		auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	            		auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	            		if(indiceFinCondicion+1!=indiceCierreLlave-1)
	                    {
	                        agregarBloqueBucle(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(indiceFinCondicion+2,
	                                indiceCierreLlave)));
	                    }
	                    auxiliar.add(new NodoSintactico(terminales.get(indiceCierreLlave)));
	                    auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                }else{
	                    indiceProximoPYCOMA=buscarIndiceProximoPYCOMA(new ArrayList(terminales.subList(indiceFinCondicion+1,
	                            terminales.size())))+indiceFinCondicion+1;
	                    switch(terminales.get(indiceFinCondicion+1).getTipo())
	                    {
	                        case "BREAK":
	                            agregarControl(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "CONTINUE":
	                            agregarControl(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "DOUBLE":
	                            agregarDeclaracion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "IDENTIFICADOR":
	                            agregarAsignacion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "IF":
	                            if(terminales.get(terminales.size()-1).getTipo().equals("PYCOMA"))
	                            {
	                                agregarSiBucle(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                        indiceProximoPYCOMA+1)));
	                            }else{
	                                agregarSiBucle(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                        terminales.size())));
	                            }
	                            break;
	                        case "LONG":
	                            agregarDeclaracion(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "READ":
	                            agregarLeer(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "WHILE":
	                            agregarMientras(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                        case "WRITE":
	                            agregarEscribir(auxiliar.get(0), new ArrayList(terminales.subList(indiceFinCondicion+1,
	                                    indiceProximoPYCOMA+1)));
	                            break;
	                    }
	                }
	                subsubraiz.agregarDerivado(auxiliar.get(0));
	            }
	            /**
	             * Busca la llave que cierre la primera llave de apertura del arreglo.
	             *
	             * @param alt ArrayList<Token>
	             * @return int indice de 0 a n-1 del arreglo alt
	             */
	            private int buscarIndiceLlaveDeCierre(ArrayList<Token> alt)
	            {
	                int i_cierre = 0;
	                int abiertas = 0;
	                boolean abertura=false;
	                for(int i=0;i<alt.size();i++)
	                {
	                    if(alt.get(i).getTipo().equals("LLAVEIZO"))
	                    {
	                        abiertas++;
	                        abertura=true;
	                    }
	                    if(abertura&&alt.get(i).getTipo().equals("LLAVEDER"))
	                        abiertas--;
	                    if(abertura&&abiertas==0)
	                    {
	                        i_cierre = i;
	                        break;
	                    }
	                }
	                return i_cierre;
	            }
	            private void agregarInstruccionVacia(NodoSintactico subsubraiz,ArrayList<Token> terminales)
	            {
	                ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                auxiliar.add(new NodoSintactico("ORACION"));
	                auxiliar.add(new NodoSintactico("INSTRUCCION"));
	                auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                auxiliar.add(new NodoSintactico(terminales.get(0)));
	                auxiliar.get(0).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                subsubraiz.agregarDerivado(auxiliar.get(0));
	            }
	            private void agregarSecuencia(NodoSintactico subsubraiz,ArrayList<Token> terminales)
	            {
	                ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                ArrayList<Token> instruccion = new ArrayList();
	                int llavesAbiertas=0;
	                boolean primera_vez = true;
	                if(terminales.size()>0)
	                {
	                    for(int i=0;i<terminales.size();i++)
	                    {
	                        instruccion.add(terminales.get(i));
	                        if(llavesAbiertas==0&&instruccion.get(i).getTipo().equals("PYCOMA"))
	                        {
	                            switch(instruccion.get(0).getTipo())
	                            {
	                                case "DOUBLE":
	                                    if(primera_vez)
	                                    {
	                                        agregarDeclaracion(subsubraiz, instruccion);
	                                        primera_vez = false;
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        subsubraiz.agregarDerivado(auxiliar.get(0));
	                                        instruccion.clear();
	                                    }else{
	                                        agregarDeclaracion(auxiliar.get(auxiliar.size()-1), instruccion);
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                        instruccion.clear();
	                                    }
	                                    break;
	                                case "IDENTIFICADOR":
	                                    if(primera_vez)
	                                    {
	                                        agregarAsignacion(subsubraiz, instruccion);
	                                        primera_vez = false;
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        subsubraiz.agregarDerivado(auxiliar.get(0));
	                                        instruccion.clear();
	                                    }else{
	                                        agregarAsignacion(auxiliar.get(auxiliar.size()-1), instruccion);
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                        instruccion.clear();
	                                    }
	                                    break;
	                                case "IF":
	                                    if(i == terminales.size()-1) {
	                                        if(primera_vez) {
	                                            agregarSi(subsubraiz, instruccion);
	                                            primera_vez = false;
	                                            auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                            subsubraiz.agregarDerivado(auxiliar.get(0));
	                                            instruccion.clear();
	                                        } else {
	                                            agregarSi(auxiliar.get(auxiliar.size()-1), instruccion);
	                                            auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                            auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                            instruccion.clear();
	                                        }
	                                    } else {
	                                        if(!terminales.get(i+1).getTipo().equals("ELSE")) {
	                                            if(primera_vez) {
	                                                agregarSi(subsubraiz, instruccion);
	                                                primera_vez = false;
	                                                auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                instruccion.clear();
	                                            } else {
	                                                agregarSi(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                instruccion.clear();
	                                            }
	                                        }
	                                    }
	                                    break;
	                            case "LONG":
	                                if(primera_vez)
	                                    {
	                                        agregarDeclaracion(subsubraiz, instruccion);
	                                        primera_vez = false;
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        subsubraiz.agregarDerivado(auxiliar.get(0));
	                                        instruccion.clear();
	                                    }else{
	                                    agregarDeclaracion(auxiliar.get(auxiliar.size()-1), instruccion);
	                                    auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                    instruccion.clear();
	                                    }
	                                    break;
	                            case "PYCOMA":
	                                    if(primera_vez)
	                                    {
	                                        agregarInstruccionVacia(subsubraiz, instruccion);
	                                        primera_vez = false;
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(0));
	                                        instruccion.clear();
	                                    }else{
	                                        agregarInstruccionVacia(auxiliar.get(auxiliar.size()-1), instruccion);
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                        instruccion.clear();
	                                      }
	                                      break;
	                                    case"READ":
	                                        if(primera_vez)
	                                    {
	                                        agregarLeer(subsubraiz, instruccion);
	                                        primera_vez = false;
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        subsubraiz.agregarDerivado(auxiliar.get(0));
	                                        instruccion.clear();
	                                    }else{
	                                            agregarLeer(auxiliar.get(auxiliar.size()-1), instruccion);
	                                            auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                            auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                            instruccion.clear();
	                                    }
	                                        break;
	                                        case"WHILE":
	                                            if(primera_vez)
	                                    {
	                                        agregarMientras(subsubraiz, instruccion);
	                                        primera_vez = false;
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        subsubraiz.agregarDerivado(auxiliar.get(0));
	                                        instruccion.clear();
	                                    }else{
	                                         agregarMientras(auxiliar.get(auxiliar.size()-1), instruccion);
	                                         auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                         auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                         instruccion.clear();
	                                    }
	                                     break;
	                                 case"WRITE":
	                                     if(primera_vez)
	                                    {
	                                        agregarEscribir(subsubraiz, instruccion);
	                                        primera_vez = false;
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        subsubraiz.agregarDerivado(auxiliar.get(0));
	                                       instruccion.clear();
	                                    }else{
	                                        agregarEscribir(auxiliar.get(auxiliar.size()-1), instruccion);
	                                        auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                        auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                        instruccion.clear();
	                                    }
	                                    break;
	                                  }
	                                }
	                                 if(terminales.get(i).getTipo().equals("LLAVEIZQ"))
	                                    {
	                                        llavesAbiertas++;
	                                    }
	                                 if(terminales.get(i).getTipo().equals("LLAVEDER"))
	                                    {
	                                        llavesAbiertas--;
	                                        if(llavesAbiertas==0)
	                                         {
	                                              switch(instruccion.get(0).getTipo())
	                                              {
	                                                 case"IF":
	                                                      if(i==terminales.size()-1)
	                                                       {
	                                                           if(primera_vez)
	                                                             {
	                                                        	   agregarSi(subsubraiz,instruccion);
	                                                        	   primera_vez=false;
	                                                        	   auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                        	   subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                        	   instruccion.clear();
	                                                             }else{
	                                                            	 agregarSi(auxiliar.get(auxiliar.size()-1),instruccion);
	                                                            	 auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                            	 auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                            	 instruccion.clear();
	                                                             }
	                                                       }else{
	                                                    	   if(!terminales.get(i+1).getTipo().equals("ELSE"))
	                                                    	   {
	                                                    		   if(primera_vez)
	                                                    		   {
	                                                    			   agregarSi(subsubraiz,instruccion);
	                                                    			   	primera_vez=false;
	                                                    			   	auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                    			   	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                    			   	instruccion.clear();
	                                                    		   }else{
	                                                    			   agregarSi(auxiliar.get(auxiliar.size()-1),instruccion);
	                                                    			   auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                    			   auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                    			   instruccion.clear();
	                                                    		   }
	                                                    	   }
	                                                       }
	                                                      break;
	                                                 case"WHILE":	
	                                                	 if(primera_vez)
	                                                	 {
	                                                		 agregarMientras(subsubraiz,instruccion);
	                                                		 primera_vez=false;
	                                                		 auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                		 subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                		 instruccion.clear();
	                                                	 }else{
	                                                		 agregarMientras(auxiliar.get(auxiliar.size()-1),instruccion);
	                                                		 auxiliar.add(new NodoSintactico("SECUENCIA"));
	                                                		 auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                		 instruccion.clear();
	                                                	 }
	                                                	 break;
	                                             }
	                                          }
	                                       }
	                                    }
	                                    subsubraiz.agregarDerivado(auxiliar.get(0));
	                						}
	                               		}
	                            	private void agregarBloqueBucle(NodoSintactico subsubraiz,ArrayList<Token> terminales)
	                                    {
	                                        ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                                        ArrayList<Token> instruccion = new ArrayList();
	                                        int llavesAbiertas=0;
	                                        boolean primera_vez = true;
	                                        if(terminales.size()>0)
	                                    {
	                                            for(int i=0;i<terminales.size();i++)
	                                            {
	                                            	instruccion.add(terminales.get(i));
	                                            	if(llavesAbiertas==0&&terminales.get(i).getTipo().equals("PYCOMA"))
	                                            	{
	                                            		switch(instruccion.get(0).getTipo())
	                                            		{
	                                            		case"BREAK":
	                                            			if(primera_vez)
	                                            			{
	                                            				agregarControl(subsubraiz, instruccion);
	                                            				primera_vez = false;
	                                            				auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                            				subsubraiz.agregarDerivado(auxiliar.get(0));
	                                            				instruccion.clear();
	                                            			}else{
	                                                            agregarControl(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                            auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                            auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                            instruccion.clear();
	                                            			}
	                                            			break;
	                                                    	case "CONTINUE":
	                                                            if(primera_vez)
	                                                            {
	                                                            	agregarControl(subsubraiz, instruccion);
	                                                            	primera_vez = false;
	                                                            	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                            	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                            	instruccion.clear();
	                                                            }else{
	                                                                    agregarControl(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                    auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                    instruccion.clear();
	                                                            }
	                                                            break;
	                                                            case"DOUBLE":
	                                                                    if(primera_vez)
	                                                                    {
	                                                                    	agregarDeclaracion(subsubraiz, instruccion);
	                                                                    	primera_vez = false;
	                                                                    	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                    	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                    	instruccion.clear();
	                                                                    }else{
	                                                                            agregarDeclaracion(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                            auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                            auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                            instruccion.clear();
	                                                                    }
	                                                                    break;
	                                                                    case "IDENTIFICADOR":
	                                                                            if(primera_vez){
	                                                                                    agregarAsignacion(subsubraiz, instruccion);
	                                                                                    primera_vez = false;
	                                                                                    auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                    subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                    instruccion.clear();
	                                                                            }else {
	                                                                            	agregarAsignacion(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                            	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                            	auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                            	instruccion.clear();
	                                                                            }
	                                                                            break;
	                                                                            case "IF":
	                                                                                    if(primera_vez)
	                                                                                    {
	                                                                                    	agregarSiBucle(subsubraiz, instruccion);
	                                                                                    	primera_vez = false;
	                                                                                    	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                    	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                    	instruccion.clear();
	                                                                                    }else{
	                                                                                            agregarSiBucle(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                            auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                            auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                            instruccion.clear();
	                                                                                    }
	                                                                                    break;
	                                                                                    case "LONG":
	                                                                                            if(primera_vez)
	                                                                                            {
	                                                                                            	agregarDeclaracion(subsubraiz, instruccion);
	                                                                                            	primera_vez = false;
	                                                                                            	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                            	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                            	instruccion.clear();
	                                                                                            }else{
	                                                                                                    agregarDeclaracion(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                                    auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    instruccion.clear();
	                                                                                            }
	                                                                                            break;
	                                                                                            case"PYCOMA":
	                                                                                                    if(primera_vez)
	                                                                                                    {
	                                                                                                    	agregarInstruccionVacia(subsubraiz, instruccion);
	                                                                                                    	primera_vez = false;
	                                                                                                    	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                                    	instruccion.clear();
	                                                                                                    }else{
	                                                                                                            agregarInstruccionVacia(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                                            auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                            auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                            instruccion.clear();
	                                                                                                    }
	                                                                                                    break;
	                                                                                                    case"READ":
	                                                                                                            if(primera_vez)
	                                                                                                            {
	                                                                                                            	agregarLeer(subsubraiz, instruccion);
	                                                                                                            	primera_vez = false;
	                                                                                                            	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                            	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                                            	instruccion.clear();
	                                                                                                            }else{
	                                                                                                                    agregarLeer(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                                                    auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                                    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                                    instruccion.clear();
	                                                                                                            }
	                                                                                                            break;
	                                                                                                            case "WHILE":
	                                                                                                                    if(primera_vez)
	                                                                                                                    {
	                                                                                                                    	agregarMientras(subsubraiz, instruccion);
	                                                                                                                    	primera_vez = false;
	                                                                                                                    	auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                                    	subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                                                    	instruccion.clear();
	                                                                                                                    }else{
	                                                                                                    				    agregarMientras(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                                    				    auxiliar.add(new NodoSintactico ("BLOQUEBUCLE"));
	                                                                                                    				    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				    instruccion.clear();
	                                                                                                    				}
	                                                                                                    				break;
	                                                                                                    			    case "WRITE":
	                                                                                                    				if(primera_vez)
	                                                                                                    				{
	                                                                                                    				    agregarEscribir(subsubraiz, instruccion);
	                                                                                                    				    primera_vez = false;
	                                                                                                    				    auxiliar.add(new NodoSintactico ("BLOQUEBUCLE"));
	                                                                                                    				    subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                                    				    instruccion.clear();
	                                                                                                    				}else{
	                                                                                                    				    agregarEscribir(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                                    				    auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    				    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				    instruccion.clear();
	                                                                                                    				}
	                                                                                                    				break;
	                                                                                                    			    }
	                                                                                                    			}
	                                                                                                    			if(terminales.get(i).getTipo().equals("LLAVEIZQ"))
	                                                                                                    			{
	                                                                                                    			    llavesAbiertas++;
	                                                                                                    			}
	                                                                                                    			if(terminales.get(i).getTipo().equals("LLAVEDER"))
	                                                                                                    			{
	                                                                                                    			    llavesAbiertas--;
	                                                                                                    			    if(llavesAbiertas == 0)
	                                                                                                    			    {
	                                                                                                    				switch(instruccion.get(0).getTipo())
	                                                                                                    				{
	                                                                                                    				case "IF":
	                                                                                                    				    if(primera_vez)
	                                                                                                    				    {
	                                                                                                    						agregarSiBucle(subsubraiz, instruccion);
	                                                                                                    						primera_vez = false;
	                                                                                                    						auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    						subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                                    						instruccion.clear();
	                                                                                                    				    }else{
	                                                                                                    						agregarSiBucle(auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                                    						auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    						auxiliar.get(auxiliar.size()-2).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    						instruccion.clear();
	                                                                                                    				    }
	                                                                                                    				    break;
	                                                                                                    				case "WHILE":
	                                                                                                    				    if(primera_vez)
	                                                                                                    				    {
	                                                                                                    						agregarMientras (subsubraiz, instruccion);
	                                                                                                    						primera_vez = false;
	                                                                                                    						auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    						subsubraiz.agregarDerivado(auxiliar.get(0));
	                                                                                                    						instruccion.clear();
	                                                                                                    				    }else{
	                                                                                                    						agregarMientras (auxiliar.get(auxiliar.size()-1), instruccion);
	                                                                                                    						auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    						auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    						instruccion.clear();
	                                                                                                    				    }
	                                                                                                    				    break;
	                                                                                                    				}
	                                                                                                    			    }
	                                                                                                    			}
	                                                                                                    		    }
	                                                                                                    		}
	                                                                                                    		subsubraiz.agregarDerivado (auxiliar.get(0));
	                                                                                                    		}

	                                                                                                    		private void agregarControl (NodoSintactico subsubraiz, ArrayList<Token> terminales)
	                                                                                                    		{
	                                                                                                    		    ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                                                                                                    		    auxiliar.add(new NodoSintactico("CONTROL"));
	                                                                                                    		    auxiliar.add(new NodoSintactico(terminales.get(0)));
	                                                                                                    		    auxiliar.add(new NodoSintactico(terminales.get(1)));
	                                                                                                    		    for(int i=2;i>0;i--)
	                                                                                                    			auxiliar.get(auxiliar.size()-3).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    		    subsubraiz.agregarDerivado (auxiliar.get(0));
	                                                                                                    		}
	                                                                                                    		private int buscarIndiceProximoPYCOMA(ArrayList<Token> terminales)
	                                                                                                    		{
	                                                                                                    		    int ipyc=-1;
	                                                                                                    		    for(int i=0;i<terminales.size();i++)
	                                                                                                    			if(terminales.get(i).getTipo().equals("PYCOMA"))
	                                                                                                    			{
	                                                                                                    			    ipyc=i;
	                                                                                                    			    break;
	                                                                                                    			}
	                                                                                                    		    return ipyc;
	                                                                                                    		}
	                                                                                                    		private void agregarSiNo (NodoSintactico subsubraiz, ArrayList<Token> terminales)
	                                                                                                    		{
	                                                                                                    		    ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                                                                                                    		    auxiliar.add(new NodoSintactico (terminales.get(0)));
	                                                                                                    		    subsubraiz.agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    		    if(terminales.get(1).getTipo().equals("LLAVEIZQ"))
	                                                                                                    		    {
	                                                                                                    			auxiliar.add(new NodoSintactico(terminales.get(1)));
	                                                                                                    			subsubraiz.agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    			if(!terminales.get(2).getTipo().equals("LLAVEDER"))
	                                                                                                    			{
	                                                                                                    			    auxiliar.add(new NodoSintactico ("SECUENCIA"));
	                                                                                                    			    subsubraiz.agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    			    agregarSecuencia(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(2,terminales.size()-1)));
	                                                                                                    			}
	                                                                                                    			auxiliar.add(new NodoSintactico(terminales.get(terminales.size()-1)));
	                                                                                                    			subsubraiz.agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    		    }else{
	                                                                                                    			int indiceProximoPYCOMA = buscarIndiceProximoPYCOMA(terminales);
	                                                                                                    			switch(terminales.get(1).getTipo())
	                                                                                                    			{
	                                                                                                    			    case "DOUBLE":
	                                                                                                    				agregarDeclaracion(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "IDENTIFICADOR":
	                                                                                                    				agregarAsignacion(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "IF":
	                                                                                                    				agregarSi(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "LONG":
	                                                                                                    				agregarDeclaracion(subsubraiz, new ArrayList(terminales.subList(1,indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "PYCOMA":
	                                                                                                    				agregarInstruccionVacia(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "READ":
	                                                                                                    				agregarLeer (subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "WHILE":
	                                                                                                    				agregarMientras (subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "WRITE":
	                                                                                                    				agregarEscribir(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			}
	                                                                                                    		    }
	                                                                                                    		}
	                                                                                                    		
	                                                                                                    		private void agregarSiNoBucle (NodoSintactico subsubraiz, ArrayList<Token> terminales)
	                                                                                                    		{
	                                                                                                    		    ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                                                                                                    		    auxiliar.add(new NodoSintactico(terminales.get(0)));
	                                                                                                    		    subsubraiz.agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    		    if(terminales.get(1).getTipo().equals("LLAVEIZQ"))
	                                                                                                    		    {
	                                                                                                    			auxiliar.add(new NodoSintactico(terminales.get(1)));
	                                                                                                    			subsubraiz.agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    			if(!terminales.get(2).getTipo().equals("LLAVEDER"))
	                                                                                                    			{
	                                                                                                    			    auxiliar.add(new NodoSintactico("BLOQUEBUCLE"));
	                                                                                                    			    subsubraiz.agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    			    agregarBloqueBucle(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(2,terminales.size()-1)));
	                                                                                                    			}
	                                                                                                    			auxiliar.add(new NodoSintactico(terminales.get(terminales.size()-1)));
	                                                                                                    			subsubraiz.agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    		    }else{
	                                                                                                    			int indiceProximoPYCOMA = buscarIndiceProximoPYCOMA(terminales);
	                                                                                                    			switch(terminales.get(1).getTipo())
	                                                                                                    			{
	                                                                                                    			    case "BREAK":
	                                                                                                    				agregarControl(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "CONTINUE":
	                                                                                                    				agregarControl(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "DOUBLE":
	                                                                                                    				agregarDeclaracion(subsubraiz, new ArrayList(terminales.subList(1,indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "IDENTIFICADOR":
	                                                                                                    				agregarAsignacion(subsubraiz, new ArrayList(terminales.subList(1,indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "IF":
	                                                                                                    				agregarSiBucle(subsubraiz, new ArrayList(terminales.subList(1,indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "LONG":
	                                                                                                    				agregarDeclaracion (subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "PYCOMA":
	                                                                                                    				agregarInstruccionVacia(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "READ":
	                                                                                                    				agregarLeer (subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "WHILE":
	                                                                                                    				agregarMientras (subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			    case "WRITE":
	                                                                                                    				agregarEscribir(subsubraiz, new ArrayList(terminales.subList(1, indiceProximoPYCOMA+1)));
	                                                                                                    				break;
	                                                                                                    			}
	                                                                                                    		    }
	                                                                                                    		}
	                                                                                                    		private void agregarTermino (NodoSintactico subsubraiz, ArrayList<Token> terminales)
	                                                                                                    		{
	                                                                                                    		    ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                                                                                                    		    int i=0, j = terminales.size();
	                                                                                                    		    for(;i<j;i++)
	                                                                                                    		    {
	                                                                                                    			if(terminales.get(i).getTipo().equals("IDENTIFICADOR") || terminales.get(i).getTipo().equals("NUMERO_DOUBLE")
	                                                                                                    			||terminales.get(i).getTipo().equals("NUMERO_LONG"))
	                                                                                                    			{
	                                                                                                    			    if(i==0)
	                                                                                                    			    {
	                                                                                                    				auxiliar.add(new NodoSintactico("OPERANDO"));
	                                                                                                    				subsubraiz.agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    				auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    			    }else{
	                                                                                                    				int indice = buscarIndiceUltimaOcurrenciaNodoSintactico(auxiliar, "TERMINO");
	                                                                                                    				auxiliar.add(new NodoSintactico("OPERANDO"));
	                                                                                                    				auxiliar.get(indice).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    				auxiliar.add(new NodoSintactico(terminales.get(i))); 
	                                                                                                    				auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    			    }
	                                                                                                    			}else{
	                                                                                                    			    if(terminales.get(i).getTipo().equals("PARIZQ"))
	                                                                                                    			    {
	                                                                                                    				int cp = buscarIndiceParentesisDeCierre(new ArrayList(terminales.subList(i,terminales.size()))); 
	                                                                                                    				if(i==0)
	                                                                                                    				{
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				    auxiliar.add(new NodoSintactico ("TERMINO"));
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i+cp)));
	                                                                                                    				    for(int k=auxiliar.size()-3;k<auxiliar.size();k++)
	                                                                                                    					subsubraiz.agregarDerivado (auxiliar.get(k));
	                                                                                                    				}else{
	                                                                                                    				    for(int k=auxiliar.size()-3;k<auxiliar.size();k++)
	                                                                                                    				    	auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(k));
	                                                                                                    				}
	                                                                                                    			    }
	                                                                                                    			    if(terminales.get(i+1).getTipo().equals("PARDER"))
	                                                                                                    			    {
	                                                                                                    				if(i==0)
	                                                                                                    				{
	                                                                                                    				    auxiliar.add(new NodoSintactico ("OPERANDO"));
	                                                                                                    				    subsubraiz.agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				    auxiliar.get(auxiliar.size()-2).agregarDerivado (auxiliar.get(auxiliar.size()-1));
	                                                                                                    				}else{
	                                                                                                    				    int indice = buscarIndiceUltimaOcurrenciaNodoSintactico(auxiliar, "TERMINO");
	                                                                                                    				    auxiliar.add(new NodoSintactico("OPERANDO"));
	                                                                                                    				    auxiliar.get(indice).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				    auxiliar.get(auxiliar.size()-2).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				}
	                                                                                                    			    }else{
	                                                                                                    				if(i==0)
	                                                                                                    				{
	                                                                                                    				    auxiliar.add(new NodoSintactico ("OPERANDO"));
	                                                                                                    				    auxiliar.add(new NodoSintactico("OPERADOR"));
	                                                                                                    				    auxiliar.add(new NodoSintactico ("TERMINO"));
	                                                                                                    				    for(int k=auxiliar.size()-3;k<auxiliar.size();k++)
	                                                                                                    					subsubraiz.agregarDerivado(auxiliar.get(k));
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				    auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				    i++;
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				    auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				}else{
	                                                                                                    				    int indice = buscarIndiceUltimaOcurrenciaNodoSintactico (auxiliar, "TERMINO");
	                                                                                                    				    auxiliar.add(new NodoSintactico ("OPERANDO"));
	                                                                                                    				    auxiliar.add(new NodoSintactico ("OPERADOR"));
	                                                                                                    				    auxiliar.add(new NodoSintactico("TERMINO"));
	                                                                                                    				    for(int k=auxiliar.size()-3;k<auxiliar.size();k++)
	                                                                                                    					auxiliar.get(indice).agregarDerivado(auxiliar.get(k));
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				    auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				    i++;
	                                                                                                    				    auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    				    auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				}
	                                                                                                    			  }
	                                                                                                    			}
	                                                                                                    		  }
	                                                                                                    		   if(terminales.get(i).getTipo().equals("PARIZQ"))
	                                                                                                    		   {
	                                                                                                    			   auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    			   auxiliar.add(new NodoSintactico("TERMINO"));
	                                                                                                    			   auxiliar.add(new NodoSintactico(terminales.get(i+buscarIndiceParentesisDeCierre(new ArrayList(
	                                                                                                    					   	terminales.subList(i,terminales.size()-1))))));
	                                                                                                    			   if(i==0)
	                                                                                                    			   {
	                                                                                                    				   for(int k=auxiliar.size()-3;k<auxiliar.size();k++)
	                                                                                                    					   subsubraiz.agregarDerivado(auxiliar.get(k));
	                                                                                                    			   }else {
	                                                                                                    				   for(int k=auxiliar.size()-3;k<auxiliar.size();k++)
	                                                                                                    					   auxiliar.get(auxiliar.size()-4).agregarDerivado(auxiliar.get(k));
	                                                                                                    			   }
	                                                                                                    		   }
	                                                                                                    		}
																											/**    
																											* Devuelve el índice de la primera ocurrencia de Token tipo AND u OR.    
																											* @param alt ArrayList<Token>    
																											* @return int de 0 a n-1 si existe, -1 si no existe    
																											*/    
																											private int buscarIndiceOY(ArrayList<Token> alt)    
																											{    
																											int indice=-1;    
																											for(int i=0;i<alt.size();i++)    
																											if(alt.get(i).getTipo().equals("AND")||alt.get(i).getTipo().equals("OR"))    
																											{    
																											indice=i;    
																											break;    
																											}    
																											return indice;    
																											}    
																											private void agregarCondicionSimple(NodoSintactico subsubraiz,ArrayList<Token> terminales)    
																											{    
																											ArrayList<NodoSintactico> auxiliar = new ArrayList();    
																											int icomparador=-1;    
																											for(int i=0;i<terminales.size();i++)    
																											{    
																											switch(terminales.get(i).getTipo())    
																											{    
																											case "IGUAL": icomparador=i;    
																											break;    
																											case "MAYOR": icomparador=i;    
																											break;    
																											case "MAYORIGUAL": icomparador=i;    
																											break;    
																											case "MENOR": icomparador=i;    
																											break;    
																											case "MENORIGUAL": icomparador=i;    
																											break;    
																											}    
																											if(icomparador>=1) break;    
																											}    
																											auxiliar.add(new NodoSintactico("TERMINO"));    
																											agregarTermino(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(0, icomparador)));
																											auxiliar.add(new NodoSintactico("COMPARADOR"));    
																											auxiliar.add(new NodoSintactico("TERMINO"));    
																											agregarTermino(auxiliar.get(auxiliar.size()-1), new ArrayList(terminales.subList(icomparador+1,terminales.size())));    
																											for(int i=0;i<auxiliar.size();i++)    
																											subsubraiz.agregarDerivado(auxiliar.get(i));    
																											auxiliar.add(new NodoSintactico(terminales.get(icomparador)));
																											auxiliar.get(1).agregarDerivado(auxiliar.get(auxiliar.size()-1));    
																											}    
																											private int buscarIndiceUltimaOcurrenciaNodoSintactico(ArrayList<NodoSintactico> arreglo,String nombreNodo)    
																											{    
																											int ions = -1;    
																											for(int i=arreglo.size()-1;i>=0;i--)    
																											if(arreglo.get(i).getNombre().equals(nombreNodo))    
																											{    
																											ions=i;    
																											break;    
																											}    
																											return ions;    
																											}    
																											public NodoSintactico getRaiz()    
																											{    
																											return raiz;    
																											}    
																											
																											/**
																											* Genera un String que almacena todos los nodos.<br>
																											* Los nodos son representados con líneas bajo el patrón.<br>
																											* na padre ==>(na+1) hijo [(token)]<br>
																											* Dentro de dicho formato, na es un int que representa el nivel de profundidad del árbol para ese nodo
																											* (0 PARA RAÍZ, n-1 PARA la HOJA MÁS lejana). Además, token es el token literal en caso de que sea terminal.
																											*
																											* @return String
																											*/
																											@Override
																											public String toString()
																											{
																											    String arbol="",consecuenteToken;
																											    NodoSintactico antecedente,consecuente;
																											    int na = 0;
																											    antecedente = raiz;
																											    if(antecedente.tieneDerivados())
																											    {
																											    for(int i=0;i<antecedente.getDerivados().size();i++)
																											    {
																											    consecuente = antecedente.getDerivado(i);
																											    consecuenteToken = consecuente.esTerminal()?"("+consecuente.getToken().getToken()+")":"";
																											    arbol += na+" "+antecedente.getNombre()+" ==> "+(na+1)+" "+consecuente.getNombre()+" "+consecuenteToken+"\n";
																											    if(!consecuente.esTerminal())
																											    {
																											    arbol += subToString(consecuente,(na+1));
																											    }
																											    }
																											    arbol=arbol.substring(0,arbol.length()-1); //Para borrar el último salto de línea.
																											    }else{
																											    arbol = na+" SECUENCIA ==> "+(na+1)+" (lambda)";
																											    }
																											    return arbol;
																											}
																											
																											private String subToString(NodoSintactico antecedente,int na)
																											{
																											    String subString="",consecuenteToken;
																											    NodoSintactico consecuente;
																											    for(int i=0;i<antecedente.getDerivados().size();i++)
																											    {
																											    consecuente = antecedente.getDerivado(i);
																											    consecuenteToken = consecuente.esTerminal()?"("+consecuente.getToken().getToken()+")":"";
																											    subString += na+" "+antecedente.getNombre()+" ==> "+(na+1)+" "+consecuente.getNombre()+" "+consecuenteToken+"\n";
																											    if(!consecuente.esTerminal())
																											    {
																											    if(consecuente.tieneDerivados())
																											    {
																											    subString += subToString(consecuente,(na+1));
																											    }else{
																											    subString += na+" "+consecuente.getNombre()+" ==> "+(na+1)+" (lambda)\n";
																											    }
																											    }
																											    }
																											    return subString;
																											}
																											/**
																											* Determina el nivel de profundidad de un nodo a partir de la raíz por defecto del árbol.
																											*
																											* @param nodo NodoSintactico cuyo nivel de profundidad se desea determinar.
																											* @return int igual a 0 si coincide con la raíz o mayor.
																											
																											* @throws IllegalArgumentException cuando la raíz no tiene derivados o el argumento nodo no es derivado de la raíz
																											*/
																											public int getNivelProfundidadNodo(NodoSintactico nodo) throws IllegalArgumentException {
																												int nivel = 0;
																												if(!raiz.equals(nodo)) {
																													if (!raiz. tieneDerivados() )
																														throw new IllegalArgumentException("La raíz no tiene derivados.");
																													else {
																														nivel++;
																														for(int i=0; i<raiz.getDerivados().size(); i++) {
																															if(raiz.getDerivado(i).equals(nodo)) {
																																break;
																															}else {
																																if(raiz.tieneDerivados()) {
																																	nivel = subGetNivelProfundidadNodo(raiz.getDerivado(i), nodo, nivel);
																																}
																															}
																															if(i==raiz.getDerivados().size()-1&&nivel==1) {
																																throw new IllegalArgumentException("El argumento nodo no existe como derivado del nodo raiz.");
																															}
																														}
																													}
																												}
																												return nivel;
																											}
	                                                                                                    	/**
	                                                                                                    	 * Retorna el nivel de profundidad de un NodoSintactico dentro se su arbol.
	                                                                                                    	 * 
	                                                                                                    	 * @param nodoRaiz NodoSintactico que es la riz del arbol(o una subraiz)
	                                                                                                    	 * @param nodo NodoSintactico que es el que se desea conocer su  nivel
	                                                                                                    	 * @return int que es el nivel de profundidad del nodo (0 si coincide con la raiz)
	                                                                                                    	 * @throws IllegalArgumentException cuando el argumento nodoRaiz no tiene derivados y no es el buscador o cuando el 
	                                                                                                    	 * argumento nodo no es derivado de NodoRaiz
	                                                                                                    	 */
	                                                                                                    	public int getNivelProfundidadNodo(NodoSintactico nodoRaiz, NodoSintactico nodo) throws IllegalArgumentException
	                                                                                                    	{
	                                                                                                    		int nivel = 0;
	                                                                                                    		if(!nodoRaiz.equals(nodo))
	                                                                                                    		{
	                                                                                                    			if(!nodoRaiz.tieneDerivados())
	                                                                                                    				throw new IllegalArgumentException("El argumento nodoRaiz no tiene derivados.");
	                                                                                                    			else {
	                                                                                                    				nivel++;
	                                                                                                    				for(int i=0;i<nodoRaiz.getDerivados().size();i++)
	                                                                                                    				{
	                                                                                                    					if(nodoRaiz.getDerivado(i).equals(nodo))
	                                                                                                    					{
	                                                                                                    						break;
	                                                                                                    					}else {
	                                                                                                    						if(nodoRaiz.tieneDerivados())
	                                                                                                    						{
	                                                                                                    							nivel = subGetNivelProfundidadNodo(nodoRaiz.getDerivado(i),nodo,nivel);
	                                                                                                    						}
	                                                                                                    					}
	                                                                                                    					if(i==nodoRaiz.getDerivados().size()-1&&nivel==1)
	                                                                                                    					{
	                                                                                                    						throw new IllegalArgumentException("El argumento nodo no existe como derivado del argumento"
	                                                                                                    								+ "nodoRaiz.");
	                                                                                                    					}
	                                                                                                    				}
	                                                                                                    			}
	                                                                                                    		}
	                                                                                                    		return nivel;
	                                                                                                    	}
	                                                                                                    	private int subGetNivelProfundidadNodo(NodoSintactico nodoSubRaiz, NodoSintactico nodo, int nivel) {
	                                                                                                			nivel++;
	                                                                                                			for(int i=0; i<nodoSubRaiz.getDerivados().size(); i++) {
	                                                                                                				if(!nodoSubRaiz.getDerivado(i).equals(nodo)) {
	                                                                                                					if(nodoSubRaiz.getDerivado(i).tieneDerivados()) {
	                                                                                                						nivel = subGetNivelProfundidadNodo(nodoSubRaiz.getDerivado(i), nodo, nivel);
	                                                                                                					}
	                                                                                                				}
	                                                                                                			}
	                                                                                                			return nivel;
	                                                                                                		}
	                                                                                                    	/** Este metodo ayuda a encontrar un nodo no terminal de cierto nombre sin derivados, aquel cuya profundidad dentro 
	                                                                                                    	 * del arbol sea la mayor.<br>
	                                                                                                    	 * Si hay mas de un hijo sin derivados con la profundidad maxima, entonces retorna la primera ocurrencia,ignorando
	                                                                                                    	 * las siguientes.
	                                                                                                    	 * 
	                                                                                                    	 * @param arreglo ArrayList<NodoSintactico> que contenga los NodoSintactico de in if o un while
	                                                                                                    	 * @param subraiz NodoSintactico que es la riz de todos los elementos del arreglo
	                                                                                                    	 * @param nombreNodo String de 0 a n-1 del argumento arreglo
	                                                                                                    	 * 
	                                                                                                    	 * @throws IllegalArgumentException cuendo el nombre de un NodoSintactico terminal o se trata de un no terminal 
	                                                                                                    	 * sin derivados distintos al buscador 
	                                                                                                    	 */
	                                                                                                    	private int buscarIndiceHijoMasProfundoSinDerivados(ArrayList<NodoSintactico> arreglo, NodoSintactico subraiz, String nombreNodo) 
	                                                                                                				throws IllegalArgumentException {
	                                                                                                			int indice = -1;
	                                                                                                			for(int i=0; i<arreglo.size(); i++) {
	                                                                                                				if(arreglo.get(i).getNombre().equals(nombreNodo)) {
	                                                                                                					if(arreglo.get(i).esTerminal()) {
	                                                                                                						throw new IllegalArgumentException("El argumento nombreNodo especifica un nodo terminal.");
	                                                                                                					}
	                                                                                                					if(!arreglo.get(i).tieneDerivados()) {
	                                                                                                						if(indice==1) {
	                                                                                                							indice = i;
	                                                                                                						}else {
	                                                                                                							if(getNivelProfundidadNodo(subraiz, arreglo.get(i) )>getNivelProfundidadNodo(subraiz, arreglo.get(indice))) {
	                                                                                                								indice = i;
	                                                                                                							}
	                                                                                                						}
	                                                                                                					}
	                                                                                                				}
	                                                                                                			}
	                                                                                                			return indice;
	                                                                                                		}
	                                                                                                    	 private void agregarCondicion(NodoSintactico subsubraiz, ArrayList<Token> terminales)
	                                                                                                    	 {
	                                                                                                    		 int fin=terminales.size()-1;
	                                                                                                    		 ArrayList<NodoSintactico> auxiliar = new ArrayList();
	                                                                                                    		 boolean saltearFor = false;
	                                                                                                    		 int comenzarDesde = 1;
	                                                                                                    		 if(terminales.get(0).getTipo().equals("PARIZQ"))
	                                                                                                    		 {
	                                                                                                    			 int cp =buscarIndiceParentesisDeCierre(terminales);
	                                                                                                    			 if(cp==fin)
	                                                                                                    			 {
	                                                                                                    				 auxiliar.add(new NodoSintactico(terminales.get(0)));
	                                                                                                    				 auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    				 auxiliar.add(new NodoSintactico(terminales.get(cp)));
	                                                                                                    				 for(int i=0;i<auxiliar.size();i++)
	                                                                                                    					 subsubraiz.agregarDerivado(auxiliar.get(i));
	                                                                                                    				 fin--; 
	                                                                                                    			 }else {
	                                                                                                    				 if(terminales.get(cp+1).getTipo().equals("AND")|| terminales.get(cp+1).getTipo().equals("OR"))
	                                                                                                    				 {
	                                                                                                    					 auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    					 auxiliar.add(new NodoSintactico("OY"));
	                                                                                                    					 auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    					 for(int i=0;i<auxiliar.size();i++)
	                                                                                                    						 subsubraiz.agregarDerivado(auxiliar.get(i));
	                                                                                                    					 auxiliar.add(new NodoSintactico(terminales.get(0)));
	                                                                                                    					 auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    					 auxiliar.add(new NodoSintactico(terminales.get(cp)));
	                                                                                                    					 for(int i=auxiliar.size()-3;i<auxiliar.size();i++)
	                                                                                                    						 auxiliar.get(0).agregarDerivado(auxiliar.get(i));
	                                                                                                    					 auxiliar.add(new NodoSintactico(terminales.get(cp+1)));
	                                                                                                    					 auxiliar.get(1).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				 }
	                                                                                                    			 }
	                                                                                                    		 }else {
	                                                                                                    			 int ioy = buscarIndiceOY(terminales);
	                                                                                                    			 if(ioy==-1)
	                                                                                                    			 {
	                                                                                                    				 agregarCondicionSimple(subsubraiz, terminales);
	                                                                                                    				 saltearFor = true;
	                                                                                                    			 }else {
	                                                                                                    				 auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    				 auxiliar.add(new NodoSintactico("OY"));
	                                                                                                    				 auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    				 for(int i=0;i<auxiliar.size();i++)
	                                                                                                    					 subsubraiz.agregarDerivado(auxiliar.get(i));
	                                                                                                    				 agregarCondicionSimple(auxiliar.get(0), new ArrayList(terminales.subList(0, ioy)));
	                                                                                                    				 auxiliar.add(new NodoSintactico(terminales.get(ioy)));
	                                                                                                    				 auxiliar.get(1).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    				 comenzarDesde = ioy;
	                                                                                                    			 }
	                                                                                                    		 }
	                                                                                                    		 if(!saltearFor)
	                                                                                                    		 {
	                                                                                                    			 int pc=-1;
	                                                                                                    			 for(int i=comenzarDesde;i<terminales.size();i++)
	                                                                                                    			 {
	                                                                                                    				 String token = terminales.get(i).getTipo();
	                                                                                                   
	                                                                                                    	 if(token.equals("PARIZQ"))
	                                                                                                    	 {
	                                                                                                    	    pc = buscarIndiceParentesisDeCierre(new ArrayList(terminales.subList(i, terminales.size())))+i;
	                                                                                                    	    if(pc==fin)
	                                                                                                    	    {
	                                                                                                    	        int icsd = buscarIndiceHijoMasProfundoSinDerivados(auxiliar, subsubraiz, "CONDICION");
	                                                                                                    	        fin--;
	                                                                                                    	        auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    	        auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	        auxiliar.add(new NodoSintactico(terminales.get(pc)));
	                                                                                                    	        for(int j=auxiliar.size()-3;j<auxiliar.size();j++)
	                                                                                                    	            auxiliar.get(icsd).agregarDerivado(auxiliar.get(j));
	                                                                                                    	    }else{
	                                                                                                    	        if(fin<terminales.size()-1)
	                                                                                                    	        {
	                                                                                                    	            if(terminales.get(pc+1).getTipo().equals("AND")||terminales.get(pc+1).getTipo().equals("OR"))
	                                                                                                    	            {
	                                                                                                    	                int icsd = buscarIndiceHijoMasProfundoSinDerivados(auxiliar, subsubraiz, "CONDICION");
	                                                                                                    	                auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                auxiliar.add(new NodoSintactico("OY"));
	                                                                                                    	                auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                for(int j=auxiliar.size()-3;j<auxiliar.size();j++)
	                                                                                                    	                    auxiliar.get(icsd).agregarDerivado(auxiliar.get(j));
	                                                                                                    	                auxiliar.add(new NodoSintactico(terminales.get(i)));
	                                                                                                    	                auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                auxiliar.add(new NodoSintactico(terminales.get(pc)));
	                                                                                                    	                for(int j=auxiliar.size()-3;j<auxiliar.size();j++)
	                                                                                                    	                    auxiliar.get(auxiliar.size()-6).agregarDerivado(auxiliar.get(j));
	                                                                                                    	                auxiliar.add(new NodoSintactico(terminales.get(pc+1)));
	                                                                                                    	                auxiliar.get(auxiliar.size()-6).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    	            }
	                                                                                                    	        }
	                                                                                                    	    }
	                                                                                                    	}else{
	                                                                                                    	    if(!token.equals("AND")&&!token.equals("PARDER")&&!token.equals("OR"))
	                                                                                                    	    {
	                                                                                                    	        if(terminales.get(i-1).getTipo().equals("PARIZQ"))
	                                                                                                    	        {
	                                                                                                    	            pc = buscarIndiceParentesisDeCierre(new ArrayList(terminales.subList(i-1, terminales.size())))
	                                                                                                    	                    +i-1;
	                                                                                                    	            int ioy = buscarIndiceOY(new ArrayList(terminales.subList(i, terminales.size())));
	                                                                                                    	            ioy=ioy==-1?ioy:ioy+i;
	                                                                                                    	            int icsd = buscarIndiceHijoMasProfundoSinDerivados(auxiliar, subsubraiz, "CONDICION");
	                                                                                                    	            if(pc==fin)
	                                                                                                    	            {
	                                                                                                    	                if(ioy==-1)
	                                                                                                    	                {
	                                                                                                    	                    agregarCondicionSimple(auxiliar.get(icsd), new ArrayList(terminales.subList(i, pc)));
	                                                                                                    	                    i=pc;
	                                                                                                    	                }else{
	                                                                                                    	                    if(ioy<pc)
	                                                                                                    	                    {
	                                                                                                    	                        auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                        auxiliar.add(new NodoSintactico("OY"));
	                                                                                                    	                        auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                        for(int j=auxiliar.size()-3;j<auxiliar.size();j++)
	                                                                                                    	                            auxiliar.get(icsd).agregarDerivado(auxiliar.get(j));
	                                                                                                    	                        agregarCondicionSimple(auxiliar.get(auxiliar.size()-3), new ArrayList(
	                                                                                                    	                                terminales.subList(i, ioy)));
	                                                                                                    	                        auxiliar.add(new NodoSintactico(terminales.get(ioy)));
	                                                                                                    	                        auxiliar.get(auxiliar.size()-3).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    	                        i=ioy;
	                                                                                                    	                    }else{
	                                                                                                    	                        agregarCondicionSimple(auxiliar.get(auxiliar.size()-3), new ArrayList(
	                                                                                                    	                                terminales.subList(i, pc)));
	                                                                                                    	                        i=pc;
	                                                                                                    	                    }
	                                                                                                    	                 }
	                                                                                                    	            }else{
	                                                                                                    	                if(pc+1<terminales.size())
	                                                                                                    	                {
	                                                                                                    	                    if(terminales.get(pc+1).getTipo().equals("AND")||terminales.get(pc+1).getTipo()
	                                                                                                    	                            .equals("OR"))
	                                                                                                    	                    {
	                                                                                                    	                        if(ioy==-1)
	                                                                                                    	                         {
	                                                                                                    	                            agregarCondicionSimple(auxiliar.get(icsd), new ArrayList(
	                                                                                                    	                                    terminales.subList(i, pc)));
	                                                                                                    	                            i=pc;
	                                                                                                    	               
	                                                                                                    	                       }else{
	                                                                                                    	                             if(ioy<pc)
	                                                                                                    	                             {
	                                                                                                    	                                
	                                                                                                    	                                auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                                auxiliar.add(new NodoSintactico("OY"));
	                                                                                                    	                                auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                                for(int j=auxiliar.size()-3;j<auxiliar.size();j++)
	                                                                                                    	                                    auxiliar.get(icsd).agregarDerivado(auxiliar.get(j));
	                                                                                                    	                                agregarCondicionSimple(auxiliar.get(auxiliar.size()-3),
	                                                                                                    	                                    new ArrayList(terminales.subList(i, ioy)));
	                                                                                                    	                                auxiliar.add(new NodoSintactico(terminales.get(ioy)));
	                                                                                                    	                                auxiliar.get(auxiliar.size()-3).agregarDerivado(
	                                                                                                    	                                        auxiliar.get(auxiliar.size()-1));
	                                                                                                    	                                i=ioy;
	                                                                                                    	                            }else{
	                                                                                                    	                                agregarCondicionSimple(auxiliar.get(auxiliar.size()-3),
	                                                                                                    	                                        new ArrayList(terminales.subList(i, pc)));
	                                                                                                    	                                i=pc;
	                                                                                                    	                             }
	                                                                                                    	                         }
	                                                                                                    	                    }
	                                                                                                    	                
	                                                                                                    	                }else{
	                                                                                                    	                    if(ioy>=1&&ioy<pc)
	                                                                                                    	                    {
	                                                                                                    	                        auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                        auxiliar.add(new NodoSintactico("0Y"));
	                                                                                                    	                        auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                        for(int j=auxiliar.size()-3;j<auxiliar.size();j++)
	                                                                                                    	                            auxiliar.get(icsd).agregarDerivado(auxiliar.get(j));
	                                                                                                    	                        agregarCondicionSimple(auxiliar.get(auxiliar.size()-3),
	                                                                                                    	                                new ArrayList(terminales.subList(i, ioy)));
	                                                                                                    	                        auxiliar.add(new NodoSintactico(terminales.get(ioy)));
	                                                                                                    	                        auxiliar.get(auxiliar.size()-3).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    	                        i=ioy;
	                                                                                                    	                    }else{
	                                                                                                    	                        agregarCondicionSimple(auxiliar.get(icsd), new ArrayList(terminales.subList(i, pc)));
	                                                                                                    	                        i=pc;
	                                                                                                    	                    }
	                                                                                                    	                }
	                                                                                                    	            }
	                                                                                                    	        }else{
	                                                                                                    	          if(terminales.get(i-1).getTipo().equals("AND")||terminales.get(i-1).getTipo().equals("OR"))
	                                                                                                    	            {
	                                                                                                    	                int ioy = buscarIndiceOY(new ArrayList(terminales.subList(i, terminales.size())));
	                                                                                                    	                ioy=ioy==-1?ioy:ioy+i;

	                                                                                                    	                int icsd = buscarIndiceHijoMasProfundoSinDerivados(auxiliar, subsubraiz, "CONDICION");
	                                                                                                    	                if(ioy==-1)
	                                                                                                    	                {
	                                                                                                    	                    agregarCondicionSimple(auxiliar.get(icsd), new ArrayList(terminales.subList(i,
	                                                                                                    	                            terminales.size())));
	                                                                                                    	                    i = terminales.size()-1;
	                                                                                                    	                }else{
	                                                                                                    	                        if(ioy<pc)
	                                                                                                    	                        {
	                                                                                                    	                                    auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                                    auxiliar.add(new NodoSintactico("0Y"));
	                                                                                                    	                                    auxiliar.add(new NodoSintactico("CONDICION"));
	                                                                                                    	                                    for(int j=auxiliar.size()-3;j<auxiliar.size();j++)
	                                                                                                    	                                        auxiliar.get(icsd).agregarDerivado(auxiliar.get(j));
	                                                                                                    	                                    agregarCondicionSimple(auxiliar.get(auxiliar.size()-3), new ArrayList(
	                                                                                                    	                                            terminales.subList(i, ioy)));
	                                                                                                    	                                    auxiliar.add(new NodoSintactico(terminales.get(ioy)));
	                                                                                                    	                                    auxiliar.get(auxiliar.size()-3).agregarDerivado(auxiliar.get(auxiliar.size()-1));
	                                                                                                    	                                     i = ioy;
	                                                                                                    	                                }else{
	                                                                                                    	                                    if(pc>-1)
	                                                                                                    	                                    {
	                                                                                                    	                                        agregarCondicionSimple(auxiliar.get(icsd), new ArrayList(
	                                                                                                    	                                                terminales.subList(i, pc)));
	                                                                                                    	                                        i=pc;
	                                                                                                    	                                    }
	                                                                                                    	                                }
	                                                                                                    	                            }
	                                                                                                    	                         }
	                                                                                                    	        }
	                                                                                                    	    }
	                                                                                                    	}
	                                                                                                    			 }}
	                                                                                                    		 }
	                                                                                                    	
	                                                                                                    	/**
	                                                                                                    	* Recupera todas las ocurrencias de NodoSintactico bajo el nombre dado.<br>
	                                                                                                    	* Advertencia: se recurre a la lectura raíz izquierda derecha.
	                                                                                                    	*
	                                                                                                    	* @param nombre String
	                                                                                                    	* @return ArrayList<NodoSintactico> de todas las ocurrencias (el arreglo puede no contener elementos)
	                                                                                                    	*/
	                                                                                                    	public ArrayList<NodoSintactico> obtenerTodosLosDerivados(String nombre)
	                                                                                                    	{
	                                                                                                    	    ArrayList<NodoSintactico> alns = new ArrayList();
	                                                                                                    	    if(raiz.tieneDerivados())
	                                                                                                    	    {
	                                                                                                    	        for(int i=0;i<raiz.getDerivados().size();i++)
	                                                                                                    	        {
	                                                                                                    	            if(raiz.getDerivado(i).getNombre().equals(nombre))
	                                                                                                    	            {
	                                                                                                    	                alns.add(raiz.getDerivado(i));
	                                                                                                    	            }else{
	                                                                                                    	                if(raiz.getDerivado(i).tieneDerivados())
	                                                                                                    	                {
	                                                                                                    	                    try{
	                                                                                                    	                        alns.addAll(obtenerTodosLosDerivados(raiz.getDerivado(i),nombre));
	                                                                                                    	                    }catch(NullPointerException exc){}
	                                                                                                    	                }
	                                                                                                    	            }
	                                                                                                    	        }
	                                                                                                    	    }
	                                                                                                    	    return alns;
	                                                                                                    	}

	                                                                                                    	/**
	                                                                                                    	 * Busca todos los NodoSintactico con el nombre especificado que sean derivados del ModoSintactico especificado.
	                                                                                                    	 *
	                                                                                                    	 * @param subraiz NodoSintactico de ArbolSintactico a partir del cual se desea buscar derivados (el arreglo
	                                                                                                    	 * puede no contener elementos)
	                                                                                                    	 * @param nombre String
	                                                                                                    	 * @return ArrayList<NodoSintactico>
	                                                                                                    	 */
	                                                                                                    	public ArrayList<NodoSintactico> obtenerTodosLosDerivados(NodoSintactico subraiz,String nombre)
	                                                                                                    	{
	                                                                                                    	    ArrayList<NodoSintactico> alns = new ArrayList();
	                                                                                                    	    for(int i=0;i<subraiz.getDerivados().size();i++)
	                                                                                                    	    {
	                                                                                                    	        if(subraiz.getDerivado(i).getNombre().equals(nombre))
	                                                                                                    	        {
	                                                                                                    	            alns.add(subraiz.getDerivado(i));
	                                                                                                    	            if(subraiz.getDerivado(i).tieneDerivados())
	                                                                                                    	            {
	                                                                                                    	                try{
	                                                                                                    	                    alns.addAll(obtenerTodosLosDerivados(subraiz.getDerivado(i),nombre));
	                                                                                                    	                }catch(NullPointerException exc){}
	                                                                                                    	            }
	                                                                                                    	        }else{
	                                                                                                    	            if(subraiz.getDerivado(i).tieneDerivados())
	                                                                                                    	            {
	                                                                                                    	                try{
	                                                                                                    	                    alns.addAll(obtenerTodosLosDerivados(subraiz.getDerivado(i),nombre));
	                                                                                                    	                }catch(NullPointerException exc){}
	                                                                                                    	            }
	                                                                                                    	        }
	                                                                                                    	    }
	                                                                                                    	    return alns;
	                                                                                                    	}
	                                                                                                    	/**
	                                                                                                    	 * Obtiene los nodos SECUENCIA (o RLOQUERDICE) enlazados a inicioSecuencia y de esa obtención busca los
	                                                                                                    	 * derivados inmediatos que tengan el nombre buscado.
	                                                                                                    	 *
	                                                                                                    	 * @param inicioSecuencia ModoSintactico que comience la secuencia
	                                                                                                    	 * @param buscado String con el nombre de ModoSintactico
	                                                                                                    	 * @return ArrayList<NodoSintactico> con las ocurrencias de NodoSintactico halladas
	                                                                                                    	 */
	                                                                                                    	public ArrayList<NodoSintactico> obtenerDerivadosDirectosDeUnaSecuencia(NodoSintactico inicioSecuencia,String buscado)
	                                                                                                    	{
	                                                                                                    	    ArrayList<NodoSintactico> secuencia = new ArrayList();
	                                                                                                    	    ArrayList<NodoSintactico> encontrados = new ArrayList();
	                                                                                                    	    secuencia.add(inicioSecuencia);
	                                                                                                    	    while(secuencia.get(secuencia.size()-1).tieneDerivados())
	                                                                                                    	    {
	                                                                                                    	        if(secuencia.get(secuencia.size()-1).getDerivados().size()>1)
	                                                                                                    	            secuencia.add(secuencia.get(secuencia.size()-1).getDerivado(1));
	                                                                                                    	        else break;
	                                                                                                    	    }
	                                                                                                    	    for(int i=0;i<secuencia.size();i++)
	                                                                                                    	        if(secuencia.get(i).tieneDerivados())
	                                                                                                    	        {
	                                                                                                    	            if(secuencia.get(i).getDerivado(0).getNombre().equals(buscado))
	                                                                                                    	                encontrados.add(secuencia.get(i).getDerivado(0));
	                                                                                                    	            else
	                                                                                                    	                if(secuencia.get(i).getDerivado(0).getNombre().equals("OBACION"))
	                                                                                                    	                    if(secuencia.get(i).getDerivado(0).getDerivado(0).getDerivado(0).getNombre().equals(buscado))
	                                                                                                    	                    encontrados.add(secuencia.get(i).getDerivado(0).getDerivado(0).getDerivado(0));
	                                                                                                    	        }
	                                                                                                    	    return encontrados;
	                                                                                                    	}
	                                                                                                    	/**
	                                                                                                    	 * Esta función devuelve los nodos Secuencia de un árbol o una rama en un vector lineal siempre que estén vinculados a directamente
	                                                                                                    	  a la raíz o subraíz.
	                                                                                                    	  *
	                                                                                                    	 * @param inicioSecuencia NodoSintactico que idealmente sea la raíz o subraíz de la secuencia
	                                                                                                    	  * @return ArrayList<NodoSintactico>
	                                                                                                    	  */
	                                                                                                    	public ArrayList<NodoSintactico> obtenerSecuencia(NodoSintactico inicioSecuencia)
	                                                                                                    	{
	                                                                                                    	    ArrayList<NodoSintactico> secuencia = new ArrayList();
	                                                                                                    	    secuencia.add(inicioSecuencia);
	                                                                                                    	    while(secuencia.get(secuencia.size()-1).tieneDerivados())
	                                                                                                    	    {
	                                                                                                    	        if(secuencia.get(secuencia.size()-1).getDerivados().size()>1)
	                                                                                                    	            secuencia.add(secuencia.get(secuencia.size()-1).getDerivado(1));
	                                                                                                    	        else break;
	                                                                                                    	    }
	                                                                                                    	    return secuencia;
	                                                                                                    	}
	                                                                                                    	/*
	                                                                                                    	 * Devuelve TODOS los nodos terminales derivados de un nodo sintáctico.
	                                                                                                    	 *
	                                                                                                    	 * @param subraíz NodoSintactico del cual se quieren averiguar todos los derivados no terminales
	                                                                                                    	  * @return ArrayList<NodoSintactico> que puede ser vacía.
	                                                                                                    	 */
	                                                                                                    	public ArrayList<NodoSintactico> obtenerTodosLosTerminalesHijos(NodoSintactico subraiz)
	                                                                                                    	{
	                                                                                                    	    ArrayList<NodoSintactico> retorno = new ArrayList();
	                                                                                                    	    if(subraiz.esTerminal())
	                                                                                                    	    {
	                                                                                                    	        retorno.add(subraiz);
	                                                                                                    	    }else{
	                                                                                                    	        for(int i=0;i<subraiz.getDerivados().size();i++)
	                                                                                                    	        {
	                                                                                                    	            if(subraiz.getDerivado(i).esTerminal())
	                                                                                                    	            {
	                                                                                                    	                retorno.add(subraiz.getDerivado(i));
	                                                                                                    	            }else{
	                                                                                                    	                retorno.addAll(obtenerTodosLosTerminalesHijos(subraiz.getDerivado(i)));
	                                                                                                    	                }
	                                                                                                    	            }
	                                                                                                    	        }
	                                                                                                    	        return retorno;
	                                                                                                    	    }
	                                                                                                    	}