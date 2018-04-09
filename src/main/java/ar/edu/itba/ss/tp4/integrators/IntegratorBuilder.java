
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public abstract class IntegratorBuilder<T extends Integrator<MassiveParticle>> {

		protected final ForceField<MassiveParticle> force;
		protected List<MassiveParticle> state;

		public IntegratorBuilder(
				final ForceField<MassiveParticle> force) {
			this.force = force;
			this.state = new ArrayList<>();
		}

		public IntegratorBuilder<T> withInitial(
				final List<MassiveParticle> state) {
			this.state = state;
			return this;
		}

		public abstract T build();
	}
