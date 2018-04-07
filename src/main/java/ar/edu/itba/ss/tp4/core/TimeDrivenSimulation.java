
	package ar.edu.itba.ss.tp4.core;

	import java.util.List;
	import java.util.function.BiConsumer;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.ParticleSystem;

	public class TimeDrivenSimulation {

		protected final ParticleSystem system;
		protected final double maxTime;
		protected final double Δt;
		protected final BiConsumer<Double, List<MassiveParticle>> spy;

		public TimeDrivenSimulation(final Builder builder) {
			this.system = builder.system;
			this.maxTime = builder.maxTime;
			this.Δt = builder.Δt;
			this.spy = builder.spy;
		}

		public TimeDrivenSimulation run() {
			System.out.println("Running...");
			//system.bootstrap();
			for (double time = 0.0; time < maxTime; time += Δt) {
				System.out.println("Time: " + time);
				/*
				Inicializo la lista de partículas en el estado inicial.
				En cada ciclo uso el integrador para computar la nueva posición y velocidad.
				¿Hay que almacenar el estado de las integraciones?
				Actualizo el nuevo estado con partículas nuevas (en la misma posición).
				Repetir...
				*/
			}
			return this;
		}

		public static Builder of(final ParticleSystem system) {
			return new Builder(system);
		}

		public static class Builder {

			protected final ParticleSystem system;
			protected double maxTime;
			protected double Δt;
			protected BiConsumer<Double, List<MassiveParticle>> spy;

			public Builder(final ParticleSystem system) {
				this.system = system;
				this.maxTime = 10;
				this.Δt = 1;
				this.spy = (t, ps) -> {};
			}

			public TimeDrivenSimulation build() {
				return new TimeDrivenSimulation(this);
			}

			public Builder by(final double Δt) {
				this.Δt = Δt;
				return this;
			}

			public Builder spy(final BiConsumer<Double, List<MassiveParticle>> spy) {
				this.spy = spy;
				return this;
			}

			public Builder maxTime(final double maxTime) {
				this.maxTime = maxTime;
				return this;
			}
		}
	}
