package xabe.multiCore;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
	private ExecutorService executor;
	
	
	/**
	*Clase para terminar todos los hilos
	*/
	private void endExecutor(){
		executor.shutdown();
		try 
		{
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) 
			{
				executor.shutdownNow();
				if (!executor.awaitTermination(60, TimeUnit.SECONDS))
				{
					System.err.println("executor  no ha terminado");
				}
			}
		} catch (InterruptedException ie) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
	/**
	 * Crea un pool que va a ir creando threads conforme se vayan necesitando
	 * si hay threads libres, se ponen las tareas a procesarse dentro de dichos threads, y
	 * las tareas faltantes se procesan en threads nuevos
	 */
	public void newCachedThreadRool(){
		this.executor = Executors.newCachedThreadPool();
		for(int i=0; i < 5; i++){
			this.executor.execute(new Handler("Hilo_"+i));
		}
		endExecutor();
	}
	
	/**
	 * crea un pool con el número de threads indicado; dichos threads siempre estarán listos 
	 * para procesar tareas. El pool maneja también una cola de tareas; cada thread toma una
	 * tarea de la cola y la procesa, y al terminar toma otra tarea de la cola para procesarla, etc en un ciclo
	 */
	public void newFixedThreadPool(){
		this.executor = Executors.newFixedThreadPool(3);
		for(int i=0; i < 5; i++){
			this.executor.execute(new Handler("Hilo_"+i));
		}
		endExecutor();
	}
	
	/**
	 * crea un pool de un solo thread, con una cola en donde se ponen las tareas a procesar; 
	 * el thread toma una tarea de la cola, la procesa y toma la siguiente, en un ciclo. 
	 */
	public void newSingleThreadExecutor(){
		this.executor = Executors.newSingleThreadExecutor();
		for(int i=0; i < 5; i++){
			this.executor.execute(new Handler("Hilo_"+i));
		}
		endExecutor();
	}
	
	/**
	 * crea un pool que va a ejecutar tareas programadas cada cierto tiempo, ya sea una sola 
	 * vez o de manera repetitiva. Es parecido a un timer, pero con la diferencia de que puede 
	 * tener varios threads que irán realizando las tareas programadas conforme se desocupen. 
	 * También hay una versión de un solo thread.
	 */
	public void newScheduledThreadPool(){
		this.executor = Executors.newScheduledThreadPool(3);
		for(int i=0; i < 5; i++){
			this.executor.execute(new Handler("Hilo_"+i));
		}
		endExecutor();
	}
	
	/**
	 * Si queremos que despues de la excución nos devuelva algo tenemos que implemetar la 
	 * interfaz Callabe
	 */
	public void callableThread() throws ExecutionException, InterruptedException{
		this.executor = Executors.newCachedThreadPool();
		Collection<CallableResult> callableResults = new HashSet<CallableResult>();
		for(int i=0; i < 3; i++){
			callableResults.add(new CallableResult("Callable_"+i));
		}
		//solo uno
		Future<String> future = executor.submit((CallableResult)callableResults.toArray()[0]);
		//devuelve el resultado de una tarea completa correcamente
		String result = executor.invokeAny(callableResults);
		//Espera a que se termine todos los hilos
		List<Future<String>> futures = executor.invokeAll(callableResults);
		
		System.out.println("Resultado de obtener uno en concreto = " + future.get());
		System.out.println("Resultado de invocar uno cualquier = " + result);
		for(int i=0; i < futures.size(); i++){
			System.out.println("Resultado de invocar a todos = " + futures.get(i).get());
		}
	}

	public static void main(String[] args) {
		try
		{
			System.out.println("El numero de procesadores : "+Runtime.getRuntime().availableProcessors());
			new Main().callableThread();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
