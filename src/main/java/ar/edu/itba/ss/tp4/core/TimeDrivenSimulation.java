
	package ar.edu.itba.ss.tp4.core;

	import java.util.List;
	import java.util.function.BiConsumer;
	import java.util.stream.LongStream;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class TimeDrivenSimulation {

		protected final Integrator<MassiveParticle> integrator;
		protected final BiConsumer<Double, List<MassiveParticle>> spy;
		protected final double maxTime;
		protected final double Δt;

		public TimeDrivenSimulation(final Builder builder) {
			System.out.println(
				"Time-Driven Simulation (T-max = " + builder.maxTime +
				" [s], Time Step = " + builder.Δt + " [s])");
			System.out.println("Integrator: " + builder.integrator
				.getClass().getSimpleName());
			System.out.println("Force Field: " + builder.integrator
				.getForceField().getClass().getSimpleName());
			this.integrator = builder.integrator;
			this.spy = builder.spy;
			this.maxTime = builder.maxTime;
			this.Δt = builder.Δt;
		}

		public static Builder of(final Integrator<MassiveParticle> integrator) {
			return new Builder(integrator);
		}

		public TimeDrivenSimulation run() {
			System.out.println("\nRunning...");
			final double initialEnergy = integrator.getEnergy(0.0);
			final double [] energyBounds = {
				initialEnergy,
				initialEnergy
			};
			System.out.println(
				"Initial Energy (t = 0.0): " +
				initialEnergy + " [J]");
			spy.accept(0.0, integrator.getState());
			final long startTime = System.nanoTime();
			LongStream.rangeClosed(1, Math.round(maxTime/Δt))
				.mapToDouble(k -> k * Δt)
				.forEachOrdered(time -> {
					spy.accept(time, integrator.integrate(Δt));
					System.out.print("\t\tTime reached: " + time + " [s]                    \r");
					final double energy = integrator.getEnergy(time);
					energyBounds[0] = Math.min(energyBounds[0], energy);
					energyBounds[1] = Math.max(energyBounds[1], energy);
				});
			final double ΔEnergy = energyBounds[1] - energyBounds[0];
			final double proportion = 100.0 * Math.abs(ΔEnergy/initialEnergy);
			System.out.println("\n\n\tMinimum Energy: " + energyBounds[0] + " [J]");
			System.out.println("\tMaximum Energy: " + energyBounds[1] + " [J]");
			System.out.println("\tDelta Energy: " + ΔEnergy + " [J]");
			if (initialEnergy != 0.0) {
				System.out.println("\tDelta Energy (percentage): " + proportion + " %");
			}
			System.out.println(
				"\n\n\tEnd simulation in " +
				1E-9 * (System.nanoTime() - startTime) + " sec.\n");
			return this;
		}

		public static class Builder {

			protected final Integrator<MassiveParticle> integrator;
			protected BiConsumer<Double, List<MassiveParticle>> spy;
			protected double maxTime;
			protected double Δt;

			public Builder(final Integrator<MassiveParticle> integrator) {
				this.integrator = integrator;
				this.spy = (t, ps) -> {};
				this.maxTime = 10.0;
				this.Δt = 1.0;
			}

			public TimeDrivenSimulation build() {
				return new TimeDrivenSimulation(this);
			}

			public Builder spy(final BiConsumer<Double, List<MassiveParticle>> spy) {
				this.spy = spy;
				return this;
			}

			public Builder maxTime(final double maxTime) {
				this.maxTime = maxTime;
				return this;
			}

			public Builder by(final double Δt) {
				this.Δt = Δt;
				return this;
			}
		}
	}
