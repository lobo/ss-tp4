
	package ar.edu.itba.ss.tp4.interfaces;

	import java.util.List;
	import java.util.function.BiFunction;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;

	public interface ForceField<T extends MassiveParticle>
		extends BiFunction<List<T>, T, Vector> {

		public boolean isVelocityDependent();

		public Vector derivative1(final List<T> state, final T body);
		public Vector derivative2(final List<T> state, final T body);
		public Vector derivative3(final List<T> state, final T body);
	}
