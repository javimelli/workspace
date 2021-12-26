package org.javimelli.ReactiveProject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

// Para que la aplicacion sea de consola o de linea de comandos implementamos CommandLineRunner
@SpringBootApplication
public class ReactiveProjectApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<String> nombres = Flux.just("Andres","Natalia","Darel","ALex","JaviPadre","Meme")
				.doOnNext(System.out::println);
		nombres.subscribe();
	}
}
