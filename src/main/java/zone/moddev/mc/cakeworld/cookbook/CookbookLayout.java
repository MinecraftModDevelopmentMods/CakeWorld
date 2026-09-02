package zone.moddev.mc.cakeworld.cookbook;

/**
 * Pure layout calculations for the Explorer's Cookbook.
 *
 * Keeping the calculations outside the client screen makes the scaled-window
 * contract deterministic and dedicated-server safe to test.
 */
public record CookbookLayout(int left, int top, int width, int height,
		int tabColumns, int tabRows) {
	public static final int MAX_WIDTH = 390;
	public static final int MAX_HEIGHT = 210;
	public static final int TAB_HEIGHT = 18;
	private static final int SCREEN_MARGIN = 12;
	private static final int TAB_TOP_OFFSET = 25;

	public static CookbookLayout calculate(int screenWidth, int screenHeight,
			int tabCount) {
		if (screenWidth < 1 || screenHeight < 1) {
			throw new IllegalArgumentException(
					"Screen dimensions must be positive");
		}
		if (tabCount < 1) {
			throw new IllegalArgumentException(
					"At least one Cookbook tab is required");
		}

		int bookWidth = Math.min(MAX_WIDTH,
				Math.max(1, screenWidth - SCREEN_MARGIN * 2));
		int bookHeight = Math.min(MAX_HEIGHT,
				Math.max(1, screenHeight - SCREEN_MARGIN * 2));
		int columns;
		if (bookWidth >= 270) {
			columns = Math.min(tabCount, 3);
		} else if (bookWidth >= 150) {
			columns = Math.min(tabCount, 2);
		} else {
			columns = 1;
		}
		int rows = (tabCount + columns - 1) / columns;
		return new CookbookLayout((screenWidth - bookWidth) / 2,
				(screenHeight - bookHeight) / 2, bookWidth, bookHeight,
				columns, rows);
	}

	public int tabLeft(int index) {
		return left + index % tabColumns * width / tabColumns;
	}

	public int tabRight(int index) {
		int column = index % tabColumns;
		return left + (column + 1) * width / tabColumns;
	}

	public int tabTop(int index) {
		return top + TAB_TOP_OFFSET
				+ index / tabColumns * TAB_HEIGHT;
	}

	public int tabIndexAt(double mouseX, double mouseY, int tabCount) {
		for (int index = 0; index < tabCount; index++) {
			if (mouseX >= tabLeft(index) && mouseX < tabRight(index)
					&& mouseY >= tabTop(index)
					&& mouseY < tabTop(index) + TAB_HEIGHT) {
				return index;
			}
		}
		return -1;
	}

	public int headingTop() {
		return top + TAB_TOP_OFFSET + tabRows * TAB_HEIGHT + 8;
	}

	public int pageTop() {
		return headingTop() + 15;
	}

	public int footerTop() {
		return top + height - 34;
	}

	public int pageColumns() {
		return width >= 280 ? 2 : 1;
	}

	public int pageRows() {
		return Math.max(0, Math.min(6, (footerTop() - pageTop()) / 19));
	}

	public int visiblePageCapacity() {
		return pageColumns() * pageRows();
	}
}
