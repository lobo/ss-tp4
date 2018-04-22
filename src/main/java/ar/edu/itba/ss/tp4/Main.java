
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
			
			final int N = generateParticles().size();
			
			final String filepath = config.getOutput(); // where I read the simulate file
			
			// !!!!!!!!!!!!!!!!!!!!
			// Ahora input tiene una List con todos los gigabytes en memoria, y esto explota...
			// Hay que leer y descargar en el archivo, no retener todo el archivo en memoria.
			// Tampoco hay que abrir el archivo en cada lectura, sino explota también.
			Input input = new Input(filepath);
			
			List<MassiveParticle> allParticles = input.getParticles();
			final Double deltat = config.getDeltat();
			
			OutputAnimatedFile output = OutputAnimatedFile.getInstance(filepath + ".xyz");
			double time = 0.0;

			for (int i = 0; i < allParticles.size() / N; i++) {
				if (i % 100 != 0) {
					// Tomar solo 1 de cada 100 chunks... (era una prueba)
					time += deltat;
					continue;
				}
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

			// Bodies (ephemeris at "1977-Sep-05 14:00"):
			final double [][] bodies = {
				{1.443746378444645E+08, -4.358198744660320E+07, 1.0, 1.595490974500632E+01, 4.052412934382448E+01, 721.9},
				{0.0, 0.0, 695508.0, 0.0, 0.0, 1.988544E+30},
				{5.370665976469804E+07, -1.503869845918308E+07, 2440.0, 3.679431626955593E+00, 4.910738195860873E+01, 3.302E+23},
				{1.996201495050072E+07, 1.058876475713890E+08, 6051.8, -3.453463426771592E+01, 6.313060885309132E+00, 48.685E+23},
				{1.443669994119172E+08, -4.358104129621790E+07, 6378.14, 8.134726841544820E+00, 2.840376536175890E+01, 5.97219E+24},
				{1.323076527142476E+08, 1.775679463400601E+08, 3389.9, -1.850294805029247E+01, 1.653549992295316E+01, 6.4185E+23},
				{1.051803449030906E+08, 7.552752897348490E+08, 71492.0, -1.310827553298509E+01, 2.413213261673731E+00, 1898.13E+24},
				{-1.075922593297449E+09, 8.540975000330083E+08, 60268.0, -6.538402765705666E+00, -7.592714108900433E+00, 5.68319E+26},
				//{-2.077011943942374E+09, -1.849240257862967E+09, 25559.0, 4.464954692356894E+00, -5.403290238393856E+00, 86.8103E+24},
				//{-1.124696717554022E+09, -4.387733548085129E+09, 24766.0, 5.217279590256633E+00, -1.318879838198444E+00, 102.41E+24}
			};

			// Dynamic values for Voyager-1:
			final Vector earth = Vector.of(bodies[4][0], bodies[4][1]);
			final Vector Ve = Vector.of(bodies[4][3], bodies[4][4]);
			final Vector voyager = earth.versor().multiplyBy(earth.magnitude() + bodies[4][2] + 1500.0);
			final Vector Vn = voyager.versor().multiplyBy(-1.0);
			final Vector Vv = Ve.add(Vn.tangent().multiplyBy(11.0));

			System.out.println("Real Pv:    " + bodies[0][0] + " " + bodies[0][1]);
			System.out.println("Dynamic Pv: " + voyager.getX() + " " + voyager.getY());
			System.out.println("Delta Pv:   " + (voyager.magnitude() - Math.hypot(bodies[0][0], bodies[0][1])) + "\n");

			System.out.println("Real Vv:    " + bodies[0][3] + " " + bodies[0][4]);
			System.out.println("Dynamic Vv: " + Vv.getX() + " " + Vv.getY());
			System.out.println("Delta Vv:   " + (Vv.magnitude() - Math.hypot(bodies[0][3], bodies[0][4])) + "\n");

			// Para escalar el radio en Ovito:
			final double [] scaleFactor = {
				1.0E+7,		// Voyager-1
				60.0,		// Sun
				1400.0,		// Mercury
				1400.0,		// Venus
				1400.0,		// Earth
				1400.0,		// Mars
				350.0,		// Jupiter
				325.0,		// Saturn
				//300.0,	// Uranus
				//300.0		// Neptune
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
