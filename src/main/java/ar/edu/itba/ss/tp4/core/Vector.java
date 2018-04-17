
	package ar.edu.itba.ss.tp4.core;

	public class Vector {

		public static final Vector ZERO = Vector.of(0.0, 0.0);

		protected final double x;
		protected final double y;

		public static Vector of(final double x, final double y) {
			return new Vector(x, y);
		}

		public Vector(final double x, final double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public Vector add(final Vector vector) {
			return Vector.of(x + vector.x, y + vector.y);
		}

		public Vector subtract(final Vector vector) {
			return Vector.of(x - vector.x, y - vector.y);
		}

		public Vector multiplyBy(final double value) {
			return Vector.of(x*value, y*value);
		}

		public Vector dividedBy(final double value) {
			return Vector.of(x/value, y/value);
		}

		public Vector power(final double exponent) {
			return Vector.of(Math.pow(x, exponent), Math.pow(y, exponent));
		}

		public Vector versor() {
			return Vector.of(x, y).dividedBy(magnitude());
		}

		public double magnitude() {
			return Math.hypot(x, y);
		}

		public Vector tangent() {
			return Vector.of(y, -x);
		}
	}
