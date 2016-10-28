package com.danielacedo.psp;

class Arbitro{
	
	private int numJugadores;
	private int turno; 		//A quien le toca
	private int objetivo; //número a adivinar
	private boolean acertado; //flag para determinar si hemos terminado
	public final int MAXIMO = 1000;
	
	private static final Object mutexTurno = new Object();
	private static final Object mutexAcertado = new Object();
	
	public Arbitro(int nJugadores){
		this.numJugadores = nJugadores;
		objetivo = 1 + (int) (MAXIMO * Math.random());
		acertado = false;
		turno = (int) (numJugadores * Math.random());
		
		System.out.println("El arbitro ha sacado el numero "+objetivo);
	}
	
	public boolean seAcabo(){
		synchronized(mutexAcertado){
			return acertado;
		}
	}
	
	public synchronized int esTurnoDe(){
		synchronized(mutexTurno){
			return turno;
		}
	}
	
	public void jugar(int jugador, int jugada){
		System.out.println("Es el turno de "+turno);
		
		if(jugador == turno){
			System.out.println("El jugador "+jugador+" ha sacado el numero "+jugada);
			
			if(jugada == objetivo){
				acertado = true;
				System.out.println("El jugador "+jugador+" ha ganado");
			}
			
			turno = (turno+1) % numJugadores;
			
		}else{
			System.out.println("El jugador "+jugador+" se ha colado");
		}
		
		System.out.println("Es el turno de: "+turno);
		System.out.println("Se acabo: "+acertado);
	}
	
}

class Jugador extends Thread{
	private int id;
	private Arbitro arbitro;
	
	public Jugador(int dorsal, Arbitro arbitro){
		this.id = dorsal;
		this.arbitro = arbitro;
	}
	
	@Override
	public void run(){
		while(!arbitro.seAcabo()){
			
			
			if(arbitro.esTurnoDe() == this.id){
				int jugada = 1 + (int) (arbitro.MAXIMO * Math.random());
				arbitro.jugar(id, jugada);
			}
		}
		
		System.out.println(id+": Se acabó");
	}
}

public class JugadoresArbitro {

	public static void main(String[] args) {
		int numJugadores = 10;
		
		Arbitro colina = new Arbitro(numJugadores);
		Jugador[] jugadores = new Jugador[numJugadores];
		
		for(int i = 0; i < numJugadores; i++){
			jugadores[i] = new Jugador(i, colina);
			jugadores[i].start();
		}
		
		for(int i = 0; i < numJugadores; i++){
			try {
				jugadores[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
