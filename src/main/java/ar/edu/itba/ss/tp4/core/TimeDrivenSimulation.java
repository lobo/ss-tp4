
	package ar.edu.itba.ss.tp4.core;

	import java.util.List;
	import java.util.function.BiConsumer;
	import java.util.stream.LongStream;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class TimeDrivenSimulation<T extends MassiveParticle> {

		protected final Integrator<T> integrator;
		protected final BiConsumer<Double, List<T>> spy;
		protected final double maxTime;
		protected final double Δt;
		protected final boolean reportEnergy;
		protected final boolean reportTime;

		public TimeDrivenSimulation(final Builder<T> builder) {
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
			this.reportEnergy = builder.reportEnergy;
			this.reportTime = builder.reportTime;
		}

		public static <T extends MassiveParticle> Builder<T> of(final Integrator<T> integrator) {
			return new Builder<T>(integrator);
		}

		public TimeDrivenSimulation<T> run() {
			System.out.println("\nRunning...");
			final double initialEnergy = reportEnergy? integrator.getEnergy(0.0) : 0.0;
			final double [] energyBounds = {
				initialEnergy,
				initialEnergy
			};
			if (reportEnergy) {
				System.out.println(
					"Initial Energy (t = 0.0): " +
					initialEnergy + " [J]");
			}
			spy.accept(0.0, integrator.getState());
			final long startTime = System.nanoTime();
			LongStream.rangeClosed(1, Math.round(maxTime/Δt))
				.mapToDouble(k -> k * Δt)
				.forEachOrdered(time -> {
					spy.accept(time, integrator.integrate(Δt));
					if (reportTime && Math.round(time/Δt) % 1000 == 0) {
						System.out.print("\t\tTime reached: " + time + " [s]                    \r");
					}
					if (reportEnergy) {
						final double energy = integrator.getEnergy(time);
						energyBounds[0] = Math.min(energyBounds[0], energy);
						energyBounds[1] = Math.max(energyBounds[1], energy);
					}
				});
			if (reportEnergy) {
				final double ΔEnergy = energyBounds[1] - energyBounds[0];
				final double proportion = 100.0 * Math.abs(ΔEnergy/initialEnergy);
				System.out.println("\n\n\tMinimum Energy: " + energyBounds[0] + " [J]");
				System.out.println("\tMaximum Energy: " + energyBounds[1] + " [J]");
				System.out.println("\tDelta Energy: " + ΔEnergy + " [J]");
				if (initialEnergy != 0.0) {
					System.out.println("\tDelta Energy (percentage): " + proportion + " %");
				}
			}
			System.out.println(
				"\n\n\tEnd simulation in " +
				1E-9 * (System.nanoTime() - startTime) + " sec.\n");
			return this;
		}

		public static class Builder<T extends MassiveParticle> {

			protected final Integrator<T> integrator;
			protected BiConsumer<Double, List<T>> spy;
			protected double maxTime;
			protected double Δt;
			protected boolean reportEnergy;
			protected boolean reportTime;

			public Builder(final Integrator<T> integrator) {
				this.integrator = integrator;
				this.spy = (t, ps) -> {};
				this.maxTime = 10.0;
				this.Δt = 1.0;
				this.reportTime = true;
				this.reportEnergy = false;
			}

			public TimeDrivenSimulation<T> build() {
				return new TimeDrivenSimulation<>(this);
			}

			public Builder<T> spy(final BiConsumer<Double, List<T>> spy) {
				this.spy = spy;
				return this;
			}

			public Builder<T> maxTime(final double maxTime) {
				this.maxTime = maxTime;
				return this;
			}

			public Builder<T> by(final double Δt) {
				this.Δt = Δt;
				return this;
			}

			public Builder<T> reportEnergy(final boolean report) {
				this.reportEnergy = report;
				return this;
			}

			public Builder<T> reportTime(final boolean report) {
				this.reportTime = report;
				return this;
			}
		}
	}
