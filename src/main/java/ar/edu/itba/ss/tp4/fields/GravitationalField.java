
	package ar.edu.itba.ss.tp4.fields;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;

	public class GravitationalField implements ForceField<MassiveParticle> {

		public static final double G = 6.693E-11;

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
		public boolean isConservative() {
			return true;
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

		@Override
		public double energyLoss(final double time) {
			return 0.0;
		}

		@Override
		public double potentialEnergy(final MassiveParticle body) {
			return 0.0;
		}

		@Override
		public double potentialEnergy(final List<MassiveParticle> state) {
			if (state.isEmpty()) return 0.0;
			else {
				final MassiveParticle p1 = state.get(0);
				return state.stream()
						.filter(p2 -> p1 != p2)
						.mapToDouble(p2 -> potential(p1, p2))
						.reduce(0.0, (U1, U2) -> U1 + U2);
			}
		}

		protected double potential(
				final MassiveParticle p1, final MassiveParticle p2) {
			final Vector Δ = Vector.of(p2.getX() - p1.getX(), p2.getY() - p1.getY());
			return -G * p1.getMass() * p2.getMass() / Δ.magnitude();
		}

		protected Vector attraction(
				final MassiveParticle p1, final MassiveParticle p2) {
			final Vector Δ = Vector.of(p2.getX() - p1.getX(), p2.getY() - p1.getY());
			final double r = Δ.magnitude();
			return Δ.multiplyBy(G * p1.getMass() * p2.getMass() / (r * r * r));
		}
	}
