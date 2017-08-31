package dominio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String EL_LIBRO_ES_PALINDROMO = "los libros palíndromos solo se pueden utilizar en la biblioteca";
	
	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, String nombreUsuario) {
		Prestamo prestamo = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String dateFormat = sdf.format(date);
		if(esPalindromo(isbn)){
			throw new PrestamoException(EL_LIBRO_ES_PALINDROMO);
		}else{
			if(isbnSuma(isbn) > 30){
				
				Date  d1= new Date();
				String dateInString = sdf.format(d1);
				Calendar cal2 = Calendar.getInstance();
				
				try {
					cal2.setTime(sdf.parse(dateInString));
					cal2.add(Calendar.DAY_OF_YEAR, 15);
					Calendar cal = Calendar.getInstance();
					cal.setTime(sdf.parse(dateInString));
				while (cal.before(cal2) || cal.equals(cal2)) {
					  cal.add(Calendar.DATE, 1);
					  }
				
				if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
					cal.add(Calendar.DAY_OF_YEAR, 1);
				}
				
				String dateEntregaFormat = sdf.format(cal.getTime());
				Libro libro = this.repositorioLibro.obtenerPorIsbn(isbn);
				prestamo = new Prestamo(sdf.parse(dateFormat),libro, sdf.parse(dateEntregaFormat), nombreUsuario );
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn) == null){
					repositorioPrestamo.agregar(prestamo);
				}else{
					throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
				} 
				
			}else{
				Libro libro = this.repositorioLibro.obtenerPorIsbn(isbn);
				try {
					 prestamo = new Prestamo(sdf.parse(dateFormat),libro,null,nombreUsuario);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn) == null){
					repositorioPrestamo.agregar(prestamo);
				}else{
					throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
				}
			}
			
		}
		
		

	}

	public boolean esPrestado(String isbn) {
		if( repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn) == null){
			return false;
		}else{
			return true;
		}
	
	}
	
	public int isbnSuma(String isbn){
		int suma = 0;
		for (int x=0; x < isbn.length(); x++) {
			if (isbn.charAt(x) != ' '){
				if (Character.isDigit(isbn.charAt(x))) {
					 suma = suma + Character.getNumericValue(isbn.charAt(x));
					}
			}
		}
		return suma;
	}
	
	public boolean esPalindromo(String isbn){
		 boolean valor=true;
		    int i,ind;    
		    String isbn2="";
		    //quitamos los espacios
		    for (int x=0; x < isbn.length(); x++) {
		        if (isbn.charAt(x) != ' ')
		            isbn2 += isbn.charAt(x);
		    }
		    //volvemos a asignar variables
		    isbn=isbn2;    
		    ind=isbn.length();
		    //comparamos isbns
		    for (i= 0 ;i < (isbn.length()); i++){        
		       if (isbn.substring(i, i+1).equals(isbn.substring(ind - 1, ind)) == false ) {
		           //si una sola letra no corresponde no es un palindromo por tanto
		           //sale del ciclo con valor false
		            valor=false;
		            break;
		       }
		       ind--;
		    }
		    return valor;
	}

}
