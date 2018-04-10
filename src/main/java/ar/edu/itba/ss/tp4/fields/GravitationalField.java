
	package ar.edu.itba.ss.tp4.fields;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;

	public class GravitationalField implements ForceField<MassiveParticle> {

		public static final double G = 6.67191E-11;

		@Override
		public Vector apply(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return state.stream()
				.filter(p -> p != body)
				.map(p -> attraction(body, p))
				.reduce(Vector.ZERO, Vector::add);
		}

		@Override
		public boolean isVelocityDependent() {
			return false;
		}

		@Override
		public Vector derivative1(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.ZERO;
		}

		@Override
		public Vector derivative2(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.ZERO;
		}

		@Override
		public Vector derivative3(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.ZERO;
		}

		protected Vector attraction(
				final MassiveParticle p1, final MassiveParticle p2) {
			final double Δx = p2.getX() - p1.getX();
			final double Δy = p2.getY() - p1.getY();
			final double r = Math.hypot(Δx, Δy);
			final double F = G * p1.getMass() * p2.getMass() / (r * r);
			return Vector.of(F * Δx / r, F * Δy / r);
		}
	}
