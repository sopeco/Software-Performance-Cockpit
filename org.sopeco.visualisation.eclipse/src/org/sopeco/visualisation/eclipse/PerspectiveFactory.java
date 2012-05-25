package org.sopeco.visualisation.eclipse;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;

public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		layout.addView(PersistenceNavigation.viewID, IPageLayout.LEFT, 0.25f,
				editorArea);
		// TODO: add further views
//		layout.addView(ViewsTreeView.viewID, IPageLayout.BOTTOM, 0.5f,
//				DataSetTreeView.viewID);
//		layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM, 0.66f,
//				editorArea);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
	}

}
