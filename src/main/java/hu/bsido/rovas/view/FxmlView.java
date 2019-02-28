package hu.bsido.rovas.view;

import java.util.ResourceBundle;

public enum FxmlView {

	MAIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("main.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/MainView.fxml";
		}
	},
	CONVERT {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("convert.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/ConvertView.fxml";
		}
	};
	
	public abstract String getTitle();
	public abstract String getFxmlFile();
	
	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
	
}
