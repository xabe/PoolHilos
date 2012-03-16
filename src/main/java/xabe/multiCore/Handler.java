package xabe.multiCore;

import java.util.Random;

public class Handler implements Runnable {
	private String nombreHilo; 
	private Random generador;
	private int dormir;

	public Handler(String name) {
		this.nombreHilo = name;
		this.generador = new Random();
		this.dormir = generador.nextInt(5000);
	}

	public void run() {
		try 
		{
			System.out.println("\t Voy a dormir "+this.dormir+" : "+nombreHilo);
			Thread.sleep(this.dormir);
		}
		catch (InterruptedException exception) {
			exception.printStackTrace();
		}
		System.out.println("\t Ya he terminado de dormir : "+nombreHilo);
	} 
}
