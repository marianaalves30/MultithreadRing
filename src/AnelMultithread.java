
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AnelMultithread extends Thread {
	//Instancia a quantidade de threads
	private static AnelMultithread[] threads = new AnelMultithread[30];
	
	//Define o tamanho da mensagem
	
	private static char[] message = new char[80];
	
	//Utilizado para manipula��o de thread-safe, consigo manipular as threads de acordo com estes obj
	private static AtomicInteger currentThread;
	private static AtomicBoolean running;
	
	//Defino o id da minha thread corrente 
	private int id;

	static {
		//Esta classe cont�m m�todos embutidos para gerar n�meros aleat�rios.
		Random generator = new Random();
		
		//percorre o for com 80 caracteres para fazer a montagem do array de char (mensagem)
		for (int i = 0; i < message.length; i++) {
			
			//Se for �mpar gera um caractere aleat�rio ma�sculo, sen�o min�sculo
			if (generator.nextInt() % 2 == 1)
				message[i] = (char) ('A' + generator.nextInt(26));

			else
				message[i] = (char) ('a' + generator.nextInt(26));
		}

		currentThread = new AtomicInteger(0);
		running = new AtomicBoolean(true);
	}

	public AnelMultithread(int id) {
		//fa�o a defini��o do id da thread corrente
		this.id = id;
	}

	public void run() {
		//vari�vel para identificarmos o pr�x. caractere a ser alterado para mai�sculo
		boolean found;

		//Enquanto a thread
		while (running.get()) {
			try {
				//Comparo qual a thread 
				if (currentThread.get() % threads.length == this.id) {
					//Printo a mensagem
					System.out.println("Thread " + this.id + ":\t" + new String(message));

					found = false;

					//procura o pr�ximo caracter min�sculo
					for (int i = 0; !found && (i < message.length); i++) {
						found = (message[i] >= 'a') && (message[i] <= 'z');

						//Quando encontrar a letra na posi��o i da vari�vel mensagem seta ela como MA�SCULO
						if (found)
							message[i] += ('A' - 'a');
					}

					/*Caso n�o tenha encontrado o caractere seta o dado 
					  como falso encerrando a thread*/
					if (!found)
						running.set(false);

					//aguarda um segundo
					Thread.currentThread().sleep(1000);
					
					//Incrementa e recupera o valor da thread corrente
					currentThread.incrementAndGet();
				}
				
			}

			catch (InterruptedException e) {}
		}
	}

	public static void main(String[] args) {
		//Percore as 30 threads
		for (int i = 0; i < threads.length; i++) {
			//Instancia a thread com seu idg
			threads[i] = new AnelMultithread(i);
			
			//Inicia o processo
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			try {
				//Aguarda a thread morrer
				threads[i].join();
			}

			catch (InterruptedException e) {}
		}

		//Printa a mensagem final com todos os caracteres ma�sculos
		System.out.println("Mensagem Final:\t" + new String(message));
	}
}