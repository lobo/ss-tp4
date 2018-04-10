
	package ar.edu.itba.ss.tp4.fields;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;

	public class HarmonicOscillator implements ForceField<MassiveParticle> {

		public static final double k = 10000.0;
		public static final double m = 70.0;
		public static final double γ = 100.0;
		public static final double A = 1.0;

		protected static final double FACTORS [][] = {
			{-γ,							-k},
			{γ*γ - k,						k*γ},
			{2.0*k*γ - γ*γ*γ,				k*k - k*γ*γ},
			{k*k - 3.0*k*γ*γ + γ*γ*γ*γ,		k*γ*γ*γ - 2.0*k*k*γ}
		};

		@Override
		public Vector apply(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(
					FACTORS[0][0] * body.getVx() + FACTORS[0][1] * body.getX(),
					0.0);
		}

		@Override
		public boolean isVelocityDependent() {
			return true;
		}

		@Override
		public boolean isConservative() {
			return false;
		}

		@Override
		public Vector derivative1(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(
					FACTORS[1][0] * body.getVx() + FACTORS[1][1] * body.getX(),
					0.0);
		}

		@Override
		public Vector derivative2(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(
					FACTORS[2][0] * body.getVx() + FACTORS[2][1] * body.getX(),
					0.0);
		}

		@Override
		public Vector derivative3(
				final List<MassiveParticle> state,
				final MassiveParticle body) {
			return Vector.of(
					FACTORS[3][0] * body.getVx() + FACTORS[3][1] * body.getX(),
					0.0);
		}

		@Override
		public double work(final double time) {
			// Verify!
			return 0.5 * k * A * A * Math.exp(-time * γ / m);
		}

		@Override
		public double potentialEnergy(final MassiveParticle body) {
			// Verify!
			return 0.5 * k * (body.getX() * body.getX() + body.getY() * body.getY());
		}
	}
