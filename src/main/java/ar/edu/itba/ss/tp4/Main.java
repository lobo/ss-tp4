
	package ar.edu.itba.ss.tp4;

	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;

	import com.fasterxml.jackson.core.JsonParseException;
	import com.fasterxml.jackson.databind.JsonMappingException;

	import ar.edu.itba.ss.tp4.core.TimeDrivenSimulation;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.fields.GravitationalField;
	import ar.edu.itba.ss.tp4.fields.HarmonicOscillator;
	import ar.edu.itba.ss.tp4.integrators.BeemanIntegrator;
	import ar.edu.itba.ss.tp4.integrators.GearIntegrator;
	import ar.edu.itba.ss.tp4.integrators.VelocityVerlet;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;
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

			final Configurator configurator = new Configurator();
			configurator.load();
			final Configuration config = configurator.getConfiguration();
			final OutputSimulationFile output = OutputSimulationFile.getInstace(config.getOutput());
			final ForceField<MassiveParticle> force = mode.equals("HarmonicOscillator")?
					new HarmonicOscillator() :
					new GravitationalField();
			final List<MassiveParticle> state = mode.equals("HarmonicOscillator")?
					generateSingleParticle() :
					generateParticles();

			Integrator<MassiveParticle> integrator;
			switch (config.getIntegrator()) {
				case "VelocityVerlet" : {
					integrator = VelocityVerlet.of(force)
							.withInitial(state)
							.build();
					break;
				}
				case "Beeman" : {
					integrator = BeemanIntegrator.of(force)
							.withInitial(state)
							.build();
					break;
				}
				case "Gear" : {
					integrator = GearIntegrator.of(force)
							.Δt(config.getDeltat())
							.withInitial(state)
							.build();
					break;
				}
				default : {
					throw new IllegalArgumentException(
						"El integrador es inválido.");
				}
			}

			// Begin simulation:
			TimeDrivenSimulation.of(integrator)
				.spy(output::write)
				.maxTime(config.getMaxtime())
				.by(config.getDeltat())
				.build()
				.run();

			output.close();
		}
		


		protected static void animateMode(final String mode) throws JsonParseException, JsonMappingException, IOException {
			System.out.println("A time-driven animation...");
			// Acá usás FPS...
			
			final Configurator configurator = new Configurator();
			configurator.load();
			final Configuration config = configurator.getConfiguration();
			
			final int N = 6;
			
			final String filepath = config.getOutput(); // where I read the simulate file
			Input input = new Input(filepath);
			
			List<MassiveParticle> allParticles = input.getParticles();
			final Double deltat = config.getDeltat();
			
			OutputAnimatedFile output = OutputAnimatedFile.getInstance(filepath + ".xyz");
			double time = 0.0;
			
			for (int i = 0; i < allParticles.size() / N; i++) {
				List<MassiveParticle> tenParticles = new ArrayList<>();
				for (int j = 0; j < N; j++) {
					tenParticles.add(allParticles.get(i * N + j));
				}
				output.write(tenParticles, time);
				time += deltat;
			}
			
			System.out.println("The animated file has been saved in: " + filepath + ".xyz");
						
		}

		public static void main(final String [] arguments)
				throws JsonParseException, JsonMappingException, IOException {

			final Configurator config = new Configurator();
			config.load();
			final String system = config.getConfiguration().getSystem();

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
					animateMode(system);
					break;
				default:
					System.out.println("[FAIL] - Invalid argument. Try 'help' for more information.");
					exit(EXIT_CODE.BAD_ARGUMENT);
					break;
			}
		}

		private static List<MassiveParticle> generateParticles() {

			// Voyager-1 properties:
			final double Re = 6371000.0;
			final Vector earth = Vector.of(1.439499338063904E+11, -4.501039010586976E+10);
			final Vector voyager = earth.versor().multiplyBy(earth.magnitude() + Re + 1500000.0);
			final Vector Vv = voyager.versor().tangent().multiplyBy(-11000.0);
			final double voyagerMass = 751.0;

			// Bodies:
			final double [][] bodies = {
				// Voyager
				{voyager.getX(), voyager.getY(), 1.0, Vv.getX(), Vv.getY(), voyagerMass},
				// Sun
				{0.0, 0.0, 6.963E+8, 0.0, 0.0, 1.988544E+30},
				// Mercury
				{5.347050011076978E+10, -1.749871636534829E+10, 2440E+3, 5.681537103747067E+3, 4.849978521587917E+4, 3.302E+23},
				// Venus
				{2.169979322974804E+10, 1.055552194021935E+11, 6051.8E+3, -3.442327831494310E+4, 6.878211961227505E+03, 4.8685E+24},
				// Earth
				{earth.getX(), earth.getY(), Re, 8.415311788888911E+3, 2.831597743867901E+4, 5.97219E+24},
				// Mars
				{1.332381415231438E+11, 1.767318038780731E+11, 3389E+3, -1.842109512919714E+4, 1.664469813414635E+4, 6.4185E+23}//,
				//{1.058409319749973E+11, 7.551533616543298E+11, 71492E+3, -1.310553498751214E+4, 2.424702557330317E+3, 1.89813E+27},
				//{-1.075592980165280E+12, 8.544801191238763E+11, 60268E+3, -6.541480412587013E+3, -7.590568168145934E+3, 5.68319E+26}
			};

			// Para escalar el radio en Ovito:
			final double [] scaleFactor = {
				15500.0,		// Voyager-1
				50.0,		// Sun
				600.0,		// Mercury
				550.0,		// Venus
				550.0,		// Earth
				500.0		// Mars
			};

			final List<MassiveParticle> particles = new ArrayList<>();
			for (int i = 0; i < bodies.length; ++i) {
				final double [] body = bodies[i];
				particles.add(new MassiveParticle(
					body[0], body[1], scaleFactor[i] * body[2],
					body[3], body[4], body[5]));
			}
			return particles;
		}

		private static List<MassiveParticle> generateSingleParticle() {
			List<MassiveParticle> particles = new ArrayList<MassiveParticle>();
			
			particles.add(new MassiveParticle(
					1.0,
					0.0,
					1.0,
					(-10.0/14.0),
					0.0,
					70.0
			));  // Single Particle
			
			return particles;
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
