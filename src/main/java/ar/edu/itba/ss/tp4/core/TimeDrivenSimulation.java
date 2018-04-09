
	package ar.edu.itba.ss.tp4.core;

	import java.util.List;
	import java.util.function.BiConsumer;

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
			this.integrator = builder.integrator;
			this.spy = builder.spy;
			this.maxTime = builder.maxTime;
			this.Δt = builder.Δt;
		}

		public static Builder of(final Integrator<MassiveParticle> integrator) {
			return new Builder(integrator);
		}

		public TimeDrivenSimulation run() {
			System.out.println("Running...");
			final long startTime = System.nanoTime();
			spy.accept(0.0, integrator.getState());
			for (double time = Δt; time <= maxTime; time += Δt) {
				spy.accept(time, integrator.integrate(Δt));
				System.out.print("\t\tTime reached: " + time + "\r");
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
