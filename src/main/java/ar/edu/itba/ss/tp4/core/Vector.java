
	package ar.edu.itba.ss.tp4.core;

	public class Vector {

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
	}
