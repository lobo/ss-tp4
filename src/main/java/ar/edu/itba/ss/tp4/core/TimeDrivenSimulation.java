
	package ar.edu.itba.ss.tp4.core;

	import java.util.function.BiConsumer;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.integrators.GearIntegrator;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;
	import ar.edu.itba.ss.tp4.interfaces.ParticleSystem;

	public class TimeDrivenSimulation {

		protected final ParticleSystem system;
		protected final Integrator integrator;
		protected final double Δt;
		protected BiConsumer<Double, MassiveParticle> spy;

		public TimeDrivenSimulation(final Builder builder) {
			this.system = builder.system;
			this.integrator = builder.integrator;
			this.Δt = builder.Δt;
			this.spy = builder.spy;
		}

		public TimeDrivenSimulation run() {
			System.out.println("Running...");
			return this;
		}

		public static Builder of(final ParticleSystem system) {
			return new Builder(system);
		}

		public static class Builder {

			protected final ParticleSystem system;
			protected Integrator integrator;
			protected double Δt;
			protected BiConsumer<Double, MassiveParticle> spy;

			public Builder(final ParticleSystem system) {
				this.system = system;
				this.integrator = new GearIntegrator();
				this.Δt = 1;
				this.spy = (t, ps) -> {};
			}

			public TimeDrivenSimulation build() {
				return new TimeDrivenSimulation(this);
			}

			public Builder with(final Integrator integrator) {
				this.integrator = integrator;
				return this;
			}

			public Builder by(final double Δt) {
				this.Δt = Δt;
				return this;
			}

			public Builder spy(final BiConsumer<Double, MassiveParticle> spy) {
				this.spy = spy;
				return this;
			}
		}
	}
