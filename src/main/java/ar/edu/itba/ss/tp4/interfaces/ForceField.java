
	package ar.edu.itba.ss.tp4.interfaces;

	import java.util.List;
	import java.util.function.BiFunction;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;

	public interface ForceField<T extends MassiveParticle>
		extends BiFunction<List<T>, T, Vector> {

		public boolean isVelocityDependent();
		public boolean isConservative();

		public Vector derivative1(final List<T> state, final T body);
		public Vector derivative2(final List<T> state, final T body);
		public Vector derivative3(final List<T> state, final T body);

		public double energyLoss(final double time);
		public double potentialEnergy(final T body);

		public default double kineticEnergy(final T body) {
			final double speed = body.getSpeed();
			return 0.5 * body.getMass() * speed * speed;
		}

		public default double mechanicalEnergy(final T body) {
			return kineticEnergy(body) + potentialEnergy(body);
		}

		public default double potentialEnergy(final List<T> state) {
			return state.stream()
					.mapToDouble(this::potentialEnergy)
					.reduce(0.0, (U1, U2) -> U1 + U2);
		}

		public default double kineticEnergy(final List<T> state) {
			return state.stream()
					.mapToDouble(this::kineticEnergy)
					.reduce(0.0, (K1, K2) -> K1 + K2);
		}

		public default double mechanicalEnergy(final List<T> state) {
			return kineticEnergy(state) + potentialEnergy(state);
		}

		public default double energy(
				final List<T> state, final double time) {
			return mechanicalEnergy(state) + energyLoss(time);
		}
	}
