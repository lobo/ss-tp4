
	package ar.edu.itba.ss.tp4;

	import static java.util.stream.Collectors.toList;

	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.PrintStream;
	import java.io.PrintWriter;
	import java.util.ArrayList;
	import java.util.List;

	import com.fasterxml.jackson.core.JsonParseException;
	import com.fasterxml.jackson.databind.JsonMappingException;

	import ar.edu.itba.ss.tp4.core.HarmonicOscillator;
	import ar.edu.itba.ss.tp4.core.TimeDrivenSimulation;
	import ar.edu.itba.ss.tp4.integrators.BeemanIntegrator;

	import ar.edu.itba.ss.tp4.Configurator;
	import ar.edu.itba.ss.tp3.core.MassiveParticle;

	/**
	* <p>Punto de entrada principal de la simulación. Se encarga de
	* configurar los parámetros de operación y de desplegar el
	* sistema requerido.</p>
	*/

	public final class Main {

		protected static void simulateMode(final String mode)
				throws JsonParseException, JsonMappingException, IOException {

			System.out.println("A time-driven simulation...");
			final Configurator config = new Configurator();
			config.load();

			final Configuration c = config.getConfiguration();
			final int fps = c.getFPS();
			final double Δt = c.getDeltat();
			final String integration = c.getIntegration();
			final String inputFilename = "./resources/data/" + c.getInputfile();
			final PrintWriter pw = new PrintWriter(inputFilename);
			List<MassiveParticle> particles = new ArrayList<MassiveParticle>();

			if(mode.equals("HarmonicOscillator")) {
				particles = generateSingleParticle();
			} else {
				particles = generateParticles();
			}

			// Generate animation file
			// Va en animación!
			//generateAnimationFile(particles, 10, inputFilename);

			// Begin simulation:
			TimeDrivenSimulation.of(new HarmonicOscillator(particles))
				.with(new BeemanIntegrator())
				.spy((t, ps) -> {})
				.maxTime(5.0)
				.by(Δt)
				.build()
				.run();
		}

		protected static void animateMode() {
			System.out.println("A time-driven animation...");
		}

		private static final String SIMULATION_FILE = "./resources/data/simulation-file.txt";
		private static final String ANIMATION_FILE  = "./resources/data/animation-file.txt";

		public static void main(final String [] arguments)
				throws JsonParseException, JsonMappingException, IOException {
			
			final Configurator config = new Configurator();
			config.load();
			String system = config.getConfiguration().getSystem();
			String inputFilename = "./resources/data/" + config.getConfiguration().getInputfile().toString();
			
			PrintWriter pw = new PrintWriter(inputFilename);

			if (arguments.length == 0) {
				System.out.println("[FAIL] - No arguments passed. Try 'help' for more information.");
				exit(EXIT_CODE.NO_ARGS);
			} else if (arguments.length != 1) {
				System.out.println("[FAIL] - Wrong number of arguments. Try 'help' for more information.");
				exit(EXIT_CODE.BAD_N_ARGUMENTS);
			}
			switch (arguments[0]) {
				case "help":
					System.out.println("Time-Driven Simulation.\n" +
							"\tPossible modes: \n" +
							"\t\tsimulate\n" +
							"\t\tanimate\n");
					break;
				case "simulate":
					simulateMode(system);
					break;
				case "animate":
					animateMode();
					break;
				default:
					System.out.println("[FAIL] - Invalid argument. Try 'help' for more information.");
					exit(EXIT_CODE.BAD_ARGUMENT);
					break;
			}
		}
		
		private static List<MassiveParticle> generateParticles() {
			List<MassiveParticle> particles = new ArrayList<MassiveParticle>();
			
			particles.add(new MassiveParticle(0, 0, 6.955 * Math.pow(10, 5) *1000, 0, 0, 1.988544 * Math.pow(10, 30))); // Sun
			particles.add(new MassiveParticle(x, y, *1000, vx, vy, Math.pow(10, b)));     							    // Voyager
			
			particles.add(new MassiveParticle(4.934575904901209E+10, -3.332189926157666E+10, 2440*1000, 1.768273057783463E+04, 4.262789135023012E+04, 3.302 * Math.pow(10, 26)));  // Mercury
			particles.add(new MassiveParticle(3.343420365750898E+10, 1.025157427442533E+11, 6051.8*1000, -3.341288489965073E+04, 1.069158573237753E+04, 48.685 * Math.pow(10, 23))); 	// Venus

			particles.add(new MassiveParticle(x, y, *1000, vx, vy, Math.pow(10, b)));     // Earth
			particles.add(new MassiveParticle(x, y, *1000, vx, vy, Math.pow(10, b)));     // Mars
			particles.add(new MassiveParticle(x, y, *1000, vx, vy, Math.pow(10, b)));     // Jupiter
			particles.add(new MassiveParticle(x, y, *1000, vx, vy, Math.pow(10, b)));     // Saturn
			particles.add(new MassiveParticle(x, y, *1000, vx, vy, Math.pow(10, b)));     // Uranus
			particles.add(new MassiveParticle(x, y, *1000, vx, vy, Math.pow(10, b)));     // Neptune
			
			return particles;
		}
		
		private static List<MassiveParticle> generateSingleParticle() {
			List<MassiveParticle> particles = new ArrayList<MassiveParticle>();
			
			particles.add(new MassiveParticle(
					1,
					0,
					1,
					(-10/14),
					0,
					70
			));  // Single Particle
			
			return particles;
		}

		private static void generateAnimationFile(final List<MassiveParticle> particles, final int N, final String input_filename) throws FileNotFoundException {
			System.out.println("The output has been written into a file: " + input_filename);
			final String filename = input_filename;
			File file = new File(filename);
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			
			PrintStream oldOut = System.out;
			System.setOut(ps);
			
			System.out.println(N);
			particles.forEach((particle) -> {
				System.out.println( 
						particle.getX() + " " + 
						particle.getY() + " " +
						particle.getRadius() + " " +
						particle.getVx() + " " +
						particle.getVy() + " " +
						particle.getMass() + " "
						);
			});
			System.setOut(oldOut);
		}

		protected enum EXIT_CODE {

			NO_ARGS(-1), 
			BAD_N_ARGUMENTS(-2),
			BAD_ARGUMENT(-3);

			protected final int code;

			EXIT_CODE(final int code) {
				this.code = code;
			}

			public int getCode() {
				return code;
			}
		}

		protected static void exit(final EXIT_CODE exitCode) {
			System.exit(exitCode.getCode());
		}
	}
