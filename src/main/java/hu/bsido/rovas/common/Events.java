package hu.bsido.rovas.common;

import java.io.File;

import hu.bsido.rovas.common.LRResources.SlideDirection;
import hu.bsido.rovas.view.FxmlView;

public abstract class Events {

	public static class AddSceneEvent {
		public final FxmlView view;
		public final SlideDirection direction;
		public AddSceneEvent(FxmlView view, SlideDirection sdir) {
			this.view = view;
			this.direction = sdir;
		}
	}
	
	public static class RemoveLastSceneEvent {
		public final SlideDirection direction;
		public final Object toUnreg;
		public RemoveLastSceneEvent(Object toUnreg, SlideDirection sdir) {
			this.direction = sdir;
			this.toUnreg = toUnreg;
		}
	}
	
	public static class FillConvertEvent {
		public final File file;
		public FillConvertEvent(File file) {
			this.file = file;
		}
	}
}
