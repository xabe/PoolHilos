package xabe.multiCore;

import java.util.Random;
import java.util.concurrent.Callable;

public class CallableResult implements Callable<String>{
	private String nombreHilo; 
	private Random generador;
	private int dormir;

	public CallableResult(String name) {
		this.nombreHilo = name;
		this.generador = new Random();
		this.dormir = generador.nextInt(5000);
	}
	
	public String call() throws Exception {
		try 
		{
			System.out.println("\t Voy a dormir "+this.dormir+" : "+nombreHilo);
			Thread.sleep(this.dormir);
			System.out.println("\t Ya he terminado de dormir : "+nombreHilo);
			return "terminado "+nombreHilo;
		}
		catch (InterruptedException exception) {
			return exception.getMessage();
		}
	}
}
