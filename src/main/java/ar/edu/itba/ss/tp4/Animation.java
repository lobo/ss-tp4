
	package ar.edu.itba.ss.tp4;

	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.Scanner;

	public class Animation {

		public static void generate(
				final String filename,
				final int FPS, final int N, final double Δt) {

			try (
				final Scanner input = new Scanner(new File(filename));
				final PrintWriter output = new PrintWriter(new FileWriter(filename + ".xyz"));
			) {
				int line = 0;
				int chunk = 0;
				while (input.hasNext()) {

					if (chunk % FPS == 0) {
						// Header...
						if (line % N == 0) {
							output.println(N);
							output.println(chunk * Δt);
						}
						// Chunk...
						for (int i = 0; i < N; ++i) {
							output.write(input.nextLine() + "\n");
						}
					}
					else {
						// Ignore...
						for (int i = 0; i < N; ++i) {
							input.nextLine();
						}
					}
					line += N;
					++chunk;
				}
				System.out.println("El archivo de animación fue generado con éxito.");
			}
			catch (final FileNotFoundException exception) {
				System.out.println(
					"El archivo de simulación no existe.");
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo crear el archivo de salida.");
			}
		}
	}
