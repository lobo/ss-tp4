
	package ar.edu.itba.ss.tp4;

	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.Scanner;

	public class SmallSimulation {

		public static void generate(final String filename) {

			try (
				final Scanner input = new Scanner(new File(filename + ".xyz"));
				final PrintWriter output = new PrintWriter(new FileWriter(filename + ".small"));
			) {
				while (input.hasNext()) {

					// Ignorar header...
					final int N = Integer.parseInt(input.nextLine());
					input.nextLine();

					// Escribir chunk...
					for (int i = 0; i < N; ++i) {
						output.write(input.nextLine() + "\n");
					}
				}
				System.out.println("El archivo de simulación fue generado con éxito.");
			}
			catch (final FileNotFoundException exception) {
				System.out.println(
					"El archivo de animación no existe.");
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo crear el archivo de salida.");
			}
		}
	}
