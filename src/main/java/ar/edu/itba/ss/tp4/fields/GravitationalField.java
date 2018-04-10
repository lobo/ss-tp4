
	package ar.edu.itba.ss.tp4.fields;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;

	public class GravitationalField implements ForceField<MassiveParticle> {

		@Override
		public Vector apply(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(0.0, 0.0);
		}

		@Override
		public boolean isVelocityDependent() {
			return false;
		}

		@Override
		public Vector derivative1(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(
					0,
					0.0);
		}

		@Override
		public Vector derivative2(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(
					0,
					0.0);
		}

		@Override
		public Vector derivative3(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(
					0,
					0.0);
		}
	}
