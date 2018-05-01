
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.MassiveParticleFactory;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public abstract class IntegratorBuilder
		<T extends MassiveParticle, U extends Integrator<T>> {

		protected final ForceField<T> force;
		protected List<T> state;
		protected MassiveParticleFactory<T> factory;

		public IntegratorBuilder(
				final ForceField<T> force) {
			this.force = force;
			this.state = new ArrayList<>();
		}

		public IntegratorBuilder<T, U> withInitial(
				final List<T> state) {
			this.state = state;
			return this;
		}

		public IntegratorBuilder<T, U> factory(
				final MassiveParticleFactory<T> factory) {
			this.factory = factory;
			return this;
		}

		public abstract U build();
	}
