
	package ar.edu.itba.ss.tp4.interfaces;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;

	public interface Integrator<T extends MassiveParticle> {

		public List<T> getState();
		public ForceField<T> getForceField();
		public List<T> integrate(final double Δt);

		public default double getEnergy(final double time) {
			return getForceField().energy(getState(), time);
		}
	}
