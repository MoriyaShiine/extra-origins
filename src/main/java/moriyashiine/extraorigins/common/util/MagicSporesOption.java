package moriyashiine.extraorigins.common.util;

public enum MagicSporesOption {
	LEFT("Left"), OFFENSE("Offense"),
	RIGHT("Right"), DEFENSE("Defense"),
	UP("Up"), MOBILITY("Mobility"),
	NONE("None");

	private final String name;

	MagicSporesOption(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public enum BackwardsCompatibleMagicSporesMode {
		OFFENSE("Offense"), DEFENSE("Defense"), MOBILITY("Mobility"), CONVERTED("Converted");

		private final String name;

		BackwardsCompatibleMagicSporesMode(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}