
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class VelocityVerlet implements Integrator {

		public VelocityVerlet() {
		}

		@Override
		public Integrator setup(final List<MassiveParticle> particles) {
			return this;
		}

		@Override
		public List<MassiveParticle> integrate(final double Δt) {
			return null;
		}
	}
