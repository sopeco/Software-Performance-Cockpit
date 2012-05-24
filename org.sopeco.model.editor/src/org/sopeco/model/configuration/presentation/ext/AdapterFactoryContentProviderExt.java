package org.sopeco.model.configuration.presentation.ext;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.ui.views.properties.IPropertySource;

public class AdapterFactoryContentProviderExt extends
		AdapterFactoryContentProvider {

	public AdapterFactoryContentProviderExt(AdapterFactory adapterFactory) {
		super(adapterFactory);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected IPropertySource createPropertySource(Object object, IItemPropertySource itemPropertySource)
    {
      return new PropertySourceExt(object, itemPropertySource);
    }

}
